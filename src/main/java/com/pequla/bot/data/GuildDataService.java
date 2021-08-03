package com.pequla.bot.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GuildDataService {

    private final GuildDataRepository repository;

    @Autowired
    public GuildDataService(GuildDataRepository repository) {
        this.repository = repository;
    }

    public void updateBotChannelId(long guildId, long channelId) {
        GuildData data = retrieveGuildData(guildId);
        data.setBotChannelId(channelId);
        repository.save(data);
    }

    public void updateJoinLeaveChannelId(long guildId, long channelId) {
        GuildData data = retrieveGuildData(guildId);
        data.setJoinLeaveChannelId(channelId);
        repository.save(data);
    }

    public void updateModeratorRoleId(long guildId, long roleId) {
        GuildData data = retrieveGuildData(guildId);
        data.setModeratorRoleId(roleId);
        repository.save(data);
    }

    public GuildData retrieveGuildData(long guildId) {
        Optional<GuildData> optional = repository.getGuildDataByGuildId(guildId);
        if (optional.isEmpty()) {
            GuildData data = new GuildData();
            data.setGuildId(guildId);
            return repository.save(data);
        }
        return optional.get();
    }

    public void deleteGuildData(long guildId) {
        Optional<GuildData> optional = repository.getGuildDataByGuildId(guildId);
        optional.ifPresent(repository::delete);
    }
}
