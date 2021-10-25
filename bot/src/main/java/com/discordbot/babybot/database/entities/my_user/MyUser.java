package com.discordbot.babybot.database.entities.my_user;


import net.dv8tion.jda.api.entities.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class MyUser {
    @Id
    private String id;
    private String username;
    private String avatarUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public static MyUser convert(User user) {
        MyUser myUser = new MyUser();
        myUser.setUsername(user.getName());
        myUser.setAvatarUrl(user.getAvatarUrl());
        myUser.setId(user.getId());
        return myUser;
    }
}
