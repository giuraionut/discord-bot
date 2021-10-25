package com.discordbot.babybot.database.entities.my_guild;

import net.dv8tion.jda.api.entities.Guild;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "guilds")
public class MyGuild {

    @Id
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static MyGuild convert(Guild guild) {
        MyGuild myGuild = new MyGuild();
        myGuild.setId(guild.getId());
        myGuild.setName(guild.getName());
        return myGuild;
    }
}
