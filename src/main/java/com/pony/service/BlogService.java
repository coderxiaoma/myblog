package com.pony.service;

import com.pony.domain.Blog;
import com.pony.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * @author pony
 * @date 2020/9/12
 */
public interface BlogService {
    Blog getBlog(long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(long tagId,Pageable pageable);

    List<Blog> listRecommendBlogTop(int size);

    Map<String,List<Blog>> archiveBlog();

    Blog saveBlog(Blog blog);

    Blog updateBlog(long id,Blog blog);

    void deleteBlog(long id);

    Blog getAndConvert(long id);

    Page<Blog> listBlog(String query, Pageable pageable);

    int countBlog();
}
