package net.andrecarbajal.minecontrolfx.loader;

import java.util.List;

public class Loaders {
    public static List<ILoader> getLoaders() {
        return List.of(new VanillaLoader(), new SnapshotLoader());
    }
}
