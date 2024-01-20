package me.blueslime.minedis.modules.commands;

import me.blueslime.minedis.Minedis;
import me.blueslime.minedis.api.command.DiscordCommand;
import me.blueslime.minedis.api.command.MinecraftCommand;
import me.blueslime.minedis.api.extension.MinedisExtension;
import me.blueslime.minedis.modules.DiscordModule;
import me.blueslime.minedis.modules.commands.list.MainCommand;
import net.dv8tion.jda.api.entities.Guild;

import java.util.*;

public class Commands extends DiscordModule {
    private final Map<String, List<DiscordCommand>> discordCommandMap = new HashMap<>();
    private final Map<String, List<MinecraftCommand>> commandMap = new HashMap<>();
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
            extension.getClass().getName(), l -> new ArrayList<>()
        );

        commandList.add(command);
    }

    public void add(MinedisExtension extension, DiscordCommand command) {
        List<DiscordCommand> commandList = discordCommandMap.computeIfAbsent(
            extension.getClass().getName(), l -> new ArrayList<>()
        );

        commandList.add(command);
    }

    public void unload(MinedisExtension extension) {
        String id = extension.getClass().getName();

        if (commandMap.containsKey(id)) {
            List<MinecraftCommand> list = commandMap.getOrDefault(id, Collections.emptyList());

            list.forEach(command -> getPluginManager().unregisterCommand(command));
        }
        if (discordCommandMap.containsKey(id)) {
            List<DiscordCommand> list = discordCommandMap.getOrDefault(id, Collections.emptyList());

            list.forEach(
                    command -> {
                        if (command.getGuild() == null) {
                            return;
                        }
                        Guild guild = getJDA().getGuildById(command.getGuild());

                        if (guild != null) {
                            guild.deleteCommandById(command.getId()).queue();
                        }
                    }
            );
        }

        commandMap.remove(id);
        discordCommandMap.remove(id);
    }

    public List<MinecraftCommand> getCommands(MinedisExtension extension) {
        if (commandMap.containsKey(extension.getClass().getName())) {{
            return Collections.unmodifiableList(
                commandMap.getOrDefault(extension.getClass().getName(), Collections.emptyList())
            );
        }}
        return Collections.emptyList();
    }
}
