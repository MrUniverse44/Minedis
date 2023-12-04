package me.blueslime.minedis.modules.discord;

import me.blueslime.minedis.Minedis;
import me.blueslime.minedis.modules.DiscordModule;

public class Controller extends DiscordModule {
    private final Bot bot = new Bot(this);
    private boolean exists = false;
    public Controller(Minedis plugin) {
        super(plugin);
    }

    @Override
    public void load() {
        if (!exists) {
            bot.start();
            exists = true;
        }
    }

    public Bot getBot() {
        return bot;
    }

    public boolean isExists() {
        return exists;
    }
}
