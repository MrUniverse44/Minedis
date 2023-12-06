package me.blueslime.minedis.modules.extensions;

import me.blueslime.minedis.Minedis;
import me.blueslime.minedis.api.extension.MinedisExtension;
import me.blueslime.minedis.api.extension.MinedisExtensionIdentifierException;
import me.blueslime.minedis.modules.DiscordModule;
import me.blueslime.minedis.modules.commands.Commands;
import me.blueslime.minedis.modules.listeners.Listeners;
import me.blueslime.minedis.utils.task.TaskExecutor;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class Extensions extends DiscordModule {
    private final Map<String, MinedisExtension> extensionMap = new ConcurrentHashMap<>();
    private final File folder;

    public Extensions(Minedis plugin) {
        super(plugin);

        folder = getDirectoryFile("extensions");

        plugin.getLogger().info("Loading extensions...   (" + folder.mkdirs() + ")");
    }

    @Override
    public void load() {
        for (MinedisExtension extension : extensionMap.values()) {
            extension.onDisable();
            getModule(Commands.class).unload(extension);
            getModule(Listeners.class).unregister(extension);
        }

        extensionMap.clear();

        TaskExecutor.execute(
                find(),
                (list, exception) -> {
                    if (exception != null) {
                        getLogger().info("Can't register extensions");
                        return;
                    }

                    for (List<Class<? extends MinedisExtension>> extensions : list) {
                        for (Class<? extends MinedisExtension> extension : extensions) {
                            try {
                                MinedisExtension instance = extension.getDeclaredConstructor()
                                        .newInstance();

                                if (instance.register()) {
                                    if (instance.isEnabled()) {
                                        if (!extensionMap.containsKey(instance.getIdentifier())) {
                                            extensionMap.put(
                                                    instance.getIdentifier(),
                                                    instance
                                            );
                                            instance.onEnabled();
                                            getLogger().info("Extension: " + extension.getName() + " was loaded using identifier: " + instance.getIdentifier());
                                        } else {
                                            getLogger().info("Ignoring extension: " + extension.getName() + ", This extension have a conflict with other extension using the same identifier. (" + instance.getIdentifier() + ")");
                                        }
                                    }
                                }

                            } catch (Exception e) {
                                if (e instanceof NullPointerException) {
                                    getLogger().info("Can't load extension: " + extension.getName() + ", because the extension was not found.");
                                    return;
                                }
                                if (e instanceof IllegalArgumentException) {
                                    getLogger().info("Can't load extension: " + extension.getName() + ", because the constructor have parameters");
                                    return;
                                }
                                if (e instanceof InstantiationException) {
                                    getLogger().info("Can't load extension: " + extension.getName() + ", this extension is a abstract class.");
                                    return;
                                }
                                if (e instanceof MinedisExtensionIdentifierException) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
        );
    }

    @Override
    public void update() {
        load();
    }

    @Override
    public boolean isExtensionInstalled(String id) {
        return extensionMap.containsKey(id);
    }

    private CompletableFuture<List<List<Class<? extends MinedisExtension>>>> find() {
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".jar"));
        if (files == null) {
            return CompletableFuture.completedFuture(Collections.emptyList());
        }

        return Arrays.stream(files)
                .map(this::findExtendedClass)
                .collect(TaskExecutor.collect());
    }


    public CompletableFuture<List<Class<? extends MinedisExtension>>> findExtendedClass(final File file) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!file.exists()) {
                    return null;
                }

                final URL jarFile = file.toURI().toURL();

                final URLClassLoader jarLoader = new URLClassLoader(
                        new URL[]{
                                jarFile
                        },
                        MinedisExtension.class.getClassLoader()
                );

                final List<Class<? extends MinedisExtension>> classList = new ArrayList<>();

                try {
                    JarInputStream stream = new JarInputStream(jarFile.openStream());

                    List<String> classNameList = new ArrayList<>();

                    JarEntry entry;

                    while ((entry = stream.getNextJarEntry()) != null) {
                        String name = entry.getName();

                        if (!name.endsWith(".class")) {
                            continue;
                        }

                        classNameList.add(
                                name.substring(
                                        0,
                                        name.lastIndexOf('.')
                                ).replace(
                                        '/',
                                        '.'
                                )
                        );
                    }

                    try {
                        for (String clazzName : classNameList) {
                            Class<?> loadedClazz = jarLoader.loadClass(clazzName);

                            if (MinedisExtension.class.isAssignableFrom(loadedClazz)) {
                                classList.add(
                                        loadedClazz.asSubclass(MinedisExtension.class)
                                );
                            }
                        }
                    } catch (NoClassDefFoundError ignored) {
                    }

                    if (classList.isEmpty()) {
                        getLogger().info("Failed to load extension " + file.getName() + ", this extension doesn't have MinedisExtensions class in the jar file.");
                        jarLoader.close();
                        return null;
                    }

                    return classList;
                } catch (Throwable ignored) {

                }
            } catch (Exception ignored) {
                getLogger().info("The plugin can't load extensions :(");
            }
            return null;
        });
    }

    public Map<String, MinedisExtension> getExtensionMap() {
        return extensionMap;
    }
}
