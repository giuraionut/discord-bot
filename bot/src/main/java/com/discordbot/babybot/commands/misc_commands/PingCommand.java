package com.discordbot.babybot.commands.misc_commands;

import com.discordbot.babybot.commands.command_logic.GuildCommand;
import com.discordbot.babybot.commands.command_logic.ICommand;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand {
    @Override
    public void handle(GuildCommand guildCommand) {
        JDA jda = guildCommand.getJDA();
        jda.getRestPing().queue(
                (ping) -> guildCommand.getGuildChannel()
                        .sendMessageFormat("Your *``REST``* ping: %sms\n" +
                                        "Your *``GATEWAY``* ping: %sms",
                                ping, jda.getGatewayPing()).queue()
        );
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getCategory() {
        return "general";
    }

    @Override
    public String getHelp() {
        return "```\n" +
                "This command returns your ping\n" +
                "Use it as !ping" +
                "\n```";
    }
}
