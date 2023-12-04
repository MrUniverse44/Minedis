package me.blueslime.minedis.utils.text;

import java.util.HashMap;
import java.util.Map;

public class TextReplacer {
    private final Map<String, String> replacements = new HashMap<>();

    private TextReplacer() {

    }

    public TextReplacer replace(String key, String value) {
        replacements.put(key, value);
        return this;
    }

    public String apply(String text) {
        for (Map.Entry<String, String> entry : replacements.entrySet()) {
            text = text.replace(
                    entry.getKey(),
                    entry.getValue()
            );
        }
        return text;
    }

    public static TextReplacer builder() {
        return new TextReplacer();
    }
}

