package ru.gafuk.android.fragments.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.util.ViewPreloadSizeProvider;

import java.util.ArrayList;
import java.util.List;

import ru.gafuk.android.App;
import ru.gafuk.android.R;
import ru.gafuk.android.adapters.ChatAdapter;
import ru.gafuk.android.api.contacts.models.Message;
import ru.gafuk.android.fragments.BaseFragment;
import ru.gafuk.android.glide.GlideApp;
import ru.gafuk.android.glide.GlideRequests;
import ru.gafuk.android.rxapi.RxApi;
import ru.gafuk.android.utils.rx.Subscriber;

/**
 * Created by Александр on 29.10.2017.
 */

public class ChatFragment extends BaseFragment
        implements ChatAdapter.ItemClickListener {

    public final static String CONTACT_ID_KEY = "CONTACT_ID_KEY";
    public final static String CONTACT_NICK_KEY = "CONTACT_NICK_KEY";
    public final static String CONTACT_AVATAR_KEY = "CONTACT_AVATAR_KEY";

    private int contact_id;
    private String contact_nick;
    private String contact_avatar;

    private ChatAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;

    private Subscriber<List<Message>> mainSubscriber = new Subscriber<>(this);

    private static final int PRELOAD_AHEAD_ITEMS = 3;

    public ChatFragment() {
        configuration.setAlone(false);
        configuration.setMenu(false);
        configuration.setUseCache(false);
        configuration.setDefaultTitle(App.getInstance().getString(R.string.fragment_title_messages));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            contact_id = getArguments().getInt(CONTACT_ID_KEY);
            contact_nick = getArguments().getString(CONTACT_NICK_KEY);
            contact_avatar = getArguments().getString(CONTACT_AVATAR_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);

        baseInflateFragment(inflater, R.layout.chat_fragment);

        GlideRequests glideRequests = GlideApp.with(this);

        layoutManager = new LinearLayoutManager(getContext());

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_list_refresh);
        refreshLayout.setVisibility(View.VISIBLE);
        refreshLayout.setOnRefreshListener(this::loadData);

        adapter = new ChatAdapter(contact_id, contact_nick, contact_avatar, this, glideRequests);

        ViewPreloadSizeProvider<Message> preloadSizeProvider = new ViewPreloadSizeProvider<>();
        RecyclerViewPreloader<Message> preloader = new RecyclerViewPreloader<>(
                glideRequests, adapter, preloadSizeProvider, PRELOAD_AHEAD_ITEMS);

        recyclerView = (RecyclerView) findViewById(R.id.base_list);
        recyclerView.setVerticalScrollBarEnabled(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(preloader);
        recyclerView.setAdapter(adapter);

//        if (Preferences.News.getLoadOnScroll()){
//            addEndlessScrollListener();
//        };
//
//        App.getInstance().addPreferenceChangeObserver(preferenceObserver);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewsReady();
    }


    @Override
    public void loadData() {
        super.loadData();

        refreshLayout.setRefreshing(true);
        loadDataMessages(true);
    }

    private void loadDataMessages(boolean withClear) {
        mainSubscriber.subscribe(RxApi.ContactsList().getContactMessages(contact_id), list -> onLoadMessages(list, true), new ArrayList<>(), v -> loadDataMessages(withClear));
    }

    private void onLoadMessages(List<Message> messages, boolean clearList){
        refreshLayout.setRefreshing(false);
        adapter.addAll(messages, clearList);
    }

    @Override
    public boolean onLongItemClick(View view, Message item, int position) {
        return false;
    }

    @Override
    public void onItemClick(View view, Message item, int position) {

    }

}
