package me.blueslime.minedis.api.command;

public class DiscordCommand {
    private final String guildID;
    private final String id;

    private DiscordCommand(String guildID, String id) {
        this.guildID = guildID;
        this.id = id;
    }

    public static DiscordCommand build(String guildID, String id) {
        return new DiscordCommand(guildID, id);
    }

    public String getId() {
        return id;
    }

    public String getGuild() {
        return guildID;
    }
}
