package com.discordbot.babybot.database.entities.guild_member;

import com.discordbot.babybot.database.entities.my_guild.MyGuild;
import com.discordbot.babybot.database.entities.my_user.MyUser;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serializable;

public class GuildMemberId implements Serializable {
    @DBRef
    private MyUser myUser;

    @DBRef
    private MyGuild myGuild;

    public MyUser getMyUser() {
        return myUser;
    }

    public void setMyUser(MyUser myUser) {
        this.myUser = myUser;
    }

    public MyGuild getMyGuild() {
        return myGuild;
    }

    public void setMyGuild(MyGuild myGuild) {
        this.myGuild = myGuild;
    }

    public String asString()
    {
        return myUser.getId()+"/"+myGuild.getId();
    }
}
