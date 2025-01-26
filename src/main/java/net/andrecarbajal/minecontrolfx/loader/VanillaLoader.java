package net.andrecarbajal.minecontrolfx.loader;

public class VanillaLoader extends MojangLoader{
    @Override
    public String getLoaderName() {
        return "Vanilla";
    }

    @Override
    public String type() {
        return "release";
    }
}
