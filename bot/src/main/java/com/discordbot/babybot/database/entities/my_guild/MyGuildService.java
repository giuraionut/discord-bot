package com.discordbot.babybot.database.entities.my_guild;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyGuildService {

    @Autowired
    private MyGuildRepository myGuildRepository;

    public void save(MyGuild myGuild) {
        myGuildRepository.save(myGuild);
    }

    public MyGuild get(String id)
    {
        return myGuildRepository.findById(id).orElse(null);
    }
}
