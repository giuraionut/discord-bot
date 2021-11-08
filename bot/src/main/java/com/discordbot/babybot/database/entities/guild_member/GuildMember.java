package com.discordbot.babybot.database.entities.guild_member;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "members")
public class GuildMember {
    @Id
    private GuildMemberId id;

    private List<String> roles;

    public GuildMemberId getId() {
        return id;
    }

    public void setId(GuildMemberId id) {
        this.id = id;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
