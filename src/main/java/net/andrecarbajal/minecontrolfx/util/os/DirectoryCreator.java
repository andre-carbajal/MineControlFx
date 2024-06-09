package net.andrecarbajal.minecontrolfx.util.os;

import net.andrecarbajal.minecontrolfx.Constants;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryCreator {
    public static String getFileFolder(OsChecker.OSType osType) {
        String fileFolder = switch (osType) {
            case Windows -> System.getenv("APPDATA");
            case MacOS -> System.getProperty("user.home") + "/Library/Application Support";
            default -> System.getProperty("user.home");
        };

        Path path = Paths.get(fileFolder, "MineControl");
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                Constants.LOGGER.error("Failed to create directory: {}", path, e);
            }
        }

        return path.toString();
    }
}