package net.andrecarbajal.minecontrolfx.loader;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.andrecarbajal.minecontrolfx.Constants;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FabricLoader implements ILoader{
    @Override
    public String getLoaderName() {
        return "Fabric";
    }

    @Override
    public String getLoaderApi() {
        return "https://meta.fabricmc.net/v2/versions/game/";
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
            JsonArray manifest = gson.fromJson(reader, JsonArray.class);

            for (JsonElement element : manifest) {
                JsonObject versionObj = element.getAsJsonObject();
                boolean isStable = versionObj.get("stable").getAsBoolean();
                if (isStable){
                    versions.add(versionObj.get("version").getAsString());
                }
            }

            reader.close();
            connection.disconnect();
        } catch (Exception e) {
            Constants.LOGGER.error("Error getting versions from Paper", e);
        }
        return versions;
    }
}