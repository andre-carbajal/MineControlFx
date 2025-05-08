# MineControlFx: Your Minecraft Server Management Tool

Tired of wrestling with server setups and configurations? MineControlFx is here to streamline your Minecraft server management experience. This user-friendly application provides an interface for creating, controlling, and managing your Minecraft servers. Whether you're a seasoned server administrator or just starting, MineControlFx simplifies the process, letting you focus on what matters most: the gameplay.

## Project Requirements

To run MineControlFx, you'll need:

*   **Java 17 or higher:** MineControlFx is built using JavaFX and requires a Java Runtime Environment (JRE) version 17 or higher.
*   **Maven:** Maven is used for project dependency management and building the application.

## Dependencies

MineControlFx relies on the following key dependencies:

*   **JavaFX:** For building the graphical user interface.
*   **SLF4J:** A logging facade for consistent logging output.
*   **Gson:** For parsing JSON data from Minecraft server APIs.

## Getting Started

Ready to dive in? Here's how to get MineControlFx up and running:

1.  **Build the project:** Use Maven to build the project:

    ```bash
    mvn clean package
    ```

    This command will compile the source code, download dependencies, and create a JAR file in the `target/` directory.

## How to Run the Application

Once you have built the project, you can run MineControlFx with the following command:

```bash
java -jar target/MineControlFx-{version}.jar
```

Replace `{version}` with the actual version number found in the `target` directory.

## Relevant Code Examples

Let's explore some key code snippets to understand how MineControlFx works:

### Downloading a Minecraft Server

The `ServerDownload` class handles the process of downloading server files.  It uses loader specific logic to get the download URL.

```java
public class ServerDownload implements IDownloader {
    public void download(ILoader selectedItem, String version, Path serverFolder) throws IOException {
        String downloadUrl = getDownloadUrl(selectedItem, version);
        if (downloadUrl != null) {
            download(new URL(downloadUrl), serverFolder.resolve("server.jar"));
        } else {
            throw new IOException("No se pudo obtener la URL de descarga para el loader seleccionado.");
        }
    }

    private String getDownloadUrl(ILoader loader, String version) {
        if (loader instanceof PaperLoader paperLoader) {
            String latestBuild = paperLoader.getLatestBuild(version);
            return paperLoader.getLoaderApi() + "/versions/" + version + "/builds/" + latestBuild + "/downloads/paper-" + version + "-" + latestBuild + ".jar";
        } else if (loader instanceof FabricLoader fabricLoader) {
            String loaderVersion = fabricLoader.getLoaderVersion();
            String installerVersion = fabricLoader.getInstallerVersion();
            return "https://meta.fabricmc.net/v2/versions/loader/" + version + "/" + loaderVersion + "/" + installerVersion + "/server/jar";
        } else if (loader instanceof MojangLoader mojangLoader) {
            return mojangLoader.getVersionId(version);
        }
        return null;
    }
}
```

This code snippet demonstrates how the `ServerDownload` class uses an `ILoader` to fetch the download link for a specific server type (Paper, Fabric, or Vanilla) and starts the download.

### Creating a Server Directory

The `DirectoryCreator` class is responsible for creating the necessary directories for storing server files, using the OS Checker for cross-platform compatibility.

```java
public class DirectoryCreator {
    public static Path getFileFolder(OsChecker.OSType osType) {
        String fileFolder = switch (osType) {
            case Windows -> System.getenv("APPDATA");
            case MacOS -> System.getProperty("user.home") + "/Library/Application Support";
            default -> System.getProperty("user.home");
        };

        Path path = Paths.get(fileFolder, "MineControl");
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                Constants.LOGGER.error("Failed to create directory: {}", path, e);
            }
        }

        return path;
    }

    public static Path getServerFolder(OsChecker.OSType osType) {
        Path path = getFileFolder(osType).resolve("servers");
        if (Files.notExists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                Constants.LOGGER.error("Failed to create directory: {}", path, e);
            }
        }

        return path;
    }
}
```

This snippet shows how `DirectoryCreator` determines the appropriate directory for server files based on the operating system, ensuring that MineControlFx works seamlessly across platforms.

## Conclusion

MineControlFx offers a streamlined, user-friendly solution for managing Minecraft servers. From easy server setup to seamless downloading and directory management, this application simplifies the entire process.  Jump in, explore the code, and contribute to making MineControlFx even better!
