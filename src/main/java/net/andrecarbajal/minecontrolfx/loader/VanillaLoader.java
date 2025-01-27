package net.andrecarbajal.minecontrolfx.loader;

import javafx.scene.image.Image;

import java.util.Objects;

public class VanillaLoader extends MojangLoader{
    @Override
    public String getLoaderName() {
        return "Vanilla";
    }

    @Override
    public Image getLoaderIcon() {
        return new Image(Objects.requireNonNull(getClass().getResourceAsStream("/loader-icons/vanilla.png")));
    }

    @Override
    public String type() {
        return "release";
    }
}
