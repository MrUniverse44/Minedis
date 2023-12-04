package me.blueslime.minedis.api;

import me.blueslime.minedis.Minedis;
import me.blueslime.minedis.modules.DiscordModule;

public class MinedisAPI extends DiscordModule {
    private static MinedisAPI instance;
    public MinedisAPI(Minedis plugin) {
        super(plugin);
        instance = this;
    }

    public static MinedisAPI get() {
        return instance;
    }
}
