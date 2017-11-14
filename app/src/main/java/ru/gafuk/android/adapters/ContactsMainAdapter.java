package ru.gafuk.android.adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import ru.gafuk.android.Constant;
import ru.gafuk.android.adapters.adapterdelegates.ContactsAdapterDelegate;
import ru.gafuk.android.api.contacts.models.ContactItem;
import ru.gafuk.android.glide.GlideRequest;
import ru.gafuk.android.glide.GlideRequests;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by Александр on 29.10.2017.
 */

public class ContactsMainAdapter extends ListDelegationAdapter<List<ContactItem>>
        implements ListPreloader.PreloadModelProvider<ContactItem> {

    private ContactsMainAdapter.ItemClickListener mItemClickListener;

    private GlideRequest<Drawable> fullRequest;
    private GlideRequest<Drawable> thumbRequest;

    public ContactsMainAdapter() {
        this.delegatesManager.addDelegate(new ContactsAdapterDelegate());
        items = new ArrayList<>();
    }

    public ContactsMainAdapter(ItemClickListener mItemClickListener, GlideRequests glideRequests) {
        this();
        this.mItemClickListener = mItemClickListener;

        fullRequest = glideRequests
                .asDrawable()
                .circleCrop()
                .placeholder(new ColorDrawable(Color.GRAY));

        thumbRequest = glideRequests
                .asDrawable()
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transition(withCrossFade());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);

        ContactsAdapterDelegate.ViewHolder viewHolder = (ContactsAdapterDelegate.ViewHolder) holder;

        if (viewHolder!=null) {
            viewHolder.clickContainer.setOnClickListener(v -> mItemClickListener.onItemClick(viewHolder.clickContainer, items.get(viewHolder.getLayoutPosition()), viewHolder.getLayoutPosition()));
            viewHolder.clickContainer.setOnLongClickListener(v -> mItemClickListener.onLongItemClick(viewHolder.clickContainer, items.get(viewHolder.getLayoutPosition()), viewHolder.getLayoutPosition()));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);

        ContactsAdapterDelegate.ViewHolder viewHolder = (ContactsAdapterDelegate.ViewHolder) holder;
        if (viewHolder!=null) {
            ContactItem item = items.get(position);
            if (!TextUtils.isEmpty(item.getAvatar())) {
                fullRequest.load(Constant.GAFUK_URL + item.getAvatar())
                        .thumbnail(thumbRequest.load(Constant.GAFUK_URL + item.getAvatar()))
                        .into(viewHolder.contact_avatar);
            }
        }
    }

    public void addItem(ContactItem item){
        if (items==null){
            items = new ArrayList<>();
        }
        items.add(item);
        notifyItemInserted(getItemCount());
    }

    public void addAll(Collection<ContactItem> newItems, boolean clearList){
        if (clearList)
            items.clear();

        if (clearList){
            items.addAll(newItems);
            notifyDataSetChanged();
        }else {
            final int positionStart = getItemCount() + 1;
            items.addAll(newItems);
            notifyItemRangeInserted(positionStart, newItems.size());
        }
    }

    @NonNull
    @Override
    public List<ContactItem> getPreloadItems(int position) {
        return items.subList(position, position + 1);
    }

    @Nullable
    @Override
    public RequestBuilder getPreloadRequestBuilder(ContactItem item) {
        return fullRequest.thumbnail(thumbRequest.load(item)).load(item);
    }

    public void clear() {
        items.clear();
    }

    public interface ItemClickListener {

        boolean onLongItemClick(View view, ContactItem item, int position);

        void onItemClick(View view, ContactItem item, int position);

    }

}
