package ru.gafuk.android.adapters;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.gafuk.android.Constant;
import ru.gafuk.android.adapters.adapterdelegates.ChatAdapterDelegateReceived;
import ru.gafuk.android.adapters.adapterdelegates.ChatAdapterDelegateSent;
import ru.gafuk.android.api.contacts.models.Message;
import ru.gafuk.android.glide.GlideRequest;
import ru.gafuk.android.glide.GlideRequests;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * Created by Александр on 29.10.2017.
 */

public class ChatAdapter extends ListDelegationAdapter<List<Message>>
        implements ListPreloader.PreloadModelProvider<Message>{

    public static final int FROM_MESSAGE = 1;
    public static final int TO_MESSAGE = 2;

    private int contact_id;
    private String contact_nick;
    private String contact_avatar;

    private GlideRequest<Drawable> fullRequest;
    private GlideRequest<Drawable> thumbRequest;

    private ChatAdapter.ItemClickListener mItemClickListener;

    private ChatAdapter() {
        this.delegatesManager.addDelegate(FROM_MESSAGE, new ChatAdapterDelegateReceived(contact_nick));
        this.delegatesManager.addDelegate(TO_MESSAGE, new ChatAdapterDelegateSent());

        items = new ArrayList<>();
    }

    private ChatAdapter(int contact_id, String contact_nick, String contact_avatar) {
        this();

        this.contact_id = contact_id;
        this.contact_nick = contact_nick;
        this.contact_avatar = contact_avatar;
    }

    public ChatAdapter(int contact_id, String contact_nick, String contact_avatar, ItemClickListener mItemClickListener, GlideRequests glideRequests) {

        this(contact_id, contact_nick, contact_avatar);

        this.mItemClickListener = mItemClickListener;
        fullRequest = glideRequests
                .asDrawable()
                .centerCrop()
                .placeholder(new ColorDrawable(Color.GRAY));

        thumbRequest = glideRequests
                .asDrawable()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transition(withCrossFade());
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position).getFrom_id() == contact_id){
            return FROM_MESSAGE;
        }else if (items.get(position).getTo_id() == contact_id){
            return TO_MESSAGE;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);

        if (holder instanceof ChatAdapterDelegateReceived.ViewHolder){
            ChatAdapterDelegateReceived.ViewHolder viewHolder = (ChatAdapterDelegateReceived.ViewHolder) holder;

            if (!TextUtils.isEmpty(contact_avatar)) {
                fullRequest.load(Constant.GAFUK_URL + contact_avatar)
                        .thumbnail(thumbRequest.load(Constant.GAFUK_URL + contact_avatar))
                        .into(viewHolder.image_message_profile);
            }
        }
    }

    public void addItem(Message item){
        if (items==null){
            items = new ArrayList<>();
        }
        items.add(item);
        notifyItemInserted(getItemCount());
    }

    public void addAll(Collection<Message> newItems, boolean clearList){
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
    public List<Message> getPreloadItems(int position) {
        return items.subList(position, position + 1);
    }

    @Nullable
    @Override
    public RequestBuilder getPreloadRequestBuilder(Message item) {
        return fullRequest.thumbnail(thumbRequest.load(item)).load(item);
    }

    public void clear() {
        items.clear();
    }

    public interface ItemClickListener {

        boolean onLongItemClick(View view, Message item, int position);

        void onItemClick(View view, Message item, int position);

    }
}
