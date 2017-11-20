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

import ru.gafuk.android.adapters.adapterdelegates.UsersAdapterDelegate;
import ru.gafuk.android.api.users.models.UserItem;
import ru.gafuk.android.glide.GlideRequest;
import ru.gafuk.android.glide.GlideRequests;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by Александр on 29.10.2017.
 */

public class UsersMainAdapter extends ListDelegationAdapter<List<UserItem>>
        implements ListPreloader.PreloadModelProvider<UserItem> {

    private UsersMainAdapter.ItemClickListener mItemClickListener;

    private GlideRequest<Drawable> fullRequest;
//    private GlideRequest<Drawable> thumbRequest;

    public UsersMainAdapter() {
        this.delegatesManager.addDelegate(new UsersAdapterDelegate());
        items = new ArrayList<>();
    }

    public UsersMainAdapter(ItemClickListener mItemClickListener, GlideRequests glideRequests) {
        this();
        this.mItemClickListener = mItemClickListener;

        fullRequest = glideRequests
                .asDrawable()
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transition(withCrossFade());
//                .placeholder(new ColorDrawable(Color.GRAY));

//        thumbRequest = glideRequests
//                .asDrawable()
//                .circleCrop()
//                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
//                .transition(withCrossFade());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);

        UsersAdapterDelegate.ViewHolder viewHolder = (UsersAdapterDelegate.ViewHolder) holder;

        if (viewHolder!=null) {
            viewHolder.clickContainer.setOnClickListener(v -> mItemClickListener.onItemClick(viewHolder.clickContainer, items.get(viewHolder.getLayoutPosition()), viewHolder.getLayoutPosition()));
            viewHolder.clickContainer.setOnLongClickListener(v -> mItemClickListener.onLongItemClick(viewHolder.clickContainer, items.get(viewHolder.getLayoutPosition()), viewHolder.getLayoutPosition()));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);

        UsersAdapterDelegate.ViewHolder viewHolder = (UsersAdapterDelegate.ViewHolder) holder;
        if (viewHolder!=null) {
            UserItem item = items.get(position);
            if (!TextUtils.isEmpty(item.getAvatar())) {
                fullRequest.load(item.getAvatar())
//                        .thumbnail(thumbRequest.load(item.getSmallAvatar()))
                        .into(viewHolder.user_avatar);
            }
        }
    }

    public void addItem(UserItem item){
        if (items==null){
            items = new ArrayList<>();
        }
        items.add(item);
        notifyItemInserted(getItemCount());
    }

    public void addAll(Collection<UserItem> newsItemsitems, boolean clearList){
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
    public List<UserItem> getPreloadItems(int position) {
        return items.subList(position, position + 1);
    }

    @Nullable
    @Override
    public RequestBuilder getPreloadRequestBuilder(UserItem item) {
        return fullRequest.load(item.getAvatar());
//        return fullRequest.thumbnail(thumbRequest.load(item.getSmallAvatar())).load(item.getSmallAvatar());
    }

    public void clear() {
        items.clear();
    }

    public interface ItemClickListener {

        boolean onLongItemClick(View view, UserItem item, int position);

        void onItemClick(View view, UserItem item, int position);

    }

}
