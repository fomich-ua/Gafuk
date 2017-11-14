package ru.gafuk.android.fragments.news.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import ru.gafuk.android.App;
import ru.gafuk.android.R;
import ru.gafuk.android.adapters.NewsMainAdapter;
import ru.gafuk.android.adapters.adapterdelegates.NewsAdapterDelegate;
import ru.gafuk.android.api.news.models.NewsItem;
import ru.gafuk.android.fragments.BaseFragment;
import ru.gafuk.android.fragments.TabManager;
import ru.gafuk.android.fragments.news.details.NewsContentFragment;
import ru.gafuk.android.glide.GlideApp;
import ru.gafuk.android.glide.GlideRequests;
import ru.gafuk.android.rxapi.RxApi;
import ru.gafuk.android.settings.Preferences;
import ru.gafuk.android.utils.AlertDialogMenu;
import ru.gafuk.android.utils.StringUtils;
import ru.gafuk.android.utils.rx.Subscriber;
import ru.gafuk.android.views.EndlessRecyclerViewScrollListener;

/**
 * Created by Александр on 22.10.2017.
 */

public class NewsMainFragment extends BaseFragment
        implements NewsMainAdapter.ItemClickListener{

    private NewsMainAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    private Subscriber<List<NewsItem>> mainSubscriber = new Subscriber<>(this);
    private Subscriber<NewsItem> itemSubscriber = new Subscriber<>(this);

    private static final int PRELOAD_AHEAD_ITEMS = 3;

    private AlertDialogMenu<NewsMainFragment, NewsItem> dialogMenu, showedDialogMenu;

    private int pageNumber = 1;
    private LinearLayoutManager layoutManager;

    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

    GlideRequests glideRequests;

    protected Observer preferenceObserver = (observable, o) -> {
        if (o == null) return;
        String key = (String) o;
        switch (key) {
            case Preferences.News.ITEM_VIEW: {
                adapter.clear();
                adapter = new NewsMainAdapter(this, glideRequests);
                recyclerView.setAdapter(adapter);
                loadData();
                break;
            }
            case Preferences.News.PROVIDER:{
                if (Preferences.News.getProvider().equalsIgnoreCase("news_site")
                        && Preferences.News.getLoadOnScroll()){
                    removeEndlessScrollLitener();
                    addEndlessScrollListener();
                    pageNumber = 1;
//                    loadDataNews(pageNumber, true);
//                    loadData();
                }else {
                    removeEndlessScrollLitener();
                }
                loadData();
                break;
            }
            case Preferences.News.LOAD_ON_SCROLL:{
                if (Preferences.News.getProvider().equalsIgnoreCase("news_site")
                        && Preferences.News.getLoadOnScroll()){
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

    public NewsMainFragment() {
        configuration.setDefaultTitle(App.getInstance().getString(R.string.fragment_title_news_list));
        configuration.setAlone(true);
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

        glideRequests = GlideApp.with(this);

        layoutManager = new LinearLayoutManager(getContext());

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_list_refresh);
        refreshLayout.setVisibility(View.VISIBLE);
        refreshLayout.setOnRefreshListener(this::loadData);

        adapter = new NewsMainAdapter(this, glideRequests);

        ViewPreloadSizeProvider<NewsItem> preloadSizeProvider = new ViewPreloadSizeProvider<>();
        RecyclerViewPreloader<NewsItem> preloader = new RecyclerViewPreloader<>(
                glideRequests, adapter, preloadSizeProvider, PRELOAD_AHEAD_ITEMS);

        recyclerView = (RecyclerView) findViewById(R.id.base_list);
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemViewCacheSize(5);
//        recyclerView.setHasFixedSize(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setRecyclerListener(holder -> {
            NewsAdapterDelegate.ViewHolder vh = (NewsAdapterDelegate.ViewHolder) holder;
            if (vh.imageView != null) glideRequests.clear(vh.imageView);
        });

        if (Preferences.News.getProvider().equalsIgnoreCase("news_site")
                && Preferences.News.getLoadOnScroll()){
            addEndlessScrollListener();
        };

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.news_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh_list) {
            loadData();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addEndlessScrollListener(){
        if (scrollListener==null){
            scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
                @Override
                public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                    loadDataNews(page, false);
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
        pageNumber = 1;
        refreshLayout.setRefreshing(true);
        loadDataNews(pageNumber, true);
    }

    private void loadDataNews(int pageNumber, boolean withClear) {
        mainSubscriber.subscribe(RxApi.NewsList().getNews(pageNumber), list -> onLoadNews(list, withClear), new ArrayList<>(), v -> loadDataNews(pageNumber, withClear));
//        itemSubscriber.subscribe(RxApi.NewsList().getNewsItems(pageNumber), this::onLoadNewsItem, null, v -> loadDataNews(pageNumber, withClear));
    }

    private void onLoadNewsItem(NewsItem newsItem){
        if (newsItem!=null){
            adapter.addItem(newsItem);
        }
    }

    private void onLoadNews(List<NewsItem> list, boolean withClear) {
        refreshLayout.setRefreshing(false);
        if (withClear) {
//            adapter.clear();
            if (scrollListener!=null) scrollListener.resetState();
        }
        if (list.size() > 0) {
            adapter.addAll(list, withClear);
        }
    }

    @Override
    public boolean onLongItemClick(View view, NewsItem item, int position) {
        if (dialogMenu == null){
            dialogMenu = new AlertDialogMenu<>();
            showedDialogMenu = new AlertDialogMenu<>();

            dialogMenu.addItem(getString(R.string.open_browser), (context, data) -> {
                StringUtils.openUrlInBrowser(data.getUrl());
            });

            dialogMenu.addItem(getString(R.string.copy_link), (context, data) -> {
                StringUtils.copyToClipBoard(data.getUrl());
            });

            dialogMenu.addItem(getString(R.string.share), (context, data) -> {
                StringUtils.shareText(data.getUrl());
            });
        }

        showedDialogMenu.clear();

        showedDialogMenu.addItem(dialogMenu.get(0));
        showedDialogMenu.addItem(dialogMenu.get(1));
        showedDialogMenu.addItem(dialogMenu.get(2));

        new AlertDialog.Builder(getContext())
                .setTitle(R.string.action_select)
                .setItems(showedDialogMenu.getTitles(), (dialog, which) -> {
                    showedDialogMenu.onClick(which, NewsMainFragment.this, item);
                })
                .show();
        return true;
    }

    @Override
    public void onItemClick(View view, NewsItem item, int position) {
        Bundle args = new Bundle();
        args.putString(NewsContentFragment.KEY_NEWS_URL, item.getUrl());
        args.putString(NewsContentFragment.KEY_NEWS_TITLE, item.getTitle());
        TabManager.getInstance().add(NewsContentFragment.class, args);
    }
}
