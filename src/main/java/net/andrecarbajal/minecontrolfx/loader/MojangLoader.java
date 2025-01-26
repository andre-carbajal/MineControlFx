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
        return "https://launchermeta.mojang.com/mc/";
    }

    @Override
    public List<String> getVersions() {
        List<String> versions = new ArrayList<>();
        try {
            URL url = new URL(getLoaderApi() + "game/version_manifest.json");
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
}
