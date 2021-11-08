package com.discordbot.babybot.database.utils;

import com.discordbot.babybot.database.entities.guild_member.GuildMember;
import com.discordbot.babybot.database.entities.guild_member.GuildMemberId;
import com.discordbot.babybot.database.entities.my_guild.MyGuild;
import com.discordbot.babybot.database.entities.my_user.MyUser;
import com.discordbot.babybot.http.SelfHttpReq;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CollectData {
    private final SelfHttpReq selfHttpReq = new SelfHttpReq();
    private final Logger log = LoggerFactory.getLogger(CollectData.class);

    @SneakyThrows
    public void collect(List<Guild> guilds) {
        TimeUnit.SECONDS.sleep(20L);
        log.info("Data collection started");
        for (Guild guild : guilds) {
            MyGuild myGuild = new MyGuild();
            myGuild.setName(guild.getName());
            myGuild.setId(guild.getId());
            selfHttpReq.post("guild", myGuild);

            List<Member> members = guild.getMembers();

            for (Member member : members) {
                MyUser myUser = new MyUser();
                myUser.setUsername(member.getUser().getName());
                myUser.setId(member.getUser().getId());
                myUser.setAvatarUrl(member.getUser().getAvatarUrl());
                final MyUser foundUser = (MyUser) selfHttpReq.get("user/", myUser.getId());
                if (foundUser == null)
                    selfHttpReq.post("user", myUser);
                GuildMember guildMember = new GuildMember();
                GuildMemberId guildMemberId = new GuildMemberId();
                guildMemberId.setMyGuild(myGuild);
                guildMemberId.setMyUser(myUser);
                guildMember.setId(guildMemberId);
                List<Role> roles = member.getRoles();
                List<String> rolesNames = roles.stream().map(Role::getName).collect(Collectors.toList());
                guildMember.setRoles(rolesNames);
                final GuildMember foundGuildMember = (GuildMember) selfHttpReq.get("guildMember/", myUser.getId() + "/" + myGuild.getId());
                if (foundGuildMember == null)
                    selfHttpReq.post("guildMember", guildMember);
            }
        }
        log.info("Data collection ended");
    }
}
