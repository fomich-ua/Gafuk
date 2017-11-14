package ru.gafuk.android.api.blogs;

import android.support.annotation.Nullable;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import okhttp3.Request;
import ru.gafuk.android.Constant;
import ru.gafuk.android.api.blogs.interfaces.IBlogApi;
import ru.gafuk.android.api.blogs.models.BlogDetailsPage;
import ru.gafuk.android.api.blogs.models.BlogItem;
import ru.gafuk.android.api.comments.CommentsApi;
import ru.gafuk.android.api.rss.SaxFeedParser;
import ru.gafuk.android.api.rss.models.RssMessage;
import ru.gafuk.android.client.Client;
import ru.gafuk.android.client.NetworkResponse;
import ru.gafuk.android.settings.Preferences;
import ru.gafuk.android.utils.DateUtils;

import static ru.gafuk.android.utils.StringUtils.str2int;

/**
 * Created by Александр on 23.10.2017.
 */

public class BlogsApi implements IBlogApi{
    public static final String LOG_TAG = Constant.GAFUK_LOG_PREFIX + BlogsApi.class.getSimpleName();
    final String regex = "<div class=\"gafuk-card mdl-cell mdl-cell--12-col\">[\\s\\S]*?style=\"background-image:url\\('([^>]*?)'\\)[\\s\\S]*?<a href=\"([^\"]*?)\">([^<]*?)<\\/a>[\\s\\S]*?<div class=\"gafuk-card__description-text\">([^<]*?)<\\/div>[\\s\\S]*?<div class=\"gafuk-card__description-date\">([^<]*?)<\\/div><div class=\"gafuk-card__description-author\"><a href=\"([^\"]*?)\">([^<]*?)<\\/a>[\\s\\S]*?>comment<\\/i><span>(\\d+)<\\/span>[\\s\\S]*?visibility<\\/i>(\\d+)<\\/div>";
    final String idRegex = "\\/images\\/photos\\/small|medium\\/article(\\d+).jpg";

    final Pattern pattern = Pattern.compile(regex);
    final Pattern idPattern = Pattern.compile(idRegex);

    @Override
    public List<BlogItem> getBlogs(@Nullable int pageNumber) throws Exception {
        final String provider = Preferences.Blog.getProvider();
        switch (provider){
            case "RSS": {
                return getBlogs_Rss();
            }
            case "blog_site":{
                return getBlogs_ajax(pageNumber);
            }
            default:
                break;
        }
        return null;
    }

    private List<BlogItem>getBlogs_Rss() throws Exception{

        List<RssMessage> rssMessages = new SaxFeedParser(Constant.GAFUK_BLOGS_FEDD_URL).parse();

        final List<BlogItem> result = new ArrayList<>();

        for (RssMessage rssMessage : rssMessages) {
            BlogItem blogItem = new BlogItem();

            blogItem.setTitle(rssMessage.getTitle());
            blogItem.setUrl(rssMessage.getLink());
            blogItem.setDescription(rssMessage.getDescription());
            blogItem.setDate(rssMessage.getDate());
            blogItem.setImgUrl(rssMessage.getEnclosure());

            result.add(blogItem);
        }
        return result;
    }

