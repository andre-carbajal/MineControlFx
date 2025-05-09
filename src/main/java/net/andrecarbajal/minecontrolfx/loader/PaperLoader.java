package net.andrecarbajal.minecontrolfx.loader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.scene.image.Image;
import net.andrecarbajal.minecontrolfx.Constants;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class PaperLoader implements ILoader{
    @Override
    public String getLoaderName() {
        return "Paper";
    }

    @Override
    public Image getLoaderIcon() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/loader-icons/paper.png")));
    }

    @Override
    public String getLoaderApi() {
        return "https://api.papermc.io/v2/projects/paper";
    }

    @Override
    public List<String> getVersions() {
        List<String> versions = new ArrayList<>();
        try {
            URL url = new URL(getLoaderApi());
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Gson gson = new Gson();
            JsonObject manifest = gson.fromJson(reader, JsonObject.class);
            JsonArray versionsArray = manifest.getAsJsonArray("versions");

            for (int i = 0; i < versionsArray.size(); i++) {
                versions.add(versionsArray.get(i).getAsString());
            }

            Collections.reverse(versions);

            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            Constants.LOGGER.error("Error getting versions from Paper", e);
        }
        return versions;
    }

    public String getLatestBuild(String version) {
        int lastBuild = -1;
        try {
            URL url = new URL(getLoaderApi() + "/versions/" + version);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Gson gson = new Gson();
            JsonObject versionObj = gson.fromJson(reader, JsonObject.class);
            JsonArray buildsArray = versionObj.getAsJsonArray("builds");
            if (!buildsArray.isEmpty()) {
                lastBuild = buildsArray.get(buildsArray.size() - 1).getAsInt();
            }

            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            Constants.LOGGER.error("Error getting latest build from Paper", e);
        }
        return String.valueOf(lastBuild);
    }
}
