package com.pony.service.impl;

import com.pony.NotFoundException;
import com.pony.dao.TypeRepository;
import com.pony.domain.Type;
import com.pony.service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author pony
 * @date 2020/9/10
 */
@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    private TypeRepository typeRepository;

    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

     @Transactional
    @Override
    public Type getType(long id) {
        return typeRepository.getOne(id);
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Type updateType(long id, Type type) {
        Type type1 = typeRepository.getOne(id);
        if(type1==null){
            throw new NotFoundException("不存在改类型");
        }else {
            BeanUtils.copyProperties(type,type1);
            return typeRepository.save(type1);
        }
    }

    @Transactional
    @Override
    public void deleteType(long id) {
        typeRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Type getTypeByName(String name) {
        return typeRepository.findByName(name);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Override
    public List<Type> listTypeTop(int size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable =  PageRequest.of(0,size,sort);
        return typeRepository.findTop(pageable);
    }
}
