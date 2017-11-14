package ru.gafuk.android.fragments.blogs.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import ru.gafuk.android.App;
import ru.gafuk.android.R;
import ru.gafuk.android.adapters.BlogsMainAdapter;
import ru.gafuk.android.api.blogs.models.BlogItem;
import ru.gafuk.android.fragments.BaseFragment;
import ru.gafuk.android.fragments.TabManager;
import ru.gafuk.android.fragments.blogs.details.BlogContentFragment;
import ru.gafuk.android.rxapi.RxApi;
import ru.gafuk.android.settings.Preferences;
import ru.gafuk.android.utils.rx.Subscriber;
import ru.gafuk.android.views.EndlessRecyclerViewScrollListener;

/**
 * Created by Александр on 22.10.2017.
 */

public class BlogsMainFragment extends BaseFragment implements BlogsMainAdapter.ItemClickListener{

    private BlogsMainAdapter adapter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    private Subscriber<List<BlogItem>> mainSubscriber = new Subscriber<>(this);

    private int pageNumber = 1;
    private LinearLayoutManager layoutManager;

    // Store a member variable for the listener
    private EndlessRecyclerViewScrollListener scrollListener;

    protected Observer preferenceObserver = (observable, o) -> {
        if (o == null) return;
        String key = (String) o;
        switch (key) {
            case Preferences.Blog.PROVIDER:{
                if (Preferences.Blog.getProvider().equalsIgnoreCase("blog_site")
                        && Preferences.Blog.getLoadOnScroll()){
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
            case Preferences.Blog.LOAD_ON_SCROLL:{
                if (Preferences.Blog.getProvider().equalsIgnoreCase("blog_site") && Preferences.Blog.getLoadOnScroll()){
                    removeEndlessScrollLitener();
                    addEndlessScrollListener();
                }else {
                    removeEndlessScrollLitener();
                }
                loadData();
                break;
            }
        }
    };

    public BlogsMainFragment() {
        configuration.setDefaultTitle(App.getInstance().getString(R.string.fragment_title_blogs_list));
        configuration.setAlone(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState); //R.layout.base_list_fragment, container, false);

        baseInflateFragment(inflater, R.layout.base_list_fragment);

        layoutManager = new LinearLayoutManager(getContext());

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_list_refresh);
        refreshLayout.setVisibility(View.VISIBLE);
        refreshLayout.setOnRefreshListener(this::loadData);

        adapter = new BlogsMainAdapter(this);

        recyclerView = (RecyclerView) findViewById(R.id.base_list);
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        if (Preferences.Blog.getProvider().equalsIgnoreCase("blog_site")
            && Preferences.Blog.getLoadOnScroll()){
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
        inflater.inflate(R.menu.blog_list_menu, menu);
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
                    loadDataBlogs(page, false);
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
        loadDataBlogs(pageNumber, true);
    }

    private void loadDataBlogs(int page, boolean withClear) {
        mainSubscriber.subscribe(RxApi.BlogsList().getBlogs(page), list -> onLoadBlogs(list, withClear), new ArrayList<>(), v -> loadDataBlogs(page, withClear));
    }

    private void onLoadBlogs(List<BlogItem> list, boolean withClear) {
        refreshLayout.setRefreshing(false);
        if (withClear) {
            adapter.clear();
        }
        if (list.size() > 0) {
            adapter.addAll(list, withClear);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onLongItemClick(View view, BlogItem item, int position) {
        return false;
    }

    @Override
    public void onItemClick(View view, BlogItem item, int position) {

        Bundle args = new Bundle();
        args.putString(BlogContentFragment.KEY_BLOG_URL, item.getUrl());
        args.putString(BlogContentFragment.KEY_BLOG_TITLE, item.getTitle());
        TabManager.getInstance().add(BlogContentFragment.class, args);

    }

}
