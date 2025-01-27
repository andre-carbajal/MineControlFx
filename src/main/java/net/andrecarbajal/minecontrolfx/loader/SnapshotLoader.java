package net.andrecarbajal.minecontrolfx.loader;

import javafx.scene.image.Image;

import java.util.Objects;

public class SnapshotLoader extends MojangLoader{
    @Override
    public String getLoaderName() {
        return "Snapshot";
    }

    @Override
    public Image getLoaderIcon() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/loader-icons/snapshot.png")));
    }

    @Override
    public String type() {
        return "snapshot";
    }
}
