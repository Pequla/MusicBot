package com.pequla.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class DiscordBot implements DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(DiscordBot.class);
    private final JDA jda;

    public DiscordBot() throws LoginException, IOException {
        logger.info("Reading the token");
        String token = Files.readString(Paths.get("token.txt"), StandardCharsets.UTF_8)
                .replaceAll("\n", "");
        logger.info("Connecting to Discord API");
        this.jda = JDABuilder.createDefault(token)
                .setActivity(Activity.listening(".help"))
                .build();
    }

    @Override
    public void destroy() {
        logger.info("Disconnecting from Discord API");
        jda.shutdownNow();
    }

    public void registerListener(@NotNull ListenerAdapter listener) {
        logger.info("Registering listener " + listener.getClass().getName());
        jda.addEventListener(listener);
    }
}
