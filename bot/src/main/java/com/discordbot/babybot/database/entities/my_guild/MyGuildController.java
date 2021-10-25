package com.discordbot.babybot.database.entities.my_guild;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "guild")
@AllArgsConstructor
public class MyGuildController {

    @Autowired
    private MyGuildService myGuildService;

    @PostMapping
    public void addGuild(@RequestBody MyGuild myGuild)
    {
        this.myGuildService.save(myGuild);
    }
}
