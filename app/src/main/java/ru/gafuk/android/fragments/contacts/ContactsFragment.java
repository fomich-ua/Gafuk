package ru.gafuk.android.fragments.contacts;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import java.util.Observable;
import java.util.Observer;

import ru.gafuk.android.App;
import ru.gafuk.android.R;
import ru.gafuk.android.adapters.ContactsMainAdapter;
import ru.gafuk.android.adapters.adapterdelegates.ContactsAdapterDelegate;
import ru.gafuk.android.api.contacts.models.ContactItem;
import ru.gafuk.android.client.Client;
import ru.gafuk.android.fragments.BaseFragment;
import ru.gafuk.android.fragments.TabManager;
import ru.gafuk.android.fragments.chat.ChatFragment;
import ru.gafuk.android.fragments.news.main.NewsMainFragment;
import ru.gafuk.android.glide.GlideApp;
import ru.gafuk.android.glide.GlideRequests;
import ru.gafuk.android.rxapi.RxApi;
import ru.gafuk.android.utils.rx.Subscriber;
import ru.gafuk.android.views.drawers.DrawerMenu;

/**
 * Created by Александр on 24.10.2017.
 */

public class ContactsFragment extends BaseFragment
        implements ContactsMainAdapter.ItemClickListener{

    private ContactsMainAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    private Subscriber<List<ContactItem>> mainSubscriber = new Subscriber<>(this);
    private List<ContactItem> results;

    private static final int PRELOAD_AHEAD_ITEMS = 3;

    public ContactsFragment() {
        configuration.setDefaultTitle(App.getInstance().getString(R.string.fragment_title_contacts));
        configuration.setAlone(true);
        configuration.setNeedAuth(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        baseInflateFragment(inflater, R.layout.base_list_fragment);

        GlideRequests glideRequests = GlideApp.with(this);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_list_refresh);
        refreshLayout.setVisibility(View.VISIBLE);
        refreshLayout.setOnRefreshListener(this::loadData);

        adapter = new ContactsMainAdapter(this, glideRequests);

        ViewPreloadSizeProvider<ContactItem> preloadSizeProvider = new ViewPreloadSizeProvider<ContactItem>();
        RecyclerViewPreloader<ContactItem> preloader = new RecyclerViewPreloader<>(
                        glideRequests, adapter, preloadSizeProvider, PRELOAD_AHEAD_ITEMS);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.base_list);
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addOnScrollListener(preloader);
        recyclerView.setRecyclerListener(holder -> {
            ContactsAdapterDelegate.ViewHolder vh = (ContactsAdapterDelegate.ViewHolder) holder;
            glideRequests.clear(vh.contact_avatar);
        });

        recyclerView.setAdapter(adapter);

        results = new ArrayList<>();

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
    }

    @Override
    public void loadData() {
        super.loadData();

        refreshLayout.setRefreshing(true);
        loadDataContacts(true);
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            private ArrayList<ContactItem> searchContacts = new ArrayList<>();

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchContacts.clear();
                if (!newText.isEmpty()) {
                    for (ContactItem contact : results) {
                        if (contact.getNickname().toLowerCase().contains(newText.toLowerCase()))
                            searchContacts.add(contact);
                    }
                    adapter.addAll(searchContacts, true);
                } else {
                    adapter.addAll(results, true);
                }
                return false;
            }
        });

        searchView.setQueryHint(getString(R.string.user));
    }

    private void loadDataContacts(boolean withClear) {
        mainSubscriber.subscribe(RxApi.ContactsList().getContacts(), list -> onLoadContacts(list, withClear), new ArrayList<>(), v -> loadDataContacts(withClear));
    }

    private void onLoadContacts(List<ContactItem> list, boolean withClear) {
        refreshLayout.setRefreshing(false);

        if (withClear) {
            results.clear();
            adapter.clear();
        }

        if (list.size() > 0) {
            results.addAll(list);
            adapter.addAll(list, withClear);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onLongItemClick(View view, ContactItem item, int position) {
        return false;
    }

    @Override
    public void onItemClick(View view, ContactItem item, int position) {
        Bundle args = new Bundle();
        args.putString(BaseFragment.ARG_TITLE, App.getInstance().getString(R.string.fragment_title_messages) + " " + item.getNickname());
        args.putInt(ChatFragment.CONTACT_ID_KEY, item.getId());
        args.putString(ChatFragment.CONTACT_NICK_KEY, item.getNickname());
        args.putString(ChatFragment.CONTACT_AVATAR_KEY, item.getAvatar());

        TabManager.getInstance().add(ChatFragment.class, args);
    }
}
