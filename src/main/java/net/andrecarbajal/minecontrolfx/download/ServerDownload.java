package net.andrecarbajal.minecontrolfx.download;

import net.andrecarbajal.minecontrolfx.loader.FabricLoader;
import net.andrecarbajal.minecontrolfx.loader.ILoader;
import net.andrecarbajal.minecontrolfx.loader.MojangLoader;
import net.andrecarbajal.minecontrolfx.loader.PaperLoader;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

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