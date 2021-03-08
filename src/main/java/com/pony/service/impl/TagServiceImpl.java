package com.pony.service.impl;

import com.pony.NotFoundException;
import com.pony.dao.TagRepository;
import com.pony.domain.Tag;
import com.pony.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author pony
 * @date 2020/9/11
 */
@Service
@Transactional
public class TagServiceImpl implements TagService {

    @Autowired
    TagRepository repository;

    @Override
    public Tag saveTag(Tag tag) {
        return repository.save(tag);
    }

    @Override
    public Tag getTag(long id) {
        return repository.getOne(id);
    }

    @Override
    public void deleteTag(long id) {
        repository.deleteById(id);
    }

    @Override
    public Tag updateTag(long id, Tag tag) {
        Tag t = repository.getOne(id);
        if (t == null) {
            throw new NotFoundException("不存在改标签");
        } else {
            BeanUtils.copyProperties(t, tag);
            return repository.save(t);
        }
    }

    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Tag getTagByName(String name) {
        return repository.getTagByName(name);
    }

    @Override
    public List<Tag> listTag() {
        return repository.findAll();
    }

    @Override
    public List<Tag> listTagTop(int size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        return repository.findTop(pageable);
    }

    //根据Id字符串得到相应的标签
    @Override
    public List<Tag> listTag(String ids) {
        ArrayList<Tag> tags = new ArrayList<>();
        if (ids != null && !("".equals(ids))){
            String[] idArray = ids.split(",");
            for(int i=0;i<idArray.length;++i){
                Tag tmp = repository.getOne(Long.valueOf(idArray[i]));
                tags.add(tmp);
            }
        }
        return tags;
    }

}
