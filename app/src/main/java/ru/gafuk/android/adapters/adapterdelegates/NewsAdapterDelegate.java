package ru.gafuk.android.adapters.adapterdelegates;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hannesdorfmann.adapterdelegates3.AdapterDelegate;

import java.util.List;

import ru.gafuk.android.R;
import ru.gafuk.android.api.news.models.NewsItem;
import ru.gafuk.android.settings.Preferences;
import ru.gafuk.android.utils.DateUtils;

/**
 * Created by Александр on 28.10.2017.
 */

public class NewsAdapterDelegate extends AdapterDelegate<List<NewsItem>> {

    private final DateUtils dateUtils = DateUtils.getInstance();
    private final String itemView;

    public NewsAdapterDelegate() {
        itemView = Preferences.News.getItemView();
    }

    @Override
    protected boolean isForViewType(@NonNull List<NewsItem> items, int position) {
        return items.get(position) != null;
    }

    @NonNull
    @Override
    protected RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        switch (itemView){
            case "simple":
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_simple_item, parent, false));
            case "compat":
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_compat_item, parent, false));
            case "full":
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_full_item, parent, false));
            default:
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_compat_item, parent, false));
        }
//        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_compat_item, parent, false));

    }

    @Override
    protected void onBindViewHolder(@NonNull List<NewsItem> items, int position, @NonNull RecyclerView.ViewHolder holder, @NonNull List<Object> payloads) {
        ViewHolder viewHolder = (ViewHolder) holder;
        NewsItem item = items.get(position);

        viewHolder.title.setText(item.getTitle());
        viewHolder.date_text.setText(dateUtils.formatFullDate(item.getDate().getTime()));
        viewHolder.avtor_text.setText(item.getAuthor());

        if (itemView.equals("compat")
                || itemView.equals("full")){
            viewHolder.description.setText(Html.fromHtml(item.getDescription()));
            viewHolder.viewCount_text.setText(String.valueOf(item.getHits()));
            viewHolder.comments_text.setText(String.valueOf(item.getCommentsCount()));
            viewHolder.rating_text.setText(String.valueOf(item.getRating()));
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public CardView clickContainer;
        public TextView title;
        public TextView description;
        public TextView date_text;
        public TextView avtor_text;
        public TextView viewCount_text;
        public TextView comments_text;
        public TextView rating_text;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);

            clickContainer = itemView.findViewById(R.id.card_view);

            imageView = itemView.findViewById(R.id.image_image);
            title = itemView.findViewById(R.id.primary_text);
            description = itemView.findViewById(R.id.sub_text);
            date_text = itemView.findViewById(R.id.date_text);
            avtor_text = itemView.findViewById(R.id.avtor_text);
            viewCount_text = itemView.findViewById(R.id.viewCount_text);
            comments_text = itemView.findViewById(R.id.comments_text);
            rating_text = itemView.findViewById(R.id.rating_text);
        }

    }
}
