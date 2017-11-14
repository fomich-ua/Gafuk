package ru.gafuk.android.rxapi.apiclasses;

import java.util.List;

import io.reactivex.Observable;
import ru.gafuk.android.api.Api;
import ru.gafuk.android.api.news.models.NewsDetailsPage;
import ru.gafuk.android.api.news.models.NewsItem;

/**
 * Created by Александр on 21.09.2017.
 */

public class NewsRx {

    public Observable<List<NewsItem>> getNews(int pageNumber) {
        return Observable.fromCallable(() -> Api.NewsApi().getNews(pageNumber));
    }

    public Observable<NewsItem> getNewsItems(int pageNumber){
        return Observable.fromCallable(() -> Api.NewsApi().getNews(pageNumber))
                .flatMap(Observable::fromIterable);
    }
    public Observable<NewsDetailsPage> getDetails(final String url) {
        return Observable.fromCallable(() -> transform(Api.NewsApi().getDetails(url)));
    }

    public static NewsDetailsPage transform(NewsDetailsPage page) throws Exception {
//        MiniTemplator t = App.getInstance().getTemplate(App.TEMPLATE_NEWS);
//        App.setTemplateResStrings(t);
//        t.setVariableOpt("style_type", App.getInstance().getCssStyleType());
//        t.setVariableOpt("details_title", StringUtils.htmlEncode(page.getTitle()));
//        t.setVariableOpt("body_type", "news");
//        t.setVariableOpt("details_content", page.getHtml());
//        for (Material material : page.getMaterials()) {
//            t.setVariableOpt("material_id", material.getId());
//            t.setVariableOpt("material_image", material.getImageUrl());
//            t.setVariableOpt("material_title", material.getTitle());
//            t.addBlockOpt("material");
//        }
//        page.setHtml(t.generateOutput());
//        t.reset();
        return page;
    }
}
