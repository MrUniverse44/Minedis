package me.blueslime.minedis.modules.commands.list;

import me.blueslime.minedis.Minedis;
import me.blueslime.minedis.api.command.MinecraftCommand;
import me.blueslime.minedis.api.command.sender.Sender;

public class MainCommand extends MinecraftCommand {
    private final Minedis plugin;
    public MainCommand(Minedis plugin, String name) {
        super(name);
        this.plugin = plugin;
    }

    @Override
    public void execute(Sender sender, String[] arguments) {
        if (!sender.hasPermission("minedis.command")) {
            sender.send("&cYou don't have permissions for this command.");
        }
        if (arguments.length == 0 || arguments[0].equalsIgnoreCase("help")) {
            sender.send(
                    "&d/minedis extensions &5- &fView the extensions installed",
                    "&d/minedis reload &5- &fReload the plugin extensions and configuration",
                    " ",
                    "&eMinedis v" + plugin.getDescription().getVersion()
            );
        }
    }
}
