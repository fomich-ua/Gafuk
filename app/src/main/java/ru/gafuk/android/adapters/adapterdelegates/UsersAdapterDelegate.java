package ru.gafuk.android.adapters.adapterdelegates;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;

import java.util.List;

import ru.gafuk.android.App;
import ru.gafuk.android.R;
import ru.gafuk.android.api.users.models.UserItem;

/**
 * Created by Александр on 28.10.2017.
 */

public class UsersAdapterDelegate extends AdapterDelegate<List<UserItem>> {

    public UsersAdapterDelegate() {
    }

    @Override
    protected boolean isForViewType(@NonNull List<UserItem> items, int position) {
        return items.get(position) != null;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<UserItem> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        ViewHolder viewHolder = (ViewHolder) holder;
        UserItem item = items.get(position);

        viewHolder.user_nick.setText(item.getNickname());
        viewHolder.user_reputation.setText(String.format(App.getContext().getString(R.string.karma_caption_user), String.valueOf(item.getReputation())));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView clickContainer;
        public TextView user_nick;
        public TextView user_reputation;
        public ImageView user_avatar;

        public ViewHolder(View itemView) {
            super(itemView);

            clickContainer = itemView.findViewById(R.id.card_view);

            user_nick = itemView.findViewById(R.id.user_nick);
            user_reputation = itemView.findViewById(R.id.user_reputation);
            user_avatar = itemView.findViewById(R.id.user_avatar);
        }

    }
}
