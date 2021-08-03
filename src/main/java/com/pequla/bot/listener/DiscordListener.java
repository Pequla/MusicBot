package com.pequla.bot.listener;

import com.pequla.bot.DiscordBot;
import com.pequla.bot.data.GuildDataService;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DiscordListener extends ListenerAdapter {
    protected DiscordBot bot;
    protected GuildDataService service;
    protected Logger logger;

    public DiscordListener(DiscordBot bot, GuildDataService service) {
        bot.registerListener(this);
        this.bot = bot;
        this.service = service;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }
}
