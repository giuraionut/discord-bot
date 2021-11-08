package com.discordbot.babybot.database.entities.my_user;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "user")
@AllArgsConstructor
public class MyUserController {
    @Autowired
    private final MyUserService myUserService;

    @PostMapping
    public void addUser(@RequestBody MyUser myUser) {
        this.myUserService.save(myUser);
    }

    @GetMapping("{id}")
    public ResponseEntity<MyUser> getUser(@PathVariable("id") String id) {
        MyUser myUser = this.myUserService.get(id);
        return new ResponseEntity<>(myUser, HttpStatus.OK);
    }
}
