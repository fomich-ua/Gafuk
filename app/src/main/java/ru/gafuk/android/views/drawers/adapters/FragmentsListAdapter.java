package ru.gafuk.android.views.drawers.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.gafuk.android.R;
import ru.gafuk.android.fragments.BaseFragment;
import ru.gafuk.android.fragments.TabManager;

/**
 * Created by Александр on 19.09.2017.
 */

public class FragmentsListAdapter extends RecyclerView.Adapter<FragmentsListAdapter.ViewHolder> {

    private int color = Color.argb(48, 128, 128, 128);

    private FragmentsListAdapter.OnItemClickListener itemClickListener;
    private FragmentsListAdapter.OnItemClickListener closeClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setCloseClickListener(OnItemClickListener closeClickListener) {
        this.closeClickListener = closeClickListener;
    }

    public BaseFragment getItem(int position){
        return TabManager.getInstance().get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_tab_item, parent, false);
        return new FragmentsListAdapter.ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return TabManager.getInstance().getSize();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BaseFragment fragment = TabManager.getInstance().get(position);
        if (position == TabManager.getActiveIndex())
            holder.itemView.setBackgroundColor(color);
        else
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);

        holder.text.setText(fragment.getTabTitle());
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView text;
        public ImageView close;

        public ViewHolder(View v) {
            super(v);
            text = (TextView) v.findViewById(R.id.drawer_item_title);
            close = (ImageView) v.findViewById(R.id.drawer_item_close);

            v.setOnClickListener(this);
            close.setOnClickListener(v1 -> {
                if (closeClickListener != null) {
                    closeClickListener.onItemClick(getItem(getLayoutPosition()), getLayoutPosition());
                }
            });
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getItem(getLayoutPosition()), getLayoutPosition());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(BaseFragment tabFragment, int position);
    }
}
