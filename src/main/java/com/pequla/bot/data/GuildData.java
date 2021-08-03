package com.pequla.bot.data;

import javax.persistence.*;

@Entity
@Table(name = "guild_data")
public class GuildData {

    @Id
    @Column(name = "guild_data_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "guild_id", nullable = false, unique = true)
    private long guildId;

    @Column(name = "bot_channel_id", unique = true)
    private long botChannelId;

    @Column(name = "moderator_role_id", unique = true)
    private long moderatorRoleId;

    @Column(name = "join_leave_channel_id", unique = true)
    private long joinLeaveChannelId;

    public GuildData() {
    }

    public GuildData(long id,
                     long guildId,
                     long botChannelId,
                     long moderatorRoleId,
                     long joinLeaveChannelId) {
        this.id = id;
        this.guildId = guildId;
        this.botChannelId = botChannelId;
        this.moderatorRoleId = moderatorRoleId;
        this.joinLeaveChannelId = joinLeaveChannelId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public long getBotChannelId() {
        return botChannelId;
    }

    public void setBotChannelId(long botChannelId) {
        this.botChannelId = botChannelId;
    }

    public long getModeratorRoleId() {
        return moderatorRoleId;
    }

    public void setModeratorRoleId(long moderatorRoleId) {
        this.moderatorRoleId = moderatorRoleId;
    }

    public long getJoinLeaveChannelId() {
        return joinLeaveChannelId;
    }

    public void setJoinLeaveChannelId(long joinLeaveChannelId) {
        this.joinLeaveChannelId = joinLeaveChannelId;
    }
}
