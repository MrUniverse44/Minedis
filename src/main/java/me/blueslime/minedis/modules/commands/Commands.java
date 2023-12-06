package me.blueslime.minedis.modules.commands;

import me.blueslime.minedis.Minedis;
import me.blueslime.minedis.api.command.MinecraftCommand;
import me.blueslime.minedis.api.extension.MinedisExtension;
import me.blueslime.minedis.modules.DiscordModule;
import me.blueslime.minedis.modules.commands.list.MainCommand;

import java.util.*;

public class Commands extends DiscordModule {
    private final Map<Class<? extends MinedisExtension>, List<MinecraftCommand>> commandMap = new HashMap<>();
    public Commands(Minedis plugin) {
        super(plugin);
    }

    @Override
    public void load() {
        getPluginManager().registerCommand(
                getPlugin(),
                new MainCommand(
                        getPlugin(),
                        "minedis"
                )
        );
    }

    public void add(MinedisExtension extension, MinecraftCommand command) {
        List<MinecraftCommand> commandList = commandMap.computeIfAbsent(
                extension.getClass(), l -> new ArrayList<>()
        );

        commandList.add(command);
    }

    public void unload(MinedisExtension extension) {
        if (commandMap.containsKey(extension.getClass())) {
            List<MinecraftCommand> list = commandMap.get(extension.getClass());

            list.forEach(command -> getPluginManager().unregisterCommand(command));
        }
    }

    public List<MinecraftCommand> getCommands(MinedisExtension extension) {
        if (commandMap.containsKey(extension.getClass())) {{
            return Collections.unmodifiableList(
                    commandMap.get(extension.getClass())
            );
        }}
        return Collections.emptyList();
    }
}
