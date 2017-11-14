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

import ru.gafuk.android.R;
import ru.gafuk.android.api.contacts.models.Message;
import ru.gafuk.android.utils.DateUtils;

/**
 * Created by Александр on 30.10.2017.
 */

public class ChatAdapterDelegateSent extends AdapterDelegate<List<Message>> {

    private final DateUtils dateUtils = DateUtils.getInstance();

    public ChatAdapterDelegateSent() {
    }

    @Override
    protected boolean isForViewType(@NonNull List<Message> items, int position) {
        return items.get(position)!=null;
    }

    @Override
    protected void onBindViewHolder(@NonNull List<Message> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Message item = items.get(position);

        viewHolder.text_message_body.setText(Html.fromHtml(item.getMessage()));
        viewHolder.text_message_time.setText(dateUtils.formatDateTime(item.getSenddate().getTime()));
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView text_message_body;
        public TextView text_message_time;

        public ViewHolder(View itemView) {
            super(itemView);

            text_message_body = itemView.findViewById(R.id.text_message_body);
            text_message_time = itemView.findViewById(R.id.text_message_time);
        }
    }
}
