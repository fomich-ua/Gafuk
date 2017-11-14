package ru.gafuk.android.rxapi.apiclasses;

import java.util.List;

import io.reactivex.Observable;
import ru.gafuk.android.api.Api;
import ru.gafuk.android.api.blogs.models.BlogDetailsPage;
import ru.gafuk.android.api.blogs.models.BlogItem;

/**
 * Created by Александр on 25.10.2017.
 */

public class BlogsRx {

    public Observable<List<BlogItem>> getBlogs(int pageNumber) {
        return Observable.fromCallable(() -> Api.BlogsApi().getBlogs(pageNumber));
    }

    public Observable<BlogDetailsPage> getDetails(final String url) {
        return Observable.fromCallable(() -> transform(Api.BlogsApi().getDetails(url)));
    }

    public static BlogDetailsPage transform(BlogDetailsPage page) throws Exception {
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
