package com.pequla.bot.listener;

import com.pequla.bot.DiscordBot;
import com.pequla.bot.data.GuildData;
import com.pequla.bot.data.GuildDataService;
import com.pequla.bot.listener.command.GuildCommand;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class CommandListener extends DiscordListener {

    private final Set<GuildCommand> commands = new HashSet<>();

    @Autowired
    public CommandListener(DiscordBot bot, GuildDataService service) {
        super(bot, service);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        final User author = event.getAuthor();
        final Message message = event.getMessage();

        // Command cant be sent via system messages, bots or webhooks
        if (author.isBot() || author.isSystem() || message.isWebhookMessage()) {
            return;
        }

        String[] args = message.getContentRaw().trim().split("\\s+");
        commands.stream()
                .filter(command -> command.getTriggers().contains(args[0]))
                .findAny()
                .ifPresent(command -> {
                    Guild guild = event.getGuild();
                    GuildData data = service.retrieveGuildData(guild.getIdLong());
                    Role moderator = guild.getRoleById(data.getModeratorRoleId());
                    TextChannel channel = guild.getTextChannelById(data.getBotChannelId());
                    Member member = event.getMember();
                    // Member is moderator
                    if (moderator != null && member != null && member.getRoles().contains(moderator)) {
                        command.execute(event, Arrays.copyOfRange(args, 1, args.length));
                        return;
                    }

                    // Bot channel is not set
                    if (channel == null) {
                        command.execute(event, Arrays.copyOfRange(args, 1, args.length));
                        return;
                    }

                    // Bot channel is set and the command is executed in that channel
                    if (event.getChannel().getIdLong() == channel.getIdLong()) {
                        command.execute(event, Arrays.copyOfRange(args, 1, args.length));
                    }
                });
    }

    public synchronized void registerCommand(@NotNull GuildCommand command) {
        logger.info("Registering command " + command.getClass().getName());
        commands.add(command);
    }

    public synchronized Set<GuildCommand> getCommands() {
        return commands;
    }
}
