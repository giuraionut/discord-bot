package com.discordbot.babybot.commands.command_logic;

import com.discordbot.babybot.commands.misc_commands.HelpPrivateCommand;
import com.discordbot.babybot.commands.reddit_commands.CreateProfile;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class PrivateCommandsCollection {
    private final List<ICommand> commandList = new ArrayList<>();

    public PrivateCommandsCollection() {
        //general
        addCommand(new HelpPrivateCommand(this));
        //reddit
        addCommand(new CreateProfile());
    }

    private void addCommand(ICommand command) {
        if (this.commandList.contains(command)) {
            throw new IllegalArgumentException("PrivateCommand already exists");
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

    public void handle(PrivateMessageReceivedEvent event) {
        String PREFIX = "?";
        String[] array = event.getMessage()
                .getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(PREFIX), "")
                .split("\\s");
        String commandString = array[0].toLowerCase();
        ICommand iCommand = getCommand(commandString);

        if (iCommand != null) {
            event.getChannel().sendTyping().queue(); // pretend is typing;
            List<String> args = Arrays.asList(array).subList(1, array.length);
            PrivateCommand privateCommand = new PrivateCommand(event, args);
            iCommand.handle(privateCommand);
        }
    }
}
