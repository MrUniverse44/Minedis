package me.blueslime.minedis.utils.consumer;

import me.blueslime.minedis.api.MinedisAPI;

import java.util.function.Consumer;

public interface PluginConsumer<T> {

    T executeConsumer() throws Exception;

    interface PluginOutConsumer {
        void executeConsumer() throws Exception;
    }

    static  void process(PluginOutConsumer consumer) {
        try {
            consumer.executeConsumer();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    static void process(String message, PluginOutConsumer consumer) {
        try {
            consumer.executeConsumer();
        } catch (Exception ex) {
            MinedisAPI.get().getLogger().info(message);
            ex.printStackTrace();
        }
    }

    static  void process(PluginOutConsumer consumer, Consumer<Exception> exception) {
        try {
            consumer.executeConsumer();
        } catch (Exception ex) {
            exception.accept(ex);
        }
    }

    static <T> T ofUnchecked(final PluginConsumer<T> template) {
        T results = null;
        try {
            results = template.executeConsumer();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return results;
    }

    static <T> T ofUnchecked(final PluginConsumer<T> template, final Consumer<Exception> exception, T defValue) {
        T results = defValue;
        try {
            results = template.executeConsumer();
        } catch (Exception ex) {
            exception.accept(ex);
        }
        return results;
    }

    static <T> T ofUnchecked(final PluginConsumer<T> template, final Consumer<Exception> exception) {
        T results = null;
        try {
            results = template.executeConsumer();
        } catch (Exception ex) {
            exception.accept(ex);
        }
        return results;
    }

    static <T> T ofUnchecked(final PluginConsumer<T> template, T defValue) {
        T results = defValue;
        try {
            results = template.executeConsumer();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return results;
    }

    static <T> T ofUnchecked(String message, final PluginConsumer<T> template) {
        T results = null;
        try {
            results = template.executeConsumer();
        } catch (Exception ex) {
            MinedisAPI.get().getLogger().info(message);
            ex.printStackTrace();
        }
        return results;
    }

    static <T> T ofUnchecked(String message, final PluginConsumer<T> template, T defValue) {
        T results = defValue;
        try {
            results = template.executeConsumer();
        } catch (Exception ex) {
            MinedisAPI.get().getLogger().info(message);
            ex.printStackTrace();
        }
        return results;
    }

    static <T> PluginConsumer<T> of(PluginConsumer<T> c){ return c; }
}

