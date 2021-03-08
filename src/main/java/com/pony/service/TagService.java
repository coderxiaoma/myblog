package com.pony.service;

import com.pony.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author pony
 * @date 2020/9/11
 */
public interface TagService {
    Tag saveTag(Tag tag);
    Tag getTag(long id);
    void deleteTag(long id);
    Tag updateTag(long id,Tag tag);
    Page<Tag> listTag(Pageable pageable);
    Tag getTagByName(String name);
    List<Tag> listTag();
    //返回指定数量的博客数量最多的tag标签
    List<Tag> listTagTop(int size);
    //通过标签ID得到相应的标签
    List<Tag> listTag(String ids);
}
