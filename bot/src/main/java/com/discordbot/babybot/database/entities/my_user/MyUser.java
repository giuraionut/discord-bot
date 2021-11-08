package com.discordbot.babybot.database.entities.my_user;


import com.discordbot.babybot.reddit.reddit_configuration.RedditInfoAPI;
import com.discordbot.babybot.reddit.reddit_configuration.RedditLoginInfo;
import lombok.Data;
import net.dv8tion.jda.api.entities.User;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "users")
public class MyUser {
    @Id
    private String id;

    private String username;

    private String avatarUrl;

    private RedditLoginInfo redditLoginInfo;

    private RedditInfoAPI redditInfoAPI;

    public static MyUser convert(User user) {
        MyUser myUser = new MyUser();
        myUser.setUsername(user.getName());
        myUser.setAvatarUrl(user.getAvatarUrl());
        myUser.setId(user.getId());
        return myUser;
    }

    @Override
    public String toString() {
        return "MyUser{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}
