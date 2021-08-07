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

    @Column(name = "bot_channel_id")
    private long botChannelId = 0;

    @Column(name = "moderator_role_id")
    private long moderatorRoleId = 0;

    @Column(name = "join_leave_channel_id")
    private long joinLeaveChannelId = 0;

    @Column(name = "dj_role_id")
    private long djRoleId = 0;

    public GuildData() {
    }

    public GuildData(long id,
                     long guildId,
                     long botChannelId,
                     long moderatorRoleId,
                     long joinLeaveChannelId,
                     long djRoleId) {
        this.id = id;
        this.guildId = guildId;
        this.botChannelId = botChannelId;
        this.moderatorRoleId = moderatorRoleId;
        this.joinLeaveChannelId = joinLeaveChannelId;
        this.djRoleId = djRoleId;
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

    public long getDjRoleId() {
        return djRoleId;
    }

    public void setDjRoleId(long djRoleId) {
        this.djRoleId = djRoleId;
    }
}
