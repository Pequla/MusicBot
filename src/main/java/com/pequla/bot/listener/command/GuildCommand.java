package com.pequla.bot.listener.command;

import com.pequla.bot.listener.CommandListener;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class GuildCommand {
    protected final CommandListener listener;
    protected final Logger logger;

    public GuildCommand(@NotNull CommandListener listener) {
        listener.registerCommand(this);
        this.listener = listener;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public abstract void execute(GuildMessageReceivedEvent event, String[] args);

    public abstract String getDescription();

    public abstract List<String> getTriggers();
}
