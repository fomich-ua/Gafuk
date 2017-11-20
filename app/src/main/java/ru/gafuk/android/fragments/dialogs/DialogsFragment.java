package ru.gafuk.android.fragments.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.ArrayList;
import java.util.List;

import ru.gafuk.android.App;
import ru.gafuk.android.R;
import ru.gafuk.android.api.dialogs.models.Dialog;
import ru.gafuk.android.fragments.BaseFragment;
import ru.gafuk.android.rxapi.RxApi;
import ru.gafuk.android.utils.rx.Subscriber;

/**
 * Created by aborz on 18.11.2017.
 */

public class DialogsFragment extends BaseFragment {

    private Subscriber<List<Dialog>> mainSubscriber = new Subscriber<>(this);

    private SwipeRefreshLayout refreshLayout;

    private ImageLoader imageLoader;
    private DialogsList dialogsList;
    private DialogsListAdapter<Dialog> dialogsAdapter;

    public DialogsFragment() {
        configuration.setDefaultTitle(App.getInstance().getString(R.string.fragment_title_dialogs));
        configuration.setAlone(true);
        configuration.setNeedAuth(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        baseInflateFragment(inflater, R.layout.dialogs_fragment);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.base_list_refresh);
        refreshLayout.setVisibility(View.VISIBLE);
        refreshLayout.setOnRefreshListener(this::loadData);

        dialogsList = (DialogsList) findViewById(R.id.dialogsList);

        imageLoader = (imageView, url) -> Glide.with(DialogsFragment.this).load(url).into(imageView);

        dialogsAdapter = new DialogsListAdapter<>(imageLoader);
        dialogsList.setAdapter(dialogsAdapter);

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
        loadDataDialogs(true);
    }

    private void loadDataDialogs(boolean withClear) {
        mainSubscriber.subscribe(RxApi.DialogsList().getDialogs(), list -> onLoadDialogs(list, withClear), new ArrayList<>(), v -> loadDataDialogs(withClear));
    }

    private void onLoadDialogs(List<Dialog> list, boolean withClear) {
        refreshLayout.setRefreshing(false);

        if (withClear) {
            dialogsAdapter.clear();
        }

        if (list.size() > 0) {
            dialogsAdapter.addItems(list);
        }
    }
}
