package com.pequla.bot.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuildDataRepository extends CrudRepository<GuildData, Long> {
    Optional<GuildData> getGuildDataByGuildId(long guildId);
}
