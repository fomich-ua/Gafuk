package ru.gafuk.android.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.gafuk.android.Constant;
import ru.gafuk.android.R;

/**
 * Created by Александр on 22.10.2017.
 */

public class TabManager {
    private final static String LOG_TAG = Constant.GAFUK_LOG_PREFIX + TabManager.class.getSimpleName();
    private final static String TAB_PREFIX = "tab_";
    private final static String BUNDLE_PREFIX = "tab_manager_";
    private final static String BUNDLE_ACTIVE_TAG = "active_tag";
    private final static String BUNDLE_ACTIVE_INDEX = "active_index";
    private static TabManager instance;
    private FragmentManager fragmentManager;
    private TabListener tabListener;
    private static String activeTag = "";
    private static int activeIndex = 0;
    private List<BaseFragment> existingFragments = new ArrayList<>();

    public interface TabListener {
        void onAddTab(BaseFragment fragment);

        void onRemoveTab(BaseFragment fragment);

        void onSelectTab(BaseFragment fragment);

        void onChange();
    }

    public TabManager(AppCompatActivity activity, TabListener listener) {
        fragmentManager = activity.getSupportFragmentManager();
        tabListener = listener;
        updateFragmentList();
    }

    public static TabManager getInstance() {
        return instance;
    }

    public static TabManager init(AppCompatActivity activity, TabListener listener) {
        if (instance != null) {
            instance.clear();
            instance = null;
        }
        instance = new TabManager(activity, listener);
        return instance;
    }

    private void clear() {
        fragmentManager = null;
        tabListener = null;
        existingFragments.clear();
        existingFragments = null;
    }

    public void saveState(Bundle outState) {
        if (outState == null) return;
        outState.putString(BUNDLE_PREFIX.concat(BUNDLE_ACTIVE_TAG), activeTag);
        outState.putInt(BUNDLE_PREFIX.concat(BUNDLE_ACTIVE_INDEX), activeIndex);
        Log.d(LOG_TAG, "saveState: " + activeTag + " : " + activeIndex);
    }

    public void loadState(Bundle state) {
        if (state == null) return;
        activeTag = state.getString(BUNDLE_PREFIX.concat(BUNDLE_ACTIVE_TAG), "");
        activeIndex = state.getInt(BUNDLE_PREFIX.concat(BUNDLE_ACTIVE_INDEX), 0);
        Log.d(LOG_TAG, "loadState: " + activeTag + " : " + activeIndex);
    }

    public int getSize() {
        return existingFragments.size();
    }

    public static String getActiveTag() {
        return activeTag;
    }

    public static int getActiveIndex() {
        return activeIndex;
    }

    public List<BaseFragment> getFragments() {
        return existingFragments;
    }

    public void updateFragmentList() {
        Log.d(LOG_TAG, "updateFragmentList");
        existingFragments.clear();
        if (fragmentManager.getFragments() == null) return;
        for (int i = 0; i < fragmentManager.getFragments().size(); i++) {
            if (fragmentManager.getFragments().get(i) != null && fragmentManager.getFragments().get(i) instanceof BaseFragment) {
                Log.d(LOG_TAG, "update fragment " + fragmentManager.getFragments().get(i));
                existingFragments.add((BaseFragment) fragmentManager.getFragments().get(i));
            }
        }
        Collections.sort(existingFragments, (o1, o2) -> o1.getTag().compareTo(o2.getTag()));
    }

    private void hideTabs(FragmentTransaction transaction) {
        for (Fragment fragment : existingFragments) {
            if (fragment != null && !fragment.isHidden()) {
                transaction.hide(fragment);
                fragment.onPause();
            }
        }
    }

    private BaseFragment findTabByTag(String tag) {
        if (tag == null) return null;
        for (BaseFragment tab : existingFragments)
            if (tab.getTag().equals(tag))
                return tab;
        return null;
    }

    public BaseFragment getActive() {
        BaseFragment active = null;
        try {
            active = get(activeIndex);
        } catch (Exception ignore) {
        }
        return active;
    }

    public BaseFragment get(final int index) {
        return existingFragments.get(index);
    }

    public BaseFragment get(final String tag) {
        if (tag == null) return null;
        for (Fragment fragment : existingFragments)
            if (fragment.getTag().equals(tag))
                return (BaseFragment) fragment;
        return null;
    }

    public void add(Class<? extends BaseFragment> tClass) {
        add(tClass, null);
    }

