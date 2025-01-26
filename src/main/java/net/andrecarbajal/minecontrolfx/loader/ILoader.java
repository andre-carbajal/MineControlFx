package net.andrecarbajal.minecontrolfx.loader;

import java.util.List;

public interface ILoader {
    String getLoaderName();
    String getLoaderApi();
    List<String> getVersions();
}
