package com.pony.service;

import com.pony.domain.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author pony
 * @date 2020/9/10
 */
public interface TypeService {
    Type saveType(Type type);
    Type getType(long id);
    Page<Type> listType(Pageable pageable);
    Type updateType(long id,Type type);
    void  deleteType(long id);
    Type getTypeByName(String name);
    List<Type> listType();
    List<Type> listTypeTop(int size);
}
