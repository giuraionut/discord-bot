package com.discordbot.babybot.database.entities.my_user;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyUserService {
    @Autowired
    private MyUserRepository myUserRepository;

    public void save(MyUser myUser) {
        this.myUserRepository.save(myUser);
    }

    public MyUser get(String id) {
        return this.myUserRepository.findById(id).orElse(null);
    }
}
