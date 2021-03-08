package com.pony.service.impl;

import com.pony.NotFoundException;
import com.pony.dao.BlogRepository;
import com.pony.domain.Blog;
import com.pony.domain.Type;
import com.pony.service.BlogService;
import com.pony.util.MarkdownUtils;
import com.pony.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @author pony
 * @date 2020/9/12
 */
@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    BlogRepository repository;


    @Override
    public Blog getBlog(long id) {
        return repository.getOne(id);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {
        return repository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if (blog.getTitle() != null && !"".equals(blog.getTitle())) {
                    predicates.add(criteriaBuilder.like(root.<String>get("title"), "%" + blog.getTitle() + "%"));
                }
                if ((Long) blog.getTypeId() != null) {
                    predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"), blog.getTypeId()));
                }
                if (blog.isRecommend()) {
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"), blog.isRecommend()));
                }
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(long tagId, Pageable pageable) {
        return repository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"), tagId);
            }
        }, pageable);
    }

    @Override
    public List<Blog> listRecommendBlogTop(int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updateTime");
        Pageable pageable = PageRequest.of(0, size, sort);
        return repository.findRecommendTop(pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years = repository.findGroupYear();
        Map<String, List<Blog>> map = new HashMap<>();
        for (String year : years) {
            map.put(year, repository.findByYear(year));
        }
        return map;
    }

    @Override
    public Blog saveBlog(Blog blog) {
        return repository.save(blog);
    }

    @Override
    public Blog updateBlog(long id, Blog blog) {
        Blog b = repository.getOne(id);
        if (b == null) {
            throw new NotFoundException("博客不存在");
        }
        blog.setUpdateTime(new Date());
        BeanUtils.copyProperties(b, blog);
        return repository.save(b);
    }

    @Override
    public void deleteBlog(long id) {
        repository.deleteById(id);
    }

    @Override
    public Blog getAndConvert(long id) {
        Blog blog = repository.getOne(id);
        if (blog == null) {
            throw new NotFoundException("该博客不存在");
        }
        blog.setViews(blog.getViews() + 1);
        repository.save(blog);
        Blog b = new Blog();
        BeanUtils.copyProperties(blog, b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        return b;
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return repository.findByQuery(query, pageable);
    }

    @Override
    public int countBlog() {
        return (int) repository.count();
    }
}
