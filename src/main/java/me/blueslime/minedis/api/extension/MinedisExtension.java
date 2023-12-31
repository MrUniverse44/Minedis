package me.blueslime.minedis.api.extension;

import me.blueslime.minedis.Minedis;
import me.blueslime.minedis.api.MinedisAPI;
import me.blueslime.minedis.api.command.MinecraftCommand;
import me.blueslime.minedis.modules.DiscordModule;
import me.blueslime.minedis.modules.cache.Cache;
import me.blueslime.minedis.modules.commands.Commands;
import me.blueslime.minedis.modules.discord.Bot;
import me.blueslime.minedis.modules.discord.Controller;
import me.blueslime.minedis.modules.listeners.Listeners;
import net.dv8tion.jda.api.JDA;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("unused")
public abstract class MinedisExtension {
    private Configuration configuration;

    /**
     * Please select a different identifier per extension
     * @return custom identifier
     */
    public abstract String getIdentifier();

    /**
     * This will be the displayed name in commands for this extension
     * @return custom name
     */
    public abstract String getName();

    public abstract void onEnabled();

    public abstract void onDisable();

    /**
     * This method will be called by the plugin
     * Here the plugin will try to load the plugin.
     * Don't touch this method
     * Only modify it if you know what are you doing.
     * @throws MinedisExtensionIdentifierException when this extension doesn't have an identifier
     */
    public boolean register() throws MinedisExtensionIdentifierException {
        if (getIdentifier() == null) {
            throw new MinedisExtensionIdentifierException("Identifier is null.");
        }

        configuration = generate();
        return true;
    }

    private Configuration generate() {
        MinedisAPI api = MinedisAPI.get();
        if (api == null) {
            return new Configuration();
        }
        File extensions = api.getDirectoryFile("extensions");
        File folder = new File(extensions, "configurations");

        boolean load = folder.exists() || folder.mkdirs();
        if (load) {
            File file = new File(
                    folder,
                    getIdentifier() + ".yml"
            );
            return api.loadConfiguration(
                    file,
                    getConfigurationInputStream()
            );
        }
        return new Configuration();
    }

    public InputStream getConfigurationInputStream() {
        return MinedisAPI.get().getResource("default-extension.yml");
    }

    public void registerEventListeners(Object... listeners) {
        getPlugin().getModule(Listeners.class).registerDiscord(this, listeners);
    }

    public void registerMinecraftListeners(Listener... listeners) {
        getPlugin().getModule(Listeners.class).registerMinecraft(this, listeners);
    }

    public void unregisterListeners() {
        getPlugin().getModule(Listeners.class).unregister(this);
    }

    public List<MinecraftCommand> getRegisteredCommands() {
        return getPlugin().getModule(Commands.class).getCommands(this);
    }

    public Bot getBot() {
        return getPlugin().getModule(Controller.class).getBot();
    }

    /**
     * Register a new cache for all extensions and for the main plugin
     * @param cache to register
     */
    public void registerCache(Cache<?, ?> cache) {
        getPlugin().getCacheMap().put(
                cache.getClass(),
                cache
        );
    }

    public JDA getJDA() {
        return getPlugin().getModule(Controller.class).getJDA();
    }

    public List<Object> getRegisteredDiscordListeners() {
        return getPlugin().getModule(Listeners.class).getDiscord(this);
    }

    public List<Listener> getRegisteredMinecraftListeners() {
        return getPlugin().getModule(Listeners.class).getMinecraft(this);
    }

    public MinedisExtension registerMinecraftCommand(MinecraftCommand command) {
        MinedisAPI api = MinedisAPI.get();
        if (api == null) {
            return this;
        }
        api.getPluginManager().registerCommand(
                api.getPlugin(),
                command
        );
        api.getPlugin().getModule(Commands.class).add(this, command);
        return this;
    }

    public Logger getLogger() {
        return getPlugin().getLogger();
    }

    public ProxyServer getProxy() {
        return getPlugin().getProxy();
    }

    public boolean isEnabled() {
        boolean exists = configuration.contains("settings.enabled");
        if (!exists) {
            MinedisAPI api = MinedisAPI.get();
            if (api != null) {
                api.getLogger().info("Extension: " + getIdentifier() + " doesn't have a 'settings.enabled' path created yet!");
            }
        }
        return configuration.getBoolean("settings.enabled", false);
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void reloadConfiguration() {
        configuration = generate();
    }

    public <T extends DiscordModule> T getModule(Class<T> module) {
        MinedisAPI api = MinedisAPI.get();
        if (api == null) {
            return null;
        }
        return api.getModule(module);
    }

    public <K, V> Cache<K, V> getCache(String id) {
        MinedisAPI api = MinedisAPI.get();
        if (api == null) {
            return null;
        }
        return api.getCache(id);
    }

    public void saveConfiguration() {
        MinedisAPI api = MinedisAPI.get();
        if (api == null) {
            return;
        }
        File extensions = api.getDirectoryFile("extensions");
        File folder = new File(extensions, "configurations");

        boolean load = folder.exists() || folder.mkdirs();
        if (load) {
            File file = new File(
                    folder,
                    getIdentifier() + ".yml"
            );
            try {
                ConfigurationProvider.getProvider(
                        YamlConfiguration.class
                ).save(configuration, file);
            } catch (IOException ignored) {}
        }
    }

    public Minedis getPlugin() {
        return MinedisAPI.get().getPlugin();
    }
}
