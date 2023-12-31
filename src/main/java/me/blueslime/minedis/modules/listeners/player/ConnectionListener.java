package me.blueslime.minedis.modules.listeners.player;

import me.blueslime.minedis.Minedis;
import me.blueslime.minedis.utils.text.TextUtilities;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ConnectionListener implements Listener {
    private final Minedis plugin;

    public ConnectionListener(Minedis plugin) {
        this.plugin = plugin;
    }

}
