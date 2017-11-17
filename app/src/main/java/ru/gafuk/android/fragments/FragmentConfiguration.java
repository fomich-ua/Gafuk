package ru.gafuk.android.fragments;

/**
 * Created by Александр on 22.10.2017.
 */

public class FragmentConfiguration {
    private boolean needAuth = false;
    private boolean isAlone = false;
    private boolean isMenu = false;
    private boolean useCache = false;
    private String defaultTitle = "";

    public boolean needAuth() {
        return needAuth;
    }

    public void setNeedAuth(boolean needAuth) {
        this.needAuth = needAuth;
    }

    public boolean isAlone() {
        return isAlone;
    }

    public void setAlone(boolean alone) {
        isAlone = alone;
    }

    public boolean isMenu() {
        return isMenu;
    }

    public void setMenu(boolean menu) {
        isMenu = menu;
    }

    public boolean isUseCache() {
        return useCache;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    public String getDefaultTitle() {
        return defaultTitle;
    }

    public void setDefaultTitle(String defaultTitle) {
        this.defaultTitle = defaultTitle;
    }

    @Override
    public String toString() {
        return "FragmentConfiguration{" + isAlone() + ", " + isMenu() + ", " + isUseCache() + ", " + getDefaultTitle() + "}";
    }
}
