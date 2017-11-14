package ru.gafuk.android.api.news;

import android.support.annotation.Nullable;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.Request;
import ru.gafuk.android.Constant;
import ru.gafuk.android.api.comments.CommentsApi;
import ru.gafuk.android.api.news.interfaces.INewsApi;
import ru.gafuk.android.api.news.models.NewsDetailsPage;
import ru.gafuk.android.api.news.models.NewsItem;
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

public class NewsApi implements INewsApi {
    public static final String LOG_TAG = Constant.GAFUK_LOG_PREFIX + NewsApi.class.getSimpleName();

    final String regex = "<div class=\"gafuk-card mdl-cell mdl-cell--12-col\">[\\s\\S]*?style=\"background-image:url\\('([^>]*?)'\\)[\\s\\S]*?<a href=\"([^\"]*?)\">([^<]*?)<\\/a>[\\s\\S]*?<div class=\"gafuk-card__description-text\">([^<]*?)<\\/div>[\\s\\S]*?<div class=\"gafuk-card__description-date\">([^<]*?)<\\/div><div class=\"gafuk-card__description-author\"><a href=\"([^\"]*?)\">([^<]*?)<\\/a>[\\s\\S]*?>comment<\\/i><span>(\\d+)<\\/span>[\\s\\S]*?visibility<\\/i>(\\d+)<\\/div>";
//    final String idRegex = "\\/images\\/photos\\/small|medium\\/article(\\d+).jpg";
    final String idRegex = "\\/images\\/photos\\/(small|medium)\\/article(\\d+[^.]).(jpg|png)";

    final Pattern pattern = Pattern.compile(regex);
    final Pattern idPattern = Pattern.compile(idRegex);

    @Override
    public List<NewsItem> getNews(@Nullable int pageNumber) throws Exception {
        final String provider = Preferences.News.getProvider();
        switch (provider){
            case "RSS": {
                return getNews_Rss();
            }
            case "news_site":{
                return getNews_ajax(pageNumber);
            }
            default:
                break;
        }
        return null;
    }

    private List<NewsItem>getNews_Rss() throws Exception{

        Log.d(LOG_TAG, "getNews_Rss: ");
        List<RssMessage> rssMessages = new SaxFeedParser(Constant.GAFUK_NEWS_FEDD_URL).parse();

        final List<NewsItem> result = new ArrayList<>();

        for (RssMessage rssMessage : rssMessages) {
            NewsItem newsItem = new NewsItem();

            newsItem.setTitle(rssMessage.getTitle());
            newsItem.setUrl(rssMessage.getLink());
            newsItem.setDescription(rssMessage.getDescription());
            newsItem.setDate(rssMessage.getDate());
            newsItem.setImgUrl(rssMessage.getEnclosure());

            result.add(newsItem);
        }
        return result;
    }

