package me.blueslime.minedis.modules.listeners;

import me.blueslime.minedis.Minedis;
import me.blueslime.minedis.api.extension.MinedisExtension;
import me.blueslime.minedis.modules.DiscordModule;
import net.md_5.bungee.api.plugin.Listener;

import java.util.*;

public class Listeners extends DiscordModule {
    private final Map<Class<? extends MinedisExtension>, List<Listener>> minecratMap = new HashMap<>();
    private final Map<Class<? extends MinedisExtension>, List<Object>> discordMap = new HashMap<>();
    public Listeners(Minedis plugin) {
        super(plugin);
    }

    public void registerDiscord(MinedisExtension extension, Object... objects) {
        if (objects == null || objects.length == 0) {
            return;
        }

        List<Object> listenerList = discordMap.computeIfAbsent(
                extension.getClass(),
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
                extension.getClass(),
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
        if (discordMap.containsKey(extension.getClass())) {
            for (Object object : discordMap.get(extension.getClass())) {
                getJDA().removeEventListener(object);
            }
            discordMap.clear();
        }
        if (minecratMap.containsKey(extension.getClass())) {
            for (Listener listener : minecratMap.get(extension.getClass())) {
                getPluginManager().unregisterListener(listener);
            }
            minecratMap.clear();
        }
    }

    public List<Object> getDiscord(MinedisExtension extension) {
        if (discordMap.containsKey(extension.getClass())) {{
            return Collections.unmodifiableList(
                    discordMap.get(extension.getClass())
            );
        }}
        return Collections.emptyList();
    }

    public List<Listener> getMinecraft(MinedisExtension extension) {
        if (minecratMap.containsKey(extension.getClass())) {{
            return Collections.unmodifiableList(
                    minecratMap.get(extension.getClass())
            );
        }}
        return Collections.emptyList();
    }
}
