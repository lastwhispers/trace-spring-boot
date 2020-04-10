package com.example.demo.core.dao;

import com.example.demo.core.model.User;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 
 * @author lastwhisper
 * @date 12/22/2019
 */
@Component
public class UserDao {

    public User findUserByUserDao(User user){
        try {
            Thread.sleep(new Random().nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new User(user.getUserId(), user.getUsername());
    }
}
