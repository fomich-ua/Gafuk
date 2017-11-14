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

import ru.gafuk.android.R;
import ru.gafuk.android.api.contacts.models.ContactItem;

/**
 * Created by Александр on 28.10.2017.
 */

public class ContactsAdapterDelegate extends AdapterDelegate<List<ContactItem>> {

    public ContactsAdapterDelegate() {
    }

    @Override
    protected boolean isForViewType(@NonNull List<ContactItem> items, int position) {
        return items.get(position) != null;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_contact_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<ContactItem> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ContactItem item = items.get(position);

        viewHolder.contact_nick.setText(item.getNickname());
        viewHolder.contact_count.setText(String.valueOf(item.getCount()));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView clickContainer;
        public TextView contact_nick;
        public TextView contact_lastMessage;
        public TextView contact_count;
        public ImageView contact_avatar;

        public ViewHolder(View itemView) {
            super(itemView);

            clickContainer = itemView.findViewById(R.id.card_view);

            contact_nick = itemView.findViewById(R.id.contact_nick);
            contact_count = itemView.findViewById(R.id.contact_count);
            contact_lastMessage = itemView.findViewById(R.id.contact_lastMessage);
            contact_avatar = itemView.findViewById(R.id.contact_avatar);
        }

    }
}
