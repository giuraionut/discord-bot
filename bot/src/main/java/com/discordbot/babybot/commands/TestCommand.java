package com.discordbot.babybot.commands;

import com.discordbot.babybot.command_logic.Command;
import com.discordbot.babybot.command_logic.ICommand;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.List;
import java.util.stream.Collectors;

public class TestCommand implements ICommand {


    @Override
    public void handle(Command command) {
        Guild guild = command.getGuild();
        String name = guild.getName();
        long idLong = guild.getIdLong();
        List<Member> members = guild.getMembers();
        int memberCount = guild.getMemberCount();
        Member owner = guild.getOwner();


        System.out.println("Guild name: " + name);
        System.out.println("Guild ID: " + idLong);
        System.out.println("Number of members in Guild: " + memberCount);
        System.out.println("Members:");
        System.out.println("Owner: " +
                "Nickname: " + owner.getNickname());

        for (Member member : members) {
            if (member != owner) {
                System.out.println(member.getNickname() + "\n" +
                        " roles: " + member.getRoles() + "\n" +
                        " colors: " + member.getRoles().stream().map(Role::getColor).collect(Collectors.toList()));
            }
        }

    }

    @Override
    public String getName() {
        return "test";
    }

    @Override
    public String getHelp() {
        return null;
    }
}
