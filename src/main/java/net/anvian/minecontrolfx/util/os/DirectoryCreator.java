package net.anvian.minecontrolfx.util.os;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DirectoryCreator {
    public static String getFileFolder(OsChecker.OSType osType) {
        String fileFolder = switch (osType) {
            case Windows -> System.getenv("APPDATA");
            case MacOS -> System.getProperty("user.home") + "/Library/Application Support";
            case Linux -> System.getProperty("user.home" + ".Launcher");
            default -> System.getProperty("user.home");
        };

        Path path = Paths.get(fileFolder, "MineControl");
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return path.toString();
    }
}