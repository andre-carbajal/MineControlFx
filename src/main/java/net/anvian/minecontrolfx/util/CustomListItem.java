package net.anvian.minecontrolfx.util;

import javafx.scene.image.Image;

public class CustomListItem {
    private String directoryName;
    private Image icon;

    public CustomListItem(String directoryName, Image icon) {
        this.directoryName = directoryName;
        this.icon = icon;
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public Image getIcon() {
        return icon;
    }
}