package com.pequla.bot;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.MarkdownUtil;

import java.time.Instant;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppUtils {
    public static EmbedBuilder createEmbed(String title) {
        return new EmbedBuilder().setColor(AppConstants.EMBED_COLOR)
                .setTitle(MarkdownUtil.bold(title))
                .setTimestamp(Instant.now());
    }

    public static String generateYoutubeThumbnail(String url) {
        String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\u200C\u200B2F|youtu.be%2F|%2Fv%2F)[^#\\&\\?\\n]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(url);
        if (matcher.find()) {
            return AppConstants.YOUTUBE_IMG_URL.replace("%id%", matcher.group());
        }
        return null;
    }

    public static void sendTrackInfoEmbed(TextChannel channel, String title, AudioTrack track) {
        AudioTrackInfo info = track.getInfo();
        channel.sendMessageEmbeds(createEmbed(title)
                .setThumbnail(generateYoutubeThumbnail(info.uri))
                .addField("Name:", info.title, false)
                .addField("Author:", info.author, false)
                .build()).queue();
    }
}
