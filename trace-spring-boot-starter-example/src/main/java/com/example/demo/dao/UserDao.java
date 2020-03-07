package com.example.demo.dao;

import com.example.demo.model.User;
import org.springframework.stereotype.Component;

/**
 * 
 * @author lastwhisper
 * @date 12/22/2019
 */
@Component
public class UserDao {

    public User findUserByUserDao(User user){
        return new User(user.getUserId(), user.getUsername());
    }
}
