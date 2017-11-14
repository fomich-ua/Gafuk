package ru.gafuk.android.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Александр on 22.10.2017.
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public void bind(T item, int section) {
    }

    public void bind(T item) {
    }

    public void bind(int position) {
    }

    public void bind() {
    }
}
