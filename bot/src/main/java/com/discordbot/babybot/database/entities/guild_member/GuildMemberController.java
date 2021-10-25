package com.discordbot.babybot.database.entities.guild_member;

import com.discordbot.babybot.database.entities.my_guild.MyGuildService;
import com.discordbot.babybot.database.entities.my_user.MyUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "guildMember")
@AllArgsConstructor
public class GuildMemberController {

    @Autowired
    private GuildMemberService guildMemberService;
    @Autowired
    private MyGuildService myGuildService;
    @Autowired
    private MyUserService myUserService;

    @PostMapping
    public void addGuildMember(@RequestBody GuildMember guildMember) {
        this.guildMemberService.save(guildMember);
    }

    @GetMapping("{userId}/{guildId}")
    public ResponseEntity<GuildMember> getGuildMember(@PathVariable("userId") String userId, @PathVariable("guildId") String guildId) {
        GuildMemberId guildMemberId = new GuildMemberId();

        guildMemberId.setMyUser(myUserService.get(userId));
        guildMemberId.setMyGuild(myGuildService.get(guildId));

        GuildMember guildMember = this.guildMemberService.get(guildMemberId);

        return new ResponseEntity<>(guildMember, HttpStatus.OK);
    }
}
