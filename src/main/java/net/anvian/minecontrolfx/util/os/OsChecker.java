package net.anvian.minecontrolfx.util.os;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public class OsChecker {
    public enum OSType {
        Windows, MacOS, Linux, Other
    }

    protected static OSType detectedOS;

    public static OSType getOperatingSystemType() {
        if (detectedOS == null) {
            String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
            if ((OS.contains("mac")) || (OS.contains("darwin"))) {
                detectedOS = OSType.MacOS;
            } else if (OS.contains("win")) {
                detectedOS = OSType.Windows;
            } else if (OS.contains("nux")) {
                detectedOS = OSType.Linux;
            } else {
                detectedOS = OSType.Other;
            }
        }
        return detectedOS;
    }

    public static String getFileFolder(){
        String fileFolder = switch (getOperatingSystemType()) {
            case Windows -> System.getenv("APPDATA");
            case MacOS -> System.getProperty("user.home") + "/Library/Application Support";
            case Linux -> System.getProperty("user.dir" + ".Launcher");
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
