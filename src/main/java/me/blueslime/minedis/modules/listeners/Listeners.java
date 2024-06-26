package me.blueslime.minedis.modules.listeners;

import me.blueslime.minedis.Minedis;
import me.blueslime.minedis.api.extension.MinedisExtension;
import me.blueslime.minedis.modules.DiscordModule;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.*;

public class Listeners extends DiscordModule {
    private final Map<String, List<Listener>> minecratMap = new HashMap<>();
    private final Map<String, List<Object>> discordMap = new HashMap<>();

    public Listeners(Minedis plugin) {
        super(plugin);
    }

    public void registerDiscord(MinedisExtension extension, Object... objects) {
        if (objects == null || objects.length == 0) {
            return;
        }

        List<Object> listenerList = discordMap.computeIfAbsent(
            extension.getClass().getName(),
            i -> new ArrayList<>()
        );

        listenerList.addAll(Arrays.asList(objects));

        getJDA().addEventListener(
            objects
        );
    }

    public void registerMinecraft(MinedisExtension extension, Listener... listeners) {
        if (listeners == null || listeners.length == 0) {
            return;
        }
        List<Listener> listenerList = minecratMap.computeIfAbsent(
            extension.getClass().getName(),
            i -> new ArrayList<>()
        );

        listenerList.addAll(Arrays.asList(listeners));

        for (Listener listener : listeners) {
            getPluginManager().registerListener(
                getPlugin(),
                listener
            );
        }
    }

    public void unregister(MinedisExtension extension) {
        getPlugin().getLogger().info("Unregistering extension discord listeners from extension: " + extension.getIdentifier());
        if (discordMap.containsKey(extension.getClass().getName())) {
            for (Object object : discordMap.get(extension.getClass().getName())) {
                getJDA().removeEventListener(object);
            }
            discordMap.clear();
        }
        getPlugin().getLogger().info("Unregistering extension minecraft listeners from extension: " + extension.getIdentifier());
        if (minecratMap.containsKey(extension.getClass().getName())) {
            for (Listener listener : minecratMap.get(extension.getClass().getName())) {
                getPluginManager().unregisterListener(listener);
            }
            minecratMap.clear();
        }
    }

    public List<Object> getDiscord(MinedisExtension extension) {
        if (discordMap.containsKey(extension.getClass().getName())) {{
            return Collections.unmodifiableList(
                discordMap.get(extension.getClass().getName())
            );
        }}
        return Collections.emptyList();
    }

    public List<Listener> getMinecraft(MinedisExtension extension) {
        if (minecratMap.containsKey(extension.getClass().getName())) {{
            return Collections.unmodifiableList(
                minecratMap.get(extension.getClass().getName())
            );
        }}
        return Collections.emptyList();
    }

    public void unregisterAll() {
        List<Listener> listenerList = new ArrayList<>();

        minecratMap.values().forEach(listenerList::addAll);

        PluginManager manager = getPluginManager();

        if (!listenerList.isEmpty()) {
            listenerList.forEach(
                    manager::unregisterListener
            );
        }

        discordMap.clear();
        minecratMap.clear();
    }
}
