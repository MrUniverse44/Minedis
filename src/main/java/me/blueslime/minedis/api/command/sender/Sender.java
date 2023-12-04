package me.blueslime.minedis.api.command.sender;

import me.blueslime.minedis.utils.text.TextReplacer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.util.List;

public class Sender {
    private final CommandSender sender;

    private Sender(CommandSender sender) {
        this.sender = sender;
    }

    public static Sender build(CommandSender sender) {
        return new Sender(sender);
    }

    public boolean isPlayer() {
        return sender instanceof ProxiedPlayer;
    }

    public ProxiedPlayer toPlayer() {
        return (ProxiedPlayer)sender;
    }

    public boolean isConsole() {
        return !(sender instanceof ProxiedPlayer);
    }

    public boolean hasPermission(String permission) {
        boolean has = sender.hasPermission(permission);

        if (!has && !(sender instanceof ProxiedPlayer)) {
            return true;
        }

        return has;
    }

    public void send(TextReplacer replacer, String... messages) {
        if (messages == null || messages.length == 0) {
            sender.sendMessage(new TextComponent(" "));
            return;
        }

        for (String message : messages) {
            sender.sendMessage(
                    new TextComponent(
                        colorize(
                                replacer == null ?
                                        message :
                                        replacer.apply(message)
                        )
                    )
            );
        }
    }

    public void send(String... messages) {
        send(null, messages);
    }

    public void send(Configuration configuration, String path, Object def, TextReplacer replacer) {
        Object ob = configuration.get(path, def);

        if (ob == null && def == null) {
            return;
        }

        if (ob instanceof Configuration && !(def instanceof Configuration)) {
            ob = def;
        }

        if (ob instanceof List) {
            List<?> list = (List<?>)ob;
            for (Object object : list) {
                send(
                        replacer,
                        object.toString()
                );
            }
        } else {
            send(
                    colorize(
                            replacer == null ?
                                    ob.toString() :
                                    replacer.apply(ob.toString())
                    )
            );
        }
    }

    public void send(Configuration configuration, String path, Object def) {
        send(configuration, path, def, null);
    }

    public void send(BaseComponent... components) {
        if (isPlayer()) {
            toPlayer().sendMessage(components);
        }
    }

    public void send(Configuration configuration, String path, TextReplacer replacer) {
        Object ob = configuration.get(path);

        if (ob == null) {
            return;
        }

        if (ob instanceof List) {
            List<?> list = (List<?>)ob;
            for (Object object : list) {
                send(
                        colorize(
                                replacer == null ?
                                        object.toString() :
                                        replacer.apply(object.toString())
                        )
                );
            }
        } else {
            send(
                    colorize(
                            replacer == null ?
                                    ob.toString() :
                                    replacer.apply(ob.toString())
                    )
            );
        }
    }

    public void send(Configuration configuration, String path) {
        send(configuration, path, null);
    }

    public void send(List<String> messages, TextReplacer replacer) {
        if (messages == null || messages.isEmpty()) {
            return;
        }

        for (String message : messages) {
            sender.sendMessage(
                    new TextComponent(
                        colorize(
                                replacer == null ?
                                        message :
                                        replacer.apply(message)
                        )
                    )
            );
        }
    }

    public void send(List<String> messages) {
        send(messages, null);
    }

    public String colorize(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
