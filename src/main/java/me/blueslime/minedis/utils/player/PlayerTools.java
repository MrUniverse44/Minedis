package me.blueslime.minedis.utils.player;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerTools {
    public static String getId(ProxiedPlayer player) {
        return player.getUniqueId().toString().replace("-", "");
    }

    public static String getIP(ProxiedPlayer player) {
        String ip = player.getSocketAddress().toString();
        if (ip.contains("/")) {
            return ip.split("/")[1].replace("/", "").split(":")[0];
        }
        return ip.replace("localhost", "").replace("/", "").split(":")[0];
    }
}
