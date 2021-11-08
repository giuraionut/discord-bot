package com.discordbot.babybot.database.entities.guild_member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GuildMemberService {
    @Autowired
    private GuildMemberRepository guildMemberRepository;

    public void save(GuildMember guildMember) {
        this.guildMemberRepository.save(guildMember);
    }

    public GuildMember get(GuildMemberId guildMemberId) {
        return this.guildMemberRepository.findById(guildMemberId).orElse(null);
    }
}
