package com.pequla.bot.listener;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pequla.bot.AppUtils;
import com.pequla.bot.DiscordBot;
import com.pequla.bot.data.GuildDataService;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EmbedListener extends DiscordListener {

    @Autowired
    public EmbedListener(DiscordBot bot, GuildDataService service) {
        super(bot, service);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        TextChannel channel = event.getChannel();
        String raw = event.getMessage().getContentRaw();
        List<String> urls = extractUrls(raw);
        if (urls.isEmpty()) {
            return;
        }
        try {
            URL url = new URL("https://api.instagram.com/oembed/?url=" + urls.get(0));
            URLConnection request = url.openConnection();
            request.connect();
            JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
            JsonObject obj = root.getAsJsonObject();
            String title = obj.get("title").getAsString().replace("\n", "");
            if (title.length() > 150) {
                title = title.substring(0, 150) + "...";
            }
            String author = obj.get("author_name").getAsString();
            String authorUrl = obj.get("author_url").getAsString();
            String image = obj.get("thumbnail_url").getAsString();
            channel.sendMessageEmbeds(AppUtils.createEmbed(title)
                    .setAuthor(author, authorUrl, null)
                    .setImage(image)
                    .build()).queue();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            channel.sendMessage("Could not embed that post").queue();
        }
    }


    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<>();
        String urlRegex = "((https?:\\/\\/(?:www\\.)?instagram\\.com\\/p\\/([^/?#&]+)).)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }

}
