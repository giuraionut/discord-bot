package com.discordbot.babybot.commands.command_logic;

import com.discordbot.babybot.commands.misc_commands.HelpCommand;
import com.discordbot.babybot.commands.misc_commands.PingCommand;
import com.discordbot.babybot.commands.misc_commands.TestCommand;
import com.discordbot.babybot.commands.music_commands.*;
import com.discordbot.babybot.commands.profile_commands.CardCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;


public class CommandsCollection {
    private final List<ICommand> commandList = new ArrayList<>();

    public CommandsCollection() {
        addCommand(new PingCommand());
        addCommand(new CardCommand());
        addCommand(new HelpCommand(this));
        addCommand(new PlayTrackCommand());
        addCommand(new PauseTrackCommand());
        addCommand(new ResumeTrackCommand());
        addCommand(new StopTrackCommand());
        addCommand(new SkipTrackCommand());
        addCommand(new CurrentTrackCommand());
        addCommand(new TestCommand());
    }

    private void addCommand(ICommand command) {
        if (this.commandList.contains(command)) {
            throw new IllegalArgumentException("Command already exists");
        }
        commandList.add(command);
    }

    @Nullable
    public ICommand getCommand(String input) {
        for (ICommand command : this.commandList) {
            if (command.getName().equals(input.toLowerCase()) | command.getAliases().contains(input.toLowerCase())) {
                return command;
            }
        }
        return null;
    }


    public List<ICommand> getCommandList() {
        return commandList;
    }

    public void handle(GuildMessageReceivedEvent event) {
        String PREFIX = "!";
        String[] array = event.getMessage()
                .getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(PREFIX), "")
                .split("\\s");
        String commandString = array[0].toLowerCase();
        ICommand iCommand = getCommand(commandString);

        if (iCommand != null) {
            event.getChannel().sendTyping().queue(); // pretend is typing;
            List<String> args = Arrays.asList(array).subList(1, array.length);
            Command command = new Command(event, args);
            iCommand.handle(command);
        }
    }
}