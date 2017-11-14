package ru.gafuk.android.views.drawers;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;

import ru.gafuk.android.fragments.BaseFragment;

/**
 * Created by Александр on 22.10.2017.
 */

public class MenuItem {
    private String title;
    private int iconRes;
    private int menuRes;
    private String attachedTabTag = "";
    private Class<? extends BaseFragment> tabClass;
    private boolean active = false;

    public MenuItem(String title, @DrawableRes int iconRes, @IdRes  int menuRes, Class<? extends BaseFragment> tabClass) {
        this.title = title;
        this.iconRes = iconRes;
        this.menuRes = menuRes;
        this.tabClass = tabClass;
    }

    public MenuItem(String title, @DrawableRes int iconRes, @IdRes  int menuRes) {
        this.title = title;
        this.iconRes = iconRes;
        this.menuRes = menuRes;
    }

    public String getTitle() {
        return title;
    }

    public int getIconRes() {
        return iconRes;
    }

    public int getMenuRes() {
        return menuRes;
    }

    public void setMenuRes(int menuRes) {
        this.menuRes = menuRes;
    }

    public String getAttachedTabTag() {
        return attachedTabTag;
    }

    public Class<? extends BaseFragment> getTabClass() {
        return tabClass;
    }

    public void setAttachedTabTag(String attachedTabTag) {
        this.attachedTabTag = attachedTabTag;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
