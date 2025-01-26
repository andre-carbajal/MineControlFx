package net.andrecarbajal.minecontrolfx.loader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.andrecarbajal.minecontrolfx.Constants;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public abstract class MojangLoader implements ILoader{
    public abstract String type();

    @Override
    public String getLoaderApi() {
        return "https://launchermeta.mojang.com/mc/game/version_manifest.json";
    }

    @Override
    public List<String> getVersions() {
        List<String> versions = new ArrayList<>();
        try {
            URL url = new URL(getLoaderApi());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Gson gson = new Gson();
            JsonObject manifest = gson.fromJson(reader, JsonObject.class);
            JsonArray versionArray = manifest.getAsJsonArray("versions");

            for (int i = 0; i < versionArray.size(); i++) {
                JsonObject versionObj = versionArray.get(i).getAsJsonObject();
                if (type().equals(versionObj.get("type").getAsString())){
                    versions.add(versionObj.get("id").getAsString());
                }
            }

            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            Constants.LOGGER.error("Error getting versions from Vanilla", e);
        }
        return versions;
    }

    public String getVersionId(String version) {
        try {
            URL url = new URL(getLoaderApi());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            Gson gson = new Gson();
            JsonObject manifest = gson.fromJson(reader, JsonObject.class);
            JsonArray versionArray = manifest.getAsJsonArray("versions");

            for (int i = 0; i < versionArray.size(); i++) {
                JsonObject versionObj = versionArray.get(i).getAsJsonObject();
                if (version.equals(versionObj.get("id").getAsString())){
                    String versionUrl = versionObj.get("url").getAsString();
                    reader.close();
                    connection.disconnect();

                    URL versionDetailsUrl = new URL(versionUrl);
                    HttpURLConnection versionConnection = (HttpURLConnection) versionDetailsUrl.openConnection();
                    versionConnection.setRequestMethod("GET");

                    BufferedReader versionReader = new BufferedReader(new InputStreamReader(versionConnection.getInputStream()));
                    JsonObject versionDetails = gson.fromJson(versionReader, JsonObject.class);
                    JsonObject downloads = versionDetails.getAsJsonObject("downloads");
                    JsonObject server = downloads.getAsJsonObject("server");
                    String serverUrl = server.get("url").getAsString();

                    versionReader.close();
                    versionConnection.disconnect();

                    return serverUrl;
                }
            }

            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            Constants.LOGGER.error("Error getting versions from Vanilla", e);
        }
        return null;
    }
}
