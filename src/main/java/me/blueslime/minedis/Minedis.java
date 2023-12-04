package me.blueslime.minedis;

import me.blueslime.minedis.api.MinedisAPI;
import me.blueslime.minedis.modules.discord.Controller;
import me.blueslime.minedis.modules.extensions.Extensions;
import me.blueslime.minedis.modules.plugin.Plugin;

public class Minedis extends Plugin {

    @Override
    public void onEnable() {
        initialize(this);
    }

    @Override
    public void onDisable() {
        shutdown();
    }

    @Override
    public void registerModules() {
        registerModule(
                new MinedisAPI(this),
                new Controller(this),
                new Extensions(this)
        ).finish();
    }
}
