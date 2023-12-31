package me.blueslime.minedis.modules;

import me.blueslime.minedis.modules.cache.Cache;
import me.blueslime.minedis.modules.discord.Bot;
import me.blueslime.minedis.modules.discord.Controller;
import me.blueslime.minedis.modules.extensions.Extensions;
import me.blueslime.minedis.utils.file.FileUtilities;
import me.blueslime.minedis.utils.text.TextUtilities;
import me.blueslime.minedis.Minedis;
import net.dv8tion.jda.api.JDA;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.api.scheduler.TaskScheduler;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.logging.Logger;

public abstract class DiscordModule {
    private final Minedis plugin;

    public DiscordModule(Minedis plugin) {
        this.plugin = plugin;
    }

    public void load() {

    }

    public void update() {

    }

    public void unload() {

    }

    public <T extends DiscordModule> T getModule(Class<T> module) {
        return plugin.getModule(module);
    }

    public <K, V> Cache<K, V> getCache(String id) {
        return plugin.getCache(id);
    }

    public void registerCache(CacheData... caches) {
        if (caches != null && caches.length >= 1) {
            for (CacheData data : caches) {
                plugin.getCacheMap().put(
                        data.getIdentifier(),
                        data.getCache()
                );
            }
        }
    }

    public static class CacheData {
        private final String identifier;
        private final Cache<?, ?> cache;

        private CacheData(String identifier, Cache<?, ?> cache) {
            this.identifier = identifier;
            this.cache = cache;
        }

        public static CacheData build(String identifier, Cache<?, ?> cache) {
            return new CacheData(identifier, cache);
        }

        public Cache<?, ?> getCache() {
            return cache;
        }

        public String getIdentifier() {
            return identifier;
        }
    }

    public ProxyServer getProxy() {
        return plugin.getProxy();
    }

    public Minedis getPlugin() {
        return plugin;
    }

    public TaskScheduler getScheduler() {
        return getProxy().getScheduler();
    }

    public PluginManager getPluginManager() {
        return getProxy().getPluginManager();
    }

    public File getDirectory() {
        return plugin.getDataFolder();
    }

    public InputStream getResource(String location) {
        return plugin.getResourceAsStream(location);
    }

    public File getDirectoryFile(String filename) {
        return new File(getDirectory(), filename);
    }

    public Configuration loadConfiguration(File file, String resource) {
        FileUtilities.saveResource(
                file,
                getResource(resource)
        );

        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(
                    file
            );
        } catch (IOException e) {
            return new Configuration();
        }
    }

    public Configuration loadConfiguration(File file, InputStream resource) {
        FileUtilities.saveResource(
                file,
                resource
        );

        try {
            return ConfigurationProvider.getProvider(YamlConfiguration.class).load(
                    file
            );
        } catch (IOException e) {
            return new Configuration();
        }
    }

    public Bot getBot() {
        return getModule(Controller.class).getBot();
    }

    public JDA getJDA() {
        return getBot().getClient();
    }

    public void saveConfiguration(Configuration configuration, String filename) {
        File file = new File(getDirectory(), filename);

        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    return;
                }

                ConfigurationProvider.getProvider(YamlConfiguration.class).save(
                        configuration,
                        file
                );
            } catch (IOException ignored) { }
            return;
        }
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(
                    configuration,
                    file
            );
        } catch (IOException ignored) { }
    }

    public boolean isExtensionInstalled(String id) {
        return getModule(Extensions.class).isExtensionInstalled(id);
    }

    public Logger getLogger() {
        return plugin.getLogger();
    }

    public Collection<? extends ProxiedPlayer> getPlayers() {
        return getProxy().getPlayers();
    }

    public String colorize(String message) {
        return TextUtilities.string(message);
    }
}