    private List<BlogItem>getBlogs_ajax(int pageNumber) throws Exception{
        Log.d(LOG_TAG, "getBlogs_ajax: pageNumber=" + pageNumber);

        final DateUtils dateUtils = DateUtils.getInstance();
        String stringValue;

        ArrayList<BlogItem> items = new ArrayList<>();

        Request request = new Request.Builder()
                .url("http://gafuk.ru/blogs/latest-" + String.valueOf(pageNumber) + ".html")
                .build();

        NetworkResponse response = Client.request(request);

        if (response.getRedirect().equalsIgnoreCase(Constant.GAFUK_LOGIN_STRING)) return items; // редирект на страницу авторизации

        if (response.getBody().isEmpty()){ // возможно последняя страница
            return items;
        }

        Document document = Jsoup.parse(response.getBody(), Constant.GAFUK_URL);

        Element blog_entries = document.getElementsByClass("blog_entries").first();

        if (blog_entries == null) return items; // скорее всего последняя страница

        for (Element blog_entrie : blog_entries.getElementsByClass("gafuk-card__description")) {

            BlogItem item = new BlogItem();

            item.setUrl(blog_entrie.getElementsByClass("gafuk-card__description-title").first().getElementsByAttribute("href").first().absUrl("href"));
            item.setTitle(blog_entrie.getElementsByClass("gafuk-card__description-title").first().getElementsByTag("a").first().text());
            item.setDescription(blog_entrie.getElementsByClass("gafuk-card__description-text").first().text());

            stringValue = blog_entrie.getElementsByClass("gafuk-card__description-views").first().text().trim();
            item.setHits(str2int(stringValue));

            stringValue = blog_entrie.getElementsByClass("gafuk-rating").first().getElementsByTag("span").first().text().trim();
            item.setRating(str2int(stringValue));

            stringValue = blog_entrie.getElementsByClass("gafuk-card__description-comments").first().text().trim();
            item.setCommentsCount(str2int(stringValue));

            item.setAuthor(blog_entrie.getElementsByClass("gafuk-card__description-author").first().text().trim());
            item.setDate(dateUtils.dateFromString(blog_entrie.getElementsByClass("gafuk-card__description-date").first().text().trim()));

            items.add(item);
        }
        return items;
    }

    public BlogDetailsPage getDetails(String url) throws Exception{

        String response = Client.get(url).getBody();

        BlogDetailsPage result = parseArticle(response);

        result.setUrl(url);

        return result;
    }

    private BlogDetailsPage parseArticle(String response) throws Exception{

        final Document document = Jsoup.parse(response, "http://gafuk.ru");

        BlogDetailsPage result = new BlogDetailsPage();

//        document.getElementsByClass("content-image").first().getElementsByTag("img").first().absUrl("src"));

        Element content;
        Elements elements = document.getElementsByClass("blog-text");

        if (elements.size() > 1){
            content = elements.get(1);
        }else {
            content = elements.first();
        }


        Element element = document.getElementsByClass("blog-details").first();

        if (element != null){
            result.setDate(element.child(0).text());
            result.setAuthor(element.child(1).getElementsByTag("a").first().text());
            element.remove();
        }

//        element = content.getElementsByClass("content-image").first();
//
//        if (element != null){
//            result.setImgUrl(element.getElementsByTag("img").first().absUrl("src"));
//            result.setTitle(element.getElementsByTag("img").first().attr("alt"));
//
//            element.remove();
//        }

        element = content.getElementsByClass("content-source").first();
        if (element != null){
            element.remove();
        }

        result.setComments(CommentsApi.parseComments(document));

        result.setHtml(response);
        result.setDescription(
                "<!DOCTYPE html>\n" +
                        "<html>\n" +
                        "<head>" +
                        "<style>" +
                        "img {\n" +
                        "    border:0 none;\n" +
                        "    display:inline-block;height:auto !important;\n" +
                        "    max-width:100%;\n" +
                        "}" +
                        ".bb_tag_video {position:relative;padding-bottom:56.25%;height:0;overflow:hidden}\n" +
                        ".bb_tag_video iframe,\n" +
                        ".bb_tag_video object,\n" +
                        ".bb_tag_video embed {position:absolute;top:0;left:0;width:100%;height:100%}\n" +
//                        ".embeddedContent {position:relative;padding-bottom:56.25%;padding-top:30px;height:0;overflow:hidden}\n" +
//                        ".embeddedContent iframe {position:absolute;top:0;left:0;width:100%;height:100%}\n" +
                        "</style>\n" +
                        "</head>\n" +
                        "<body>" +
                        content.html() +
                        "</body>\n" +
                        "</html>");

        return result;
    }
}
