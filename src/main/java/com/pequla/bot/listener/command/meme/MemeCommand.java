package com.pequla.bot.listener.command.meme;

import com.google.gson.Gson;
import com.pequla.bot.AppConstants;
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
public class MemeCommand extends GuildCommand {

    @Autowired
    public MemeCommand(CommandListener listener) {
        super(listener);
    }

    @Override
    public void execute(GuildMessageReceivedEvent event, String[] args) {
        TextChannel channel = event.getChannel();
        try {
            URLConnection request = new URL(AppConstants.MEME_API_URL).openConnection();
            request.connect();
            Gson gson = new Gson();
            Meme meme = gson.fromJson(new InputStreamReader((InputStream) request.getContent()), Meme.class);
            if (!channel.isNSFW() && meme.isNsfw()) {
                channel.sendMessage("Sorry, this meme is nsfw but the channel isn't").queue();
                return;
            }
            channel.sendMessage(meme.getUrl()).queue();
        } catch (IOException e) {
            channel.sendMessage("Could not get a fresh meme").queue();
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public String getDescription() {
        return "Sends a meme from reddit";
    }

    @Override
    public List<String> getTriggers() {
        return Collections.singletonList(".meme");
    }
}
