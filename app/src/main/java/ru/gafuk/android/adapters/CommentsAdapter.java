package ru.gafuk.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.gafuk.android.adapters.adapterdelegates.CommentAdapterDelegate;
import ru.gafuk.android.api.comments.models.Comment;

/**
 * Created by Александр on 14.11.2017.
 */

public class CommentsAdapter extends ListDelegationAdapter<List<Comment>> {

    private CommentsAdapter.ItemClickListener mItemClickListener;

    public interface ItemClickListener {

        boolean onLongItemClick(View view, Comment item, int position);

        void onItemClick(View view, Comment item, int position);

    }

    public CommentsAdapter() {
        delegatesManager.addDelegate(new CommentAdapterDelegate());
        items = new ArrayList<>();
    }

    public CommentsAdapter(ItemClickListener mItemClickListener) {
        this();
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);

        CommentAdapterDelegate.ViewHolder viewHolder = (CommentAdapterDelegate.ViewHolder) holder;

        if (viewHolder!=null) {
            viewHolder.clickContainer.setOnClickListener(v -> mItemClickListener.onItemClick(viewHolder.clickContainer, items.get(viewHolder.getLayoutPosition()), viewHolder.getLayoutPosition()));
            viewHolder.clickContainer.setOnLongClickListener(v -> mItemClickListener.onLongItemClick(viewHolder.clickContainer, items.get(viewHolder.getLayoutPosition()), viewHolder.getLayoutPosition()));
        }

        return holder;
    }

    public void addAll(Collection<Comment> blogItems, boolean clearList){

        if (clearList){
            items.clear();
            items.addAll(blogItems);
            notifyDataSetChanged();
        }else {
            final int positionStart = getItemCount() + 1;
            items.addAll(blogItems);
            notifyItemRangeInserted(positionStart, blogItems.size());
        }
    }

    public void clear() {
        items.clear();
    }
}
