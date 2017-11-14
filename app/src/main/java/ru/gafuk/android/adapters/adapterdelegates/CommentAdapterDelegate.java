package ru.gafuk.android.adapters.adapterdelegates;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;

import java.util.List;

import ru.gafuk.android.Constant;
import ru.gafuk.android.R;
import ru.gafuk.android.api.comments.models.Comment;
import ru.gafuk.android.utils.DateUtils;

/**
 * Created by Александр on 14.11.2017.
 */

public class CommentAdapterDelegate extends AdapterDelegate<List<Comment>> {

    DateUtils dateUtils = DateUtils.getInstance();

    @Override
    protected boolean isForViewType(@NonNull List<Comment> items, int position) {
        return items.get(position) != null;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull List<Comment> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Comment item = items.get(position);

        viewHolder.comment_user_nick.setText(item.getUser_nickname());
        viewHolder.comment_date.setText(dateUtils.formatFullDateTime(item.getDate().getTime()));
        viewHolder.comment_rating.setText(String.valueOf(item.getRating()));
        // FIXME: 14.11.2017 смайлики
        viewHolder.comment_text.setText(Html.fromHtml(item.getText().replace("/images/smilies", Constant.GAFUK_URL + "images/smilies")));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ConstraintLayout clickContainer;
        public ImageView comment_user_avatar;
        public TextView comment_user_nick;
        public TextView comment_date;
        public TextView comment_rating;
        public TextView comment_text;


        public ViewHolder(View itemView) {
            super(itemView);

            clickContainer = itemView.findViewById(R.id.comment_container);
            comment_user_avatar = itemView.findViewById(R.id.comment_user_avatar);
            comment_user_nick = itemView.findViewById(R.id.comment_user_nick);
            comment_date = itemView.findViewById(R.id.comment_date);
            comment_rating = itemView.findViewById(R.id.comment_rating);
            comment_text = itemView.findViewById(R.id.comment_text);
        }

    }
}
