package ru.gafuk.android.fragments.users;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import ru.gafuk.android.App;
import ru.gafuk.android.Constant;
import ru.gafuk.android.R;
import ru.gafuk.android.adapters.UsersMainAdapter;
import ru.gafuk.android.adapters.adapterdelegates.UsersAdapterDelegate;
import ru.gafuk.android.api.users.models.SearchUsersResult;
import ru.gafuk.android.api.users.models.UserItem;
import ru.gafuk.android.fragments.BaseFragment;
import ru.gafuk.android.glide.GlideApp;
import ru.gafuk.android.glide.GlideRequests;
import ru.gafuk.android.rxapi.RxApi;
import ru.gafuk.android.settings.Preferences;
import ru.gafuk.android.utils.rx.Subscriber;
import ru.gafuk.android.views.EndlessRecyclerViewScrollListener;

/**
 * Created by Александр on 27.10.2017.
 */

public class UsersFragment extends BaseFragment
        implements UsersMainAdapter.ItemClickListener{

    public static final String LOG_TAG = Constant.GAFUK_LOG_PREFIX + UsersFragment.class.getSimpleName();

    private UsersMainAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    private Subscriber<List<UserItem>> mainSubscriber = new Subscriber<>(this);
    private Subscriber<SearchUsersResult> searchUsersResultSubscriber = new Subscriber<>(this);

    private static final int PRELOAD_AHEAD_ITEMS = 3;

    private int startPageNumber = 1;
    private LinearLayoutManager layoutManager;

    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

    protected Observer preferenceObserver = (observable, o) -> {
        if (o == null) return;
        String key = (String) o;
        switch (key) {
            case Preferences.Users.LOAD_ON_SCROLL: {
                if (Preferences.Users.getLoadOnScroll()){
                    removeEndlessScrollLitener();
                    addEndlessScrollListener();
                    loadData();
                }else {
                    removeEndlessScrollLitener();
                }
                break;
            }
        }
    };

    public UsersFragment() {
        configuration.setDefaultTitle(App.getInstance().getString(R.string.fragment_title_users));
        configuration.setAlone(true);
        configuration.setNeedAuth(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        baseInflateFragment(inflater, R.layout.base_list_fragment);

        GlideRequests glideRequests = GlideApp.with(this);

        layoutManager = new LinearLayoutManager(getContext());

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_list_refresh);
        refreshLayout.setVisibility(View.VISIBLE);
        refreshLayout.setOnRefreshListener(this::loadData);

        adapter = new UsersMainAdapter(this, glideRequests);

        ViewPreloadSizeProvider<UserItem> preloadSizeProvider = new ViewPreloadSizeProvider<>();
        RecyclerViewPreloader<UserItem> preloader = new RecyclerViewPreloader<>(
                glideRequests, adapter, preloadSizeProvider, PRELOAD_AHEAD_ITEMS);

        recyclerView = (RecyclerView) findViewById(R.id.base_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(preloader);
        recyclerView.setRecyclerListener(holder -> {
            UsersAdapterDelegate.ViewHolder vh = (UsersAdapterDelegate.ViewHolder) holder;
            glideRequests.clear(vh.user_avatar);
        });

        recyclerView.setAdapter(adapter);

        if (Preferences.Users.getLoadOnScroll()){
            addEndlessScrollListener();
        }

        App.getInstance().addPreferenceChangeObserver(preferenceObserver);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewsReady();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        App.getInstance().removePreferenceChangeObserver(preferenceObserver);
    }

    private void addEndlessScrollListener(){
        if (scrollListener==null){
            scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    loadDataUsers(page, false);
                }
            };
        }
        recyclerView.removeOnScrollListener(scrollListener);
        recyclerView.addOnScrollListener(scrollListener);
        scrollListener.resetState();
    }

    private void removeEndlessScrollLitener(){
        if (scrollListener!=null){
            recyclerView.removeOnScrollListener(scrollListener);
            scrollListener = null;
        }
    }

    @Override
    public void loadData() {
        super.loadData();

        refreshLayout.setRefreshing(true);
        cancelSearch();
//        loadDataUsers(startPageNumber, true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.contacts_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        SearchManager searchManager = (SearchManager) getMainActivity().getSystemService(Context.SEARCH_SERVICE);
        if (null != searchManager) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getMainActivity().getComponentName()));
        }

        searchView.setIconifiedByDefault(true);

        // Get the search close button image view
        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final String nickname = newText.trim();

                if (nickname.length() > 2){

                    refreshLayout.setRefreshing(true);

                    getDisposable().clear();

                    searchUsersResultSubscriber.subscribe(RxApi.UsersList().getSearchResult(nickname), searchUsersResult -> onLoadSearchResult(searchUsersResult), new SearchUsersResult(), v -> loadSearchUsers(nickname));
                }else if (nickname.isEmpty()){

                }

                return false;
            }

        });

        // When using the support library, the setOnActionExpandListener() method is
        // static and accepts the MenuItem object as an argument
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                //Nothing to do here
                Log.d(LOG_TAG, "Search widget expand ");
                return true; // Return true to expand action view
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                refreshLayout.setRefreshing(true);
                mainSubscriber.subscribe(RxApi.UsersList().cancelSearch(), list -> onLoadUsers(list, true), new ArrayList<>(), v -> cancelSearch());
                Log.d(LOG_TAG, "Search widget colapsed ");
                return true; // Return true to collapse action view
            }
        });

        searchView.setQueryHint(getString(R.string.user));

    }

    private void loadDataUsers(int pageNumber, boolean withClear) {
        mainSubscriber.subscribe(RxApi.UsersList().getUsers(pageNumber), list -> onLoadUsers(list, withClear), new ArrayList<>(), v -> loadDataUsers(pageNumber, withClear));
    }

    private void onLoadUsers(List<UserItem> list, boolean withClear) {
        refreshLayout.setRefreshing(false);
        if (withClear) {
            adapter.clear();
            if (scrollListener!=null) scrollListener.resetState();
        }
        if (list.size() > 0) {
            adapter.addAll(list, withClear);
        }
        adapter.notifyDataSetChanged();

        if (withClear){
            recyclerView.scrollToPosition(0);
        }
    }

    private void loadSearchUsers(String nickname){
        searchUsersResultSubscriber.subscribe(RxApi.UsersList().getSearchResult(nickname), this::onLoadSearchResult, new SearchUsersResult(), v -> loadSearchUsers(nickname));
    }

    private void onLoadSearchResult(SearchUsersResult searchUsersResult){
        refreshLayout.setRefreshing(false);

        adapter.clear();
        if (scrollListener!=null){
            scrollListener.resetState();
            scrollListener.setLastPageIndex(searchUsersResult.getTotalPages());
        }

        adapter.addAll(searchUsersResult.getUsers(), true);
        adapter.notifyDataSetChanged();

        recyclerView.scrollToPosition(0);

    }

    private void cancelSearch(){
        refreshLayout.setRefreshing(true);
        mainSubscriber.subscribe(RxApi.UsersList().cancelSearch(), list -> onLoadUsers(list, true), new ArrayList<>(), v -> cancelSearch());
    }

    @Override
    public boolean onLongItemClick(View view, UserItem item, int position) {
        return false;
    }

    @Override
    public void onItemClick(View view, UserItem item, int position) {

    }

}
