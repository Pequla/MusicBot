package com.pequla.bot.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "guild_data")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
}
