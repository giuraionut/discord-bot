package com.discordbot.babybot.database.entities.guild_member;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildMemberRepository extends MongoRepository<GuildMember, GuildMemberId> {
}
