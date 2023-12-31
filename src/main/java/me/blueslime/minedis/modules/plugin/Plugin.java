package me.blueslime.minedis.modules.plugin;

import me.blueslime.minedis.Minedis;
import me.blueslime.minedis.modules.DiscordModule;
import me.blueslime.minedis.modules.cache.Cache;
import me.blueslime.minedis.modules.extensions.Extensions;
import me.blueslime.minedis.utils.file.FileUtilities;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class Plugin extends net.md_5.bungee.api.plugin.Plugin {
    private final Map<String, Cache<?, ?>> cacheMap = new HashMap<>();
    private final Map<Class<?>, DiscordModule> moduleMap = new HashMap<>();
    private Configuration settings;
    private Configuration messages;

    private boolean preventJoin;

    protected void initialize(Minedis plugin) {

        build();

        registerModules();

        loadModules();
    }

    public abstract void registerModules();

    private void loadModules() {
        for (DiscordModule module : moduleMap.values()) {
            if (module instanceof Extensions) {
                continue;
            }
            module.load();
        }
    }

    public void shutdown() {
        for (DiscordModule module : moduleMap.values()) {
            module.unload();
        }
    }

    public void build() {
        FileUtilities.saveResource(
                new File(getDataFolder(), "settings.yml"),
                getResourceAsStream("settings.yml")
        );
        settings = loadConfiguration(getDataFolder(), "settings.yml");

        FileUtilities.saveResource(
                new File(getDataFolder(), "messages.yml"),
                getResourceAsStream("messages.yml")
        );
        messages = loadConfiguration(getDataFolder(), "messages.yml");

    }

    public void reload() {
        build();

        for (DiscordModule module : moduleMap.values()) {
            module.update();
        }
    }

    private Configuration loadConfiguration(File folder, String child) {
        File file = new File(folder, child);
        if (file.exists()) {
            try {
                return ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            } catch (IOException e) {
                return new Configuration();
            }
        } else {
            try {
                if (file.createNewFile()) {
                    return ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
                }
            } catch (IOException ignored) { }
            return new Configuration();
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends DiscordModule> T getModule(Class<T> module) {
        return (T) moduleMap.get(module);
    }

    @SuppressWarnings("unchecked")
    public <K, V> Cache<K, V> getCache(String id) {
        return (Cache<K, V>) cacheMap.get(id);
    }

    public Plugin registerModule(DiscordModule... modules) {
        if (modules != null && modules.length >= 1) {
            for (DiscordModule module : modules) {
                moduleMap.put(module.getClass(), module);
            }
        }
        return this;
    }

    public void finish() {
        getLogger().info("Registered " + moduleMap.size() + " module(s).");
    }

    public Map<Class<?>, DiscordModule> getModules() {
        return moduleMap;
    }

    public Map<String, Cache<?, ?>> getCacheMap() {
        return cacheMap;
    }

    public boolean isRefreshing() {
        return preventJoin;
    }

    public void setPreventJoin(boolean preventJoin) {
        this.preventJoin = preventJoin;
    }

    public Configuration getSettings() {
        return settings;
    }

    public Configuration getMessages() {
        return messages;
    }

}