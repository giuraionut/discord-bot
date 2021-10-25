package com.discordbot.babybot.database.entities.my_guild;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyGuildRepository extends MongoRepository<MyGuild, String> {
}
