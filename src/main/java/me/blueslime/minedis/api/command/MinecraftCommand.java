package me.blueslime.minedis.api.command;

import me.blueslime.minedis.api.command.sender.Sender;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public abstract class MinecraftCommand extends Command {
    public MinecraftCommand(String name) {
        super(name);
    }

    public abstract void execute(Sender sender, String[] arguments);

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        execute(Sender.build(commandSender), strings);
    }
}
