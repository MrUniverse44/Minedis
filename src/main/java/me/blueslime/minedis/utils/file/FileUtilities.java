package me.blueslime.minedis.utils.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.logging.Logger;

public class FileUtilities {
    public static void saveConfig(File file, Logger logs, InputStream resource) {
        checkFileExistence(file, logs, resource);
    }

    public static InputStream build(String location) {
        if (!location.startsWith("/")) {
            return FileUtilities.class.getResourceAsStream("/" + location);
        }
        return FileUtilities.class.getResourceAsStream(location);
    }

    public static void saveResource(File file, InputStream resource) {
        checkFileExistence(file, resource);
    }

    public static void checkFileExistence(File file, Logger logs, InputStream resource) {
        if (!file.getParentFile().exists()) {
            boolean createFile = file.getParentFile().mkdirs();
            if (createFile && logs != null) {
                logs.info("&7Folder created!!");
            }
        }

        if (!file.exists()) {
            try (InputStream in = resource) {
                cloneResource(file, logs, in);
            } catch (Exception exception) {
                if (logs != null) {
                    logs.info(
                            String.format("A error occurred while copying the config %s to the plugin data folder.", file.getName())
                    );
                }
            }
        }
    }

    public static void checkFileExistence(File file, InputStream resource) {
        if (!file.getParentFile().exists()) {
            boolean createFile = file.getParentFile().mkdirs();
            if (!createFile) {
                new NullPointerException("folder can't be created!").printStackTrace();
            }
        }

        if (!file.exists()) {
            try (InputStream in = resource) {
                cloneResource(file, in);
            } catch (Exception exception) {
                new NullPointerException("resource can't be cloned!").printStackTrace();
            }
        }
    }

    public static void cloneResource(File file, InputStream in) throws IOException {
        if (in != null) {
            Files.copy(in, file.toPath());
        } else {
            boolean created = file.createNewFile();
            if (!created) {
                new NullPointerException("file can't be created!").printStackTrace();
            }
        }
    }

    public static void cloneResource(File file, Logger logs, InputStream in) throws IOException {
        if (in != null) {
            Files.copy(in, file.toPath());
        } else {
            if (logs != null) {
                logs.info("Resource is null");
                logs.info("Creating a empty file for " + file.getName());
            }
            boolean created = file.createNewFile();
            if (created && logs != null) {
                logs.info("File created!");
            }
        }
    }
}
