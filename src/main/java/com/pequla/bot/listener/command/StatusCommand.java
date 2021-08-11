package com.pequla.bot.listener.command;

import com.pequla.bot.AppUtils;
import com.pequla.bot.listener.CommandListener;
import com.pequla.server.ping.Player;
import com.pequla.server.ping.Players;
import com.pequla.server.ping.ServerPing;
import com.pequla.server.ping.StatusResponse;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xbill.DNS.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class StatusCommand extends GuildCommand {

    @Autowired
    public StatusCommand(CommandListener listener) {
        super(listener);
    }

    @Override
    public void execute(@NotNull GuildMessageReceivedEvent event, String @NotNull [] args) {
        TextChannel channel = event.getChannel();
        if (args.length == 1) {
            String query = "_minecraft._tcp." + args[0];
            try {
                Lookup lookup = new Lookup(query, Type.SRV);
                Record[] records = lookup.run();
                if (records == null) {
                    sendServerStatus(channel, args[0], 25565);
                    return;
                }
                for (Record record : records) {
                    SRVRecord srv = (SRVRecord) record;
                    String hostname = srv.getTarget().toString().replaceFirst("\\.$", "");
                    sendServerStatus(channel, hostname, srv.getPort());
                    return;
                }
            } catch (TextParseException e) {
                channel.sendMessage("Hostname text is in invalid format").queue();
                e.printStackTrace();
            }
            return;
        }
        // server address and port
        if (args.length == 2) {
            try {
                int port = Integer.parseInt(args[1]);
                if (port >= 0 && port <= 65535) {
                    sendServerStatus(channel, args[0], port);
                } else {
                    channel.sendMessage("TCP ports can only be in range of `0-65535`").queue();
                }
            } catch (NumberFormatException e) {
                channel.sendMessage("`" + args[1] + "` is not a number").queue();
                e.printStackTrace();
            }
            return;
        }
        channel.sendMessage("Usage: `.status <server-address>`").queue();
    }

    private void sendServerStatus(TextChannel channel, String hostname, int port) {
        try {
            InetAddress address = InetAddress.getByName(hostname);
            ServerPing ping = new ServerPing(new InetSocketAddress(address, port));
            StatusResponse response = ping.fetchData();
            Players players = response.getPlayers();
            channel.sendMessageEmbeds(AppUtils.createEmbed("Minecraft server status")
                    .setThumbnail("https://api.mcsrvstat.us/icon/" + hostname)
                    .addField("Version:", response.getVersion().getName(), true)
                    .addField("Online:", players.getOnline() + "/" + players.getMax(), true)
                    .addField("Players:", playerListFormat(players.getSample()), false)
                    .build()).queue();
        } catch (IOException e) {
            channel.sendMessage("Server " + hostname + " is **offline**").queue();
            logger.error(e.getMessage(), e);
        }
    }

    private String playerListFormat(List<Player> players) {
        if (players == null || players.isEmpty()) {
            return "Server is empty";
        }
        return MarkdownSanitizer.sanitize(players.stream().map(Player::getName).collect(Collectors.toList()).toString());
    }

    @Override
    public String getDescription() {
        return "Minecraft server status";
    }

    @Override
    public List<String> getTriggers() {
        return Arrays.asList(".status", ".mcstatus");
    }
}
