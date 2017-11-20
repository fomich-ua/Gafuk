package ru.gafuk.android.adapters.adapterdelegates;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.gafuk.android.R;
import ru.gafuk.android.api.contacts.models.MessageContacts;
import ru.gafuk.android.utils.DateUtils;

/**
 * Created by Александр on 30.10.2017.
 */

public class ChatAdapterDelegateReceived extends AdapterDelegate<List<MessageContacts>> {

    private String contact_nick;
    private final DateUtils dateUtils = DateUtils.getInstance();

    public ChatAdapterDelegateReceived() {
    }

    public ChatAdapterDelegateReceived(String contact_nick) {
        this();
        this.contact_nick = contact_nick;
    }

    @Override
    protected boolean isForViewType(@NonNull List<MessageContacts> items, int position) {
        return items.get(position)!=null;
    }

    @Override
    protected void onBindViewHolder(@NonNull List<MessageContacts> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        ViewHolder viewHolder = (ViewHolder) holder;
        MessageContacts item = items.get(position);

        viewHolder.text_message_name.setText(contact_nick);
        viewHolder.text_message_body.setText(Html.fromHtml(item.getMessage()));
        viewHolder.text_message_time.setText(dateUtils.formatDateTime(item.getSenddate().getTime()));
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView image_message_profile;
        public TextView text_message_name;
        public TextView text_message_body;
        public TextView text_message_time;

        public ViewHolder(View itemView) {
            super(itemView);

            image_message_profile = itemView.findViewById(R.id.image_message_profile);

            text_message_name = itemView.findViewById(R.id.text_message_name);
            text_message_body = itemView.findViewById(R.id.text_message_body);
            text_message_time = itemView.findViewById(R.id.text_message_time);
        }
    }
}