    public void add(Class<? extends BaseFragment> tClass, Bundle args) {
        BaseFragment.Builder builder = new BaseFragment.Builder<>(tClass);
        if (args != null) {
            builder.setArgs(args);
        }
        BaseFragment fragment = builder.build();
        add(fragment);
    }

    public void add(BaseFragment baseFragment) {
        Log.d(LOG_TAG, "add: " + baseFragment);
        if (baseFragment == null)
            return;
        String check = null;
        if (baseFragment.getConfiguration().isAlone()) {
            check = getTagContainClass(baseFragment.getClass());
        }
        if (check != null) {
            select(check);
            return;
        }

        activeTag = TAB_PREFIX.concat(Long.toString(System.currentTimeMillis()));
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideTabs(transaction);
        //transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        transaction.add(R.id.fragments_container, baseFragment, activeTag).commit();
        fragmentManager.executePendingTransactions();
        updateFragmentList();
        activeIndex = existingFragments.indexOf(baseFragment);
        tabListener.onChange();
        tabListener.onAddTab(baseFragment);
    }

    public void add(BaseFragment baseFragment, View sharedElement, Fragment fragment) {
        if (baseFragment == null)
            return;
        String check = null;
        if (baseFragment.getConfiguration().isAlone()) {
            check = getTagContainClass(baseFragment.getClass());
        }
        if (check != null) {
            select(check);
            return;
        }

        activeTag = TAB_PREFIX.concat(Long.toString(System.currentTimeMillis()));
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideTabs(transaction);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // TODO: 16.09.2017  baseFragment.setSharedElementEnterTransition(new DetailsTransition());
            baseFragment.setEnterTransition(new Fade());
            fragment.setExitTransition(new Fade());
            // TODO: 16.09.2017  baseFragment.setSharedElementReturnTransition(new DetailsTransition());
        }

        transaction.addSharedElement(sharedElement, "detailsCover");
        transaction.add(R.id.fragments_container, baseFragment, activeTag).commit();
        fragmentManager.executePendingTransactions();
        updateFragmentList();
        activeIndex = existingFragments.indexOf(baseFragment);
        tabListener.onChange();
        tabListener.onAddTab(baseFragment);
    }

    public String getTagContainClass(final Class aClass) {
        String className = aClass.getSimpleName();
        for (BaseFragment fragment : existingFragments) {
            if (fragment.getClass().getSimpleName().equals(className)) return fragment.getTag();
        }
        return null;
    }

    public BaseFragment getByClass(final Class aClass) {
        String className = aClass.getSimpleName();
        for (BaseFragment fragment : existingFragments) {
            if (fragment.getClass().getSimpleName().equals(className))
                return fragment;
        }
        return null;
    }

    public void remove(final String tag) {
        remove(get(tag));
    }

    public void remove(BaseFragment baseFragment) {
        Log.d(LOG_TAG, "remove: " + baseFragment);
        if (baseFragment == null)
            return;

        fragmentManager.beginTransaction().remove(baseFragment).commit();
        fragmentManager.executePendingTransactions();
        updateFragmentList();

        BaseFragment parent = null;
        if (baseFragment.getParentTag() != null && !baseFragment.getParentTag().equals(""))
            parent = findTabByTag(baseFragment.getParentTag());

        if (parent == null) {
            if (existingFragments.size() >= 1) {
                if (existingFragments.size() <= activeIndex)
                    activeIndex = existingFragments.size() - 1;

                activeTag = existingFragments.get(activeIndex).getTag();
            } else {
                activeIndex = 0;
                activeTag = "";
            }
        } else {
            activeTag = baseFragment.getParentTag();
            activeIndex = existingFragments.indexOf(parent);
        }

        select(activeTag);
        tabListener.onChange();
        tabListener.onRemoveTab(baseFragment);
    }

    public void select(final String tag) {
        select(get(tag));
    }

    public void select(BaseFragment baseFragment) {
        Log.d(LOG_TAG, "select: " + baseFragment);
        if (baseFragment == null)
            return;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideTabs(transaction);
        transaction.show(baseFragment).commit();
        baseFragment.onResume();
        fragmentManager.executePendingTransactions();
        updateFragmentList();
        activeTag = baseFragment.getTag();
        activeIndex = existingFragments.indexOf(baseFragment);
        tabListener.onChange();
        tabListener.onSelectTab(baseFragment);
    }
}
