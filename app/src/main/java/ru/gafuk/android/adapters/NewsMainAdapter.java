package ru.gafuk.android.adapters;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.gafuk.android.adapters.adapterdelegates.NewsAdapterDelegate;
import ru.gafuk.android.api.news.models.NewsItem;
import ru.gafuk.android.glide.GlideRequest;
import ru.gafuk.android.glide.GlideRequests;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by Александр on 28.10.2017.
 */

public class NewsMainAdapter extends ListDelegationAdapter<List<NewsItem>>
        implements ListPreloader.PreloadModelProvider<NewsItem>{

        private NewsMainAdapter.ItemClickListener mItemClickListener;

        private GlideRequest<Drawable> fullRequest;

        public NewsMainAdapter() {
            this.delegatesManager.addDelegate(new NewsAdapterDelegate());
            items = new ArrayList<>();
        }

        public NewsMainAdapter(ItemClickListener mItemClickListener, GlideRequests glideRequests) {
            this();
            this.mItemClickListener = mItemClickListener;

            fullRequest = glideRequests
                    .asDrawable()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .transition(withCrossFade());
//                .placeholder(new ColorDrawable(Color.GRAY));
        }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);

        NewsAdapterDelegate.ViewHolder viewHolder = (NewsAdapterDelegate.ViewHolder) holder;

        if (viewHolder!=null) {
            viewHolder.clickContainer.setOnClickListener(v -> mItemClickListener.onItemClick(viewHolder.clickContainer, items.get(viewHolder.getLayoutPosition()), viewHolder.getLayoutPosition()));
            viewHolder.clickContainer.setOnLongClickListener(v -> mItemClickListener.onLongItemClick(viewHolder.clickContainer, items.get(viewHolder.getLayoutPosition()), viewHolder.getLayoutPosition()));
        }
        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);

        NewsAdapterDelegate.ViewHolder viewHolder = (NewsAdapterDelegate.ViewHolder) holder;
        if (viewHolder!=null) {
            NewsItem item = items.get(position);
            if (viewHolder.imageView != null && !TextUtils.isEmpty(item.getImgUrl())) {
                fullRequest.load(item.getImgUrl())
//                        .thumbnail(thumbRequest.load(item.getSmallAvatar()))
                        .into(viewHolder.imageView);
            }
        }
    }

    public void addItem(NewsItem item){
        if (items==null){
            items = new ArrayList<>();
        }
        items.add(item);
        notifyItemInserted(getItemCount());
    }

    public void addAll(Collection<NewsItem> newsItemsitems, boolean clearList){
        if (clearList)
            items.clear();

        if (clearList){
            items.addAll(newsItemsitems);
            notifyDataSetChanged();
        }else {
            final int positionStart = getItemCount() + 1;
            items.addAll(newsItemsitems);
            notifyItemRangeInserted(positionStart, newsItemsitems.size());
        }
    }

    @NonNull
    @Override
    public List<NewsItem> getPreloadItems(int position) {
        return items.subList(position, position + 1);
    }

    @Nullable
    @Override
    public RequestBuilder getPreloadRequestBuilder(NewsItem item) {
        return fullRequest.load(item.getImgUrl());
//        return fullRequest.thumbnail(thumbRequest.load(item.getSmallAvatar())).load(item.getSmallAvatar());
    }

    public void clear() {
        items.clear();
    }

    public interface ItemClickListener {

        boolean onLongItemClick(View view, NewsItem item, int position);

        void onItemClick(View view, NewsItem item, int position);

    }
}
