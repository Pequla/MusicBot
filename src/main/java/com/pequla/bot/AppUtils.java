package com.pequla.bot;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.MarkdownUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {
    public static @NotNull EmbedBuilder createEmbed(String title) {
        return new EmbedBuilder().setColor(AppConstants.EMBED_COLOR)
                .setTitle(MarkdownUtil.bold(title))
                .setTimestamp(Instant.now());
    }

    public static @Nullable String generateYoutubeThumbnail(String url) {
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) {
            return AppConstants.YOUTUBE_IMG_URL.replace("%id%", matcher.group());
        }
        return null;
    }

    public static void sendTrackInfoEmbed(@NotNull TextChannel channel, String title, @NotNull AudioTrack track) {
        AudioTrackInfo info = track.getInfo();
        channel.sendMessageEmbeds(createEmbed(title)
                .setThumbnail(generateYoutubeThumbnail(info.uri))
                .addField("Name:", info.title, false)
                .addField("Author:", info.author, false)
                .build()).queue();
    }

    public static JsonElement getJsonElement(String link) throws IOException {
        URL url = new URL(link);
        URLConnection request = url.openConnection();
        request.connect();
        return JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));

    }
}
