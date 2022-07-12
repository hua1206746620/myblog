package com.myblog.myblog.service;


import com.myblog.myblog.entity.User;

public interface UserService {

    public User checkUser(String username, String password);
}
