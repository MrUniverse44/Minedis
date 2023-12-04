package me.blueslime.minedis.utils.text;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtilities {
    public static Color color = null;
    public static String string(String message) {
        if (color == null) {
            color = new Color();
        }

        return color.execute(message);
    }

    public static TextComponent component(String message) {
        return new TextComponent(string(message));
    }

    public static String strip(String message) {
        return ChatColor.stripColor(message);
    }


    public static class Color {
        private Method COLORIZE_METHOD;

        public Color() {
            try {
                COLORIZE_METHOD = ChatColor.class.getDeclaredMethod("of", String.class);
            } catch (Exception ignored) {
                COLORIZE_METHOD = null;
            }
        }

        public String execute(String message) {
            Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
            Matcher matcher = pattern.matcher(message);

            while (matcher.find()) {
                String code = message.substring(matcher.start(), matcher.end());
                if (COLORIZE_METHOD != null) {
                    try {
                        ChatColor color = (ChatColor) COLORIZE_METHOD.invoke(
                                ChatColor.WHITE,
                                code
                        );

                        message = message.replace(
                                code,
                                String.valueOf(color)
                        );

                        matcher = pattern.matcher(
                                message
                        );
                    } catch (IllegalAccessException | InvocationTargetException e ) {
                        return message.replace(
                                code,
                                ""
                        );
                    }
                }
            }
            return ChatColor.translateAlternateColorCodes('&', message);
        }
    }
}
