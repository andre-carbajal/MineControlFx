package net.andrecarbajal.minecontrolfx.loader;

public class SnapshotLoader extends MojangLoader{
    @Override
    public String getLoaderName() {
        return "Snapshot";
    }

    @Override
    public String type() {
        return "snapshot";
    }
}
