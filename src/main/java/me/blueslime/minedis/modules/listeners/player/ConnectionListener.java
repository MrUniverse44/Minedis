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

    @EventHandler(priority = EventPriority.HIGHEST)
    public void on(PreLoginEvent event) {
        if (plugin.isRefreshing() && !event.isCancelled()) {
            event.setCancelled(true);
            event.setCancelReason(
                    TextUtilities.component(
                            plugin.getMessages().getString(
                                    "messages.kick.refresh",
                                    "&aThis network is refreshing some important stuff, please wait 1 second and try again"
                            )
                    )
            );
        }
    }
}