    private List<NewsItem>getNews_ajax(int pageNumber) throws Exception{
        final boolean highImageQuality = true;
        final Pattern imgPattern = Pattern.compile("\'.*\'");

        final DateUtils dateUtils = DateUtils.getInstance();
        String stringValue;

        Log.d(LOG_TAG, "getNews_ajax: pageNumber=" + pageNumber);

        ArrayList<NewsItem> items = new ArrayList<>();

        Request request = new Request.Builder()
                .url(Constant.GAFUK_AJAX_LATEST)
                .header("X-Requested-With", "XMLHttpRequest")
                .post(new FormBody.Builder()
                        .add("module_id", "19")
                        .add("page", String.valueOf(pageNumber))
                        .build())
                .build();

        NetworkResponse response = Client.request(request);

        Document document = Jsoup.parse(response.getBody(), Constant.GAFUK_URL);

        for (Element gafuk_card : document.getElementsByClass("gafuk-card")) {

            NewsItem item = new NewsItem();
            item.setUrl(gafuk_card.getElementsByClass("gafuk-card__description-title").first().getElementsByAttribute("href").first().absUrl("href"));
            item.setTitle(gafuk_card.getElementsByClass("gafuk-card__description-title").first().getElementsByAttribute("href").first().text().trim());

            Matcher matcher = imgPattern.matcher(gafuk_card.getElementsByClass("gafuk-card__image").first().attr("style"));
            if (matcher.find()){
                item.setImgUrl(Constant.GAFUK_URL + (highImageQuality ? matcher.group(0).replace("/small/", "/medium/") : matcher.group(0)).replace("\'", ""));
            }else {
                item.setImgUrl("");
            };

            stringValue = gafuk_card.getElementsByClass("gafuk-card__description-views").first().text().trim();
            item.setHits(str2int(stringValue));

            stringValue = gafuk_card.getElementsByClass("gafuk-rating").first().getElementsByTag("span").first().text().trim();
            item.setRating(str2int(stringValue));

            stringValue = gafuk_card.getElementsByClass("gafuk-card__description-comments").first().text().trim();
            item.setCommentsCount(str2int(stringValue));

            item.setAuthor(gafuk_card.getElementsByClass("gafuk-card__description-author").first().text().trim());
            item.setDate(dateUtils.dateFromString(gafuk_card.getElementsByClass("gafuk-card__description-date").first().text().trim()));

            item.setDescription(gafuk_card.getElementsByClass("gafuk-card__description-text").first().text());

            items.add(item);
        }
//        final Matcher matcher = pattern.matcher(response.getBody());
//
//        while (matcher.find()) {
//
//            NewsItem item = new NewsItem();
//
//            item.setUrl(Constant.GAFUK_URL + matcher.group(2));
////            item.setId(Integer.parseInt(matcher.group(11)));
//            item.setImgUrl(Constant.GAFUK_URL + matcher.group(1));
//            item.setTitle(matcher.group(3));
//            item.setCommentsCount(Integer.parseInt(matcher.group(8)));
//            item.setDate(DateUtils.dateFromString(matcher.group(5)));
//            item.setAuthor(matcher.group(7));
//            item.setAuthorRef(Constant.GAFUK_URL + matcher.group(6));
//            item.setDescription(matcher.group(4).trim());
//
//            final Matcher matcherId = idPattern.matcher(item.getImgUrl());
//            if (matcherId.find()){
//                if (matcherId.group(2)!= null)
//                    item.setId(Integer.parseInt(matcherId.group(2)));
//            }
//
//            items.add(item);
//        };

        return items;
    }

    public NewsDetailsPage getDetails(String url) throws Exception{

        String response = Client.get(url).getBody();

        NewsDetailsPage result = parseArticle(response);

        result.setUrl(url);

        return result;
    }

    private NewsDetailsPage parseArticle(String response) throws Exception{

        final Document document = Jsoup.parse(response, "http://gafuk.ru");

        NewsDetailsPage result = new NewsDetailsPage();

//        document.getElementsByClass("content-image").first().getElementsByTag("img").first().absUrl("src"));

        Element content = document.getElementsByClass("content-text").first();

        Element element = document.getElementsByClass("content-details").first();

        if (element != null){
            result.setDate(element.child(0).text());
//            result.setAuthor(element.child(1).getElementsByTag("a").first().absUrl("href"));
            result.setAuthor(element.child(1).getElementsByTag("a").first().text());
            element.remove();
        }

        element = content.getElementsByClass("content-image").first();

        if (element != null){
            result.setImgUrl(element.getElementsByTag("img").first().absUrl("src"));
            result.setTitle(element.getElementsByTag("img").first().attr("alt"));

            element.remove();
        }

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
                        ".embeddedContent {position:relative;padding-bottom:56.25%;padding-top:30px;height:0;overflow:hidden}\n" +
		                ".embeddedContent iframe {position:absolute;top:0;left:0;width:100%;height:100%}\n" +
                        "</style>\n" +
                        "</head>\n" +
                        "<body>" +
                        content.html() +
                        "</body>\n" +
                        "</html>");

        return result;
    }

}
