package net.andrecarbajal.minecontrolfx.loader;

import javafx.scene.image.Image;

import java.util.List;

public interface ILoader {
    String getLoaderName();
    Image getLoaderIcon();
    String getLoaderApi();
    List<String> getVersions();
}
