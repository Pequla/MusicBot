package com.pequla.bot.listener.command.music;

import com.pequla.bot.listener.CommandListener;
import com.pequla.bot.listener.command.GuildCommand;
import com.pequla.bot.listener.command.music.utils.PlayerManager;

public abstract class MusicCommand extends GuildCommand {

    protected final PlayerManager manager;

    public MusicCommand(CommandListener listener, PlayerManager manager) {
        super(listener);
        this.manager = manager;
    }

}
