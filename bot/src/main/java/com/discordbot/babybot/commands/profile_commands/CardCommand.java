package com.discordbot.babybot.commands.profile_commands;

import com.discordbot.babybot.commands.command_logic.Command;
import com.discordbot.babybot.commands.command_logic.ICommand;
import com.discordbot.babybot.profile_card.ProfileCard;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CardCommand implements ICommand {
    @Override
    public void handle(Command command) {
        ProfileCard profileCard = new ProfileCard();
        User author = command.getAuthor();
        Guild guild = command.getGuild();

        TextChannel channel = command.getChannel();
        int width;
        try {
            String arg = command.getArgs().get(0);
            width = Integer.parseInt(arg);
        }
        catch (IndexOutOfBoundsException ex)
        {
            width = 500;
        }

        File profileCardImage = profileCard.draw(width, author, guild);
        if (profileCardImage != null) {
            channel.sendFile(profileCardImage).queue();
        } else {
            channel.sendMessage("Sorry, I can't generate you a card right now because I messed up something :(").queue();
        }
    }

    @Override
    public String getName() {
        return "card";
    }

    @Override
    public String getHelp() {
        return "```\n" +
                "This command generates a card with your profile\n" +
                "Use it as !card" +
                "\n```";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("mycard");
        aliases.add("profilecard");
        return aliases;
    }
}
