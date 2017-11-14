package ru.gafuk.android.fragments;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.Observer;

import io.reactivex.disposables.CompositeDisposable;
import ru.gafuk.android.MainActivity;
import ru.gafuk.android.R;
import ru.gafuk.android.client.Client;

/**
 * Created by Александр on 22.10.2017.
 */

public abstract class BaseFragment extends Fragment {
    protected final static String LOG_TAG = BaseFragment.class.getSimpleName();
    public final static String ARG_TITLE = "TAB_TITLE";
    public final static String TAB_SUBTITLE = "TAB_SUBTITLE";

    private final static String BUNDLE_PREFIX = "tab_fragment_";
    private final static String BUNDLE_TITLE = "title";
    private final static String BUNDLE_TAB_TITLE = "tab_title";
    private final static String BUNDLE_SUBTITLE = "subtitle";
    private final static String BUNDLE_PARENT_TAG = "parent_tag";

    protected FragmentConfiguration configuration = new FragmentConfiguration();

    private String title = null, tabTitle = null, subtitle = null, parentTag = null;

    protected View view;
    protected CoordinatorLayout coordinatorLayout;
    protected LinearLayout fragmentContent;

    private boolean alreadyCallLoad = false;

    protected CompositeDisposable disposable = new CompositeDisposable();

    protected Observer networkObserver = (observable, o) -> {
        if (o == null)
            o = true;
        if (!configuration.isUseCache()
//                || noNetwork.getVisibility() == View.VISIBLE)
                && (boolean) o) {
            if (!alreadyCallLoad)
                loadData();
//            noNetwork.setVisibility(View.GONE);
        }
    };

    public BaseFragment() {
        parentTag = TabManager.getActiveTag();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            title = savedInstanceState.getString(BUNDLE_PREFIX.concat(BUNDLE_TITLE));
            subtitle = savedInstanceState.getString(BUNDLE_PREFIX.concat(BUNDLE_SUBTITLE));
            tabTitle = savedInstanceState.getString(BUNDLE_PREFIX.concat(BUNDLE_TAB_TITLE));
            parentTag = savedInstanceState.getString(BUNDLE_PREFIX.concat(BUNDLE_PARENT_TAG));
        }
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            subtitle = getArguments().getString(TAB_SUBTITLE);
        }
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.base_list_fragment, container, false);
//        coordinatorLayout = view.findViewById(R.id.coordinator_layout);
        view = inflater.inflate(R.layout.base_fragment, container, false);
        coordinatorLayout = view.findViewById(R.id.coordinator_layout);
        fragmentContent = view.findViewById(R.id.fragment_content);

        boolean isMenu = configuration.isAlone() || configuration.isMenu();

        Client.addNetworkObserver(networkObserver);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(LOG_TAG, "onDestroy " + this);
        if (!disposable.isDisposed())
            disposable.dispose();

        Client.removeNetworkObserver(networkObserver);
    }

    @CallSuper
    public boolean onBackPressed() {
        Log.d(LOG_TAG, "onBackPressed " + this);
        return false;
    }

    protected void viewsReady() {
        if (Client.getNetworkState() && !configuration.isUseCache()) {
            if (!alreadyCallLoad)
                loadData();
        } else {
            loadCacheData();
        }
    }

    protected void baseInflateFragment(LayoutInflater inflater, @LayoutRes int res) {
        inflater.inflate(res, fragmentContent, true);
    }

    @CallSuper
    public void loadData() {
        alreadyCallLoad = true;
    }

    @CallSuper
    public void loadCacheData() {

    }

    public void hidePopupWindows() {
        getMainActivity().hideKeyboard();
    }

    public final View findViewById(@IdRes int id) {
        return view.findViewById(id);
    }

    public CompositeDisposable getDisposable() {
        return disposable;
    }

    public CoordinatorLayout getCoordinatorLayout() {
        return coordinatorLayout;
    }

    public final MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    public String getParentTag() {
        return parentTag;
    }

    public FragmentConfiguration getConfiguration() {
        return configuration;
    }

    public String getTitle() {
        return title == null ? configuration.getDefaultTitle() : title;
    }

    public final void setTitle(String newTitle) {
        this.title = newTitle;
//        if (tabTitle == null)
//            getMainActivity().updateFragmentsList();
//        toolbarTitleView.setText(getTitle());
    }

    public String getTabTitle() {
        return tabTitle == null ? getTitle() : tabTitle;
    }

    public void setTabTitle(String tabTitle) {
        this.tabTitle = tabTitle;
        getMainActivity().updateFragmentsList();
    }

    public final String getSubtitle() {
        return subtitle;
    }

    public final void setSubtitle(String newSubtitle) {
        this.subtitle = newSubtitle;
//        if (subtitle == null) {
//            if (toolbarSubtitleView.getVisibility() != View.GONE)
//                toolbarSubtitleView.setVisibility(View.GONE);
//        } else {
//            if (toolbarSubtitleView.getVisibility() != View.VISIBLE)
//                toolbarSubtitleView.setVisibility(View.VISIBLE);
//            toolbarSubtitleView.setText(getSubtitle());
//        }
    }

    /* Experiment */
    public static class Builder<T extends BaseFragment> {
        private T tClass;

        public Builder(Class<T> tClass) {
            try {
                this.tClass = tClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Builder setArgs(Bundle args) {
            tClass.setArguments(args);
            return this;
        }

        public Builder setIsMenu() {
            tClass.configuration.setMenu(true);
            return this;
        }

        public T build() {
            return tClass;
        }
    }
}
