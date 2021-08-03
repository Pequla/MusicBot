package com.pequla.bot.listener.command.joke;

import com.google.gson.Gson;
import com.pequla.bot.AppConstants;
import com.pequla.bot.AppUtils;
import com.pequla.bot.listener.CommandListener;
import com.pequla.bot.listener.command.GuildCommand;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.List;

@Component
public class JokeCommand extends GuildCommand {

    @Autowired
    public JokeCommand(CommandListener listener) {
        super(listener);
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] args) {
        TextChannel channel = event.getChannel();
        try {
            URLConnection request = new URL(AppConstants.JOKE_API_URL).openConnection();
            request.connect();
            Gson gson = new Gson();
            Joke joke = gson.fromJson(new InputStreamReader((InputStream) request.getContent()), Joke.class);
            channel.sendMessageEmbeds(AppUtils.createEmbed("Joke")
                    .setDescription(String.format("%s%n  - %s", joke.getSetup(), joke.getPunchline()))
                    .build()).queue();
        } catch (IOException e) {
            channel.sendMessage("Could not get a fresh joke").queue();
            logger.error(e.getMessage(), e);

        }
    }

    @Override
    public String getDescription() {
        return "Sends a random joke";
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList(".joke");
    }
}
