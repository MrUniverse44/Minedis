package me.blueslime.minedis.modules.discord;

import me.blueslime.minedis.Minedis;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class Bot extends Thread implements EventListener {

    private final Controller controller;

    private JDA client;

    public Bot(Controller controller) {
        this.controller = controller;
    }

    /**
     * The overridden Thread#run
     */
    @Override
    public void run() {
        Configuration settings = controller.getPlugin().getSettings();

        String token = settings.getString("settings.token", "NOT-SET");

        if (token != null && (!token.isEmpty() && !token.equals("NOT-SET"))) {
            JDABuilder builder = JDABuilder.createDefault(token);

            int intents = 0;

            for (String intent : settings.getStringList("settings.gateway-intents")) {
                try {
                    GatewayIntent gatewayIntent = GatewayIntent.valueOf(intent.toUpperCase(Locale.ENGLISH));

                    builder.enableIntents(gatewayIntent);
                    intents++;
                } catch (Exception ignored) {
                    controller.getLogger().info("Gateway Intent: " + intent + ", was not found!");
                    controller.getLogger().info("Available intents:");
                    controller.getLogger().info(
                            Arrays.stream(GatewayIntent.values()).map(Enum::toString).collect(Collectors.joining(" ,"))
                    );
                }
            }

            this.client = builder.build();
            controller.getLogger().info("Discord bot has been created with " + intents + " GatewayIntent(s).");
        }
    }

    @Override
    public void onEvent(@NotNull GenericEvent e) {
        if (e instanceof ReadyEvent) {
            updateStatus();
        }
    }

    public Minedis getPlugin() {
        return controller.getPlugin();
    }

    public void update() {

    }

    public void updateStatus() {
        if (client == null) {
            return;
        }

        update();
    }

    /**
     * Disconnection from discord before interrupting the thread
     */
    @Override
    public void interrupt() {
        client.shutdown();
        super.interrupt();
    }

    public JDA getClient() {
        return client;
    }
}