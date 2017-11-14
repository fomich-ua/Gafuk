package ru.gafuk.android.fragments.blogs.details;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.LinearLayout;

import ru.gafuk.android.App;
import ru.gafuk.android.Constant;
import ru.gafuk.android.MainActivity;
import ru.gafuk.android.R;
import ru.gafuk.android.adapters.CommentsAdapter;
import ru.gafuk.android.api.blogs.models.BlogDetailsPage;
import ru.gafuk.android.api.comments.models.Comment;
import ru.gafuk.android.fragments.BaseFragment;
import ru.gafuk.android.rxapi.RxApi;
import ru.gafuk.android.utils.CustomWebChromeClient;
import ru.gafuk.android.utils.CustomWebViewClient;
import ru.gafuk.android.utils.rx.Subscriber;
import ru.gafuk.android.views.ExtendedWebView;

/**
 * Created by Александр on 10.11.2017.
 */

public class BlogContentFragment extends BaseFragment implements CommentsAdapter.ItemClickListener{
    public final static String JS_INTERFACE = "IBlog";

    public static final String KEY_BLOG_URL = "KEY_NEWS_URL";
    public static final String KEY_BLOG_TITLE = "KEY_NEWS_TITLE";

    public String blogurl = "";
    public String blogtitle = "";

    private ExtendedWebView webView;
    private BlogDetailsPage article;

    private CommentsAdapter commentsAdapter;
    private RecyclerView recyclerView;

    private Subscriber<BlogDetailsPage> detailsSubscriber = new Subscriber<>(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        configuration.setDefaultTitle(App.getInstance().getString(R.string.fragment_title_blog));
        configuration.setUseCache(false);
        configuration.setAlone(false);

        if (getArguments() != null) {
            blogurl = getArguments().getString(KEY_BLOG_URL, "");
            blogtitle = getArguments().getString(KEY_BLOG_TITLE, "");

            if (!blogtitle.isEmpty()) configuration.setDefaultTitle(blogtitle);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        baseInflateFragment(inflater, R.layout.content_comments_fragment);

        LinearLayout contentLayout = (LinearLayout) findViewById(R.id.contentLayout);

        // добавление WebView - сам блог
        webView = ((MainActivity) getActivity()).getWebViewsProvider().pull(getContext());
        registerForContextMenu(webView);
        webView.setWebViewClient(new ArticleWebViewClient());
        webView.setWebChromeClient(new CustomWebChromeClient());
        webView.addJavascriptInterface(this, JS_INTERFACE);
        webView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        webView.setNestedScrollingEnabled(false);

        contentLayout.addView(webView, 0);

        // комментарии
        LinearLayoutManager layoutManager= new LinearLayoutManager(getActivity());

        commentsAdapter = new CommentsAdapter(this);

        recyclerView = (RecyclerView) findViewById(R.id.comments_RecyclerView);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(commentsAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation()));

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
        loadDetailsPage();
    }

    private void loadDetailsPage(){
        detailsSubscriber.subscribe(RxApi.BlogsList().getDetails(blogurl), this::onLoadDetails, new BlogDetailsPage(), v -> loadDetailsPage());
    }

    private void onLoadDetails(BlogDetailsPage article) {
        webView.loadDataWithBaseURL(Constant.GAFUK_URL, article.getDescription(), "text/html", "utf-8", null);
        commentsAdapter.addAll(article.getComments(), true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
            ((MainActivity) getActivity()).getWebViewsProvider().push(webView);
        }
    }

    @JavascriptInterface
    public void toComments() {
        webView.runInUiThread(() -> {

        });
    }

    private class ArticleWebViewClient extends CustomWebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return handleUri(Uri.parse(url));
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return handleUri(request.getUrl());
        }

        private boolean handleUri(Uri uri) {
            // TODO: 06.10.2017  IntentHandler.handle(uri.toString());
            return true;
        }
    }

    @Override
    public void onItemClick(View view, Comment item, int position) {

    }

    @Override
    public boolean onLongItemClick(View view, Comment item, int position) {
        return false;
    }
}
