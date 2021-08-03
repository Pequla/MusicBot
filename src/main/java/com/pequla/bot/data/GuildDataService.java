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

    public Optional<GuildData> getByGuildId(long guildId) {
        return repository.getGuildDataByGuildId(guildId);
    }

    public GuildData createNew(long guildId) {
        GuildData data = new GuildData();
        data.setGuildId(guildId);
        return repository.save(data);
    }

    public void updateBotChannelId(long guildId, long channelId) {
        GuildData data = generateGuildData(guildId);
        data.setBotChannelId(channelId);
        repository.save(data);
    }

    public void updateJoinLeaveChannelId(long guildId, long channelId) {
        GuildData data = generateGuildData(guildId);
        data.setJoinLeaveChannelId(channelId);
        repository.save(data);
    }

    public void updateModeratorRoleId(long guildId, long roleId) {
        GuildData data = generateGuildData(guildId);
        data.setModeratorRoleId(roleId);
        repository.save(data);
    }

    public GuildData generateGuildData(long id) {
        Optional<GuildData> optional = getByGuildId(id);
        return optional.orElseGet(() -> createNew(id));
    }
}
