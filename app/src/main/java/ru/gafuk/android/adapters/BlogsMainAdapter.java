package ru.gafuk.android.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.gafuk.android.adapters.adapterdelegates.BlogsAdapterDelegate;
import ru.gafuk.android.api.blogs.models.BlogItem;

/**
 * Created by Александр on 28.10.2017.
 */

public class BlogsMainAdapter extends ListDelegationAdapter<List<BlogItem>> {

    private BlogsMainAdapter.ItemClickListener mItemClickListener;

    public BlogsMainAdapter() {
        this.delegatesManager.addDelegate(new BlogsAdapterDelegate());
        items = new ArrayList<>();
    }

    public BlogsMainAdapter(ItemClickListener mItemClickListener) {
        this();
        this.mItemClickListener = mItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);

        BlogsAdapterDelegate.ViewHolder viewHolder = (BlogsAdapterDelegate.ViewHolder) holder;

        if (viewHolder!=null) {
            viewHolder.clickContainer.setOnClickListener(v -> mItemClickListener.onItemClick(viewHolder.clickContainer, items.get(viewHolder.getLayoutPosition()), viewHolder.getLayoutPosition()));
            viewHolder.clickContainer.setOnLongClickListener(v -> mItemClickListener.onLongItemClick(viewHolder.clickContainer, items.get(viewHolder.getLayoutPosition()), viewHolder.getLayoutPosition()));
        }
        return holder;
    }

    public void addItem(BlogItem blogItem){
        if (items==null){
            items = new ArrayList<>();
        }
        items.add(blogItem);
        notifyItemInserted(getItemCount());
    }

    public void addAll(Collection<BlogItem> blogItems, boolean clearList){
        if (clearList)
            items.clear();

        if (clearList){
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

    public interface ItemClickListener {

        boolean onLongItemClick(View view, BlogItem item, int position);

        void onItemClick(View view, BlogItem item, int position);

    }

}
