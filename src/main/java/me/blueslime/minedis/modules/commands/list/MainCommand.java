package me.blueslime.minedis.modules.commands.list;

import me.blueslime.minedis.Minedis;
import me.blueslime.minedis.api.command.MinecraftCommand;
import me.blueslime.minedis.api.command.sender.Sender;
import me.blueslime.minedis.api.extension.MinedisExtension;
import me.blueslime.minedis.modules.extensions.Extensions;

import java.util.HashMap;
import java.util.Map;

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
            return;
        }
        if (arguments[0].equalsIgnoreCase("reload")) {
            plugin.reload();

            sender.send(
                    "&aPlugin has been reloaded."
            );
            return;
        }

        if (arguments[0].equalsIgnoreCase("extensions")) {
            Map<String, MinedisExtension> extensionMap = new HashMap<>(
                    plugin.getModule(Extensions.class).getExtensionMap()
            );

            for (Map.Entry<String, MinedisExtension> entry : extensionMap.entrySet()) {
                MinedisExtension extension = entry.getValue();

                sender.send(
                        "&7> &6Extension:&f " + extension.getName() + " &6with id:&f " + entry.getKey()
                );
            }
            sender.send("&aThe plugin detected " + extensionMap.size() + " extension(s).");
        }
    }
}
