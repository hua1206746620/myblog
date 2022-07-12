package com.myblog.myblog.service.impl;


import com.myblog.myblog.dao.UserDao;
import com.myblog.myblog.entity.User;
import com.myblog.myblog.service.UserService;
import com.myblog.myblog.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User checkUser(String username, String password) {
        User user = userDao.queryByUsernameAndPassword(username, MD5Utils.code(password));
        return user;
    }
}
