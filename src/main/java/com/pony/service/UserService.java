package com.pony.service;

import com.pony.domain.User;

/**
 * @author pony
 * @date 2020/9/10
 */
public interface UserService {
    User checkUser(String username,String password);
}
