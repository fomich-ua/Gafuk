package ru.gafuk.android.api.users;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import ru.gafuk.android.Constant;
import ru.gafuk.android.api.users.interfaces.IUsersApi;
import ru.gafuk.android.api.users.models.SearchUsersResult;
import ru.gafuk.android.api.users.models.UserItem;
import ru.gafuk.android.client.Client;
import ru.gafuk.android.client.NetworkResponse;
import ru.gafuk.android.utils.StringUtils;

/**
 * Created by Александр on 27.10.2017.
 */

public class UsersApi implements IUsersApi {

    @Override
    public List<UserItem> getUsers(int pageNumber) throws Exception{

        ArrayList<UserItem> userItems = new ArrayList<>();

        Request request = new Request.Builder()
                .url("http://gafuk.ru/users/rating" + String.valueOf(pageNumber) + ".html")
                .header("Referer", (pageNumber == 1) ? Constant.GAFUK_USERS : "http://gafuk.ru/users/rating" + String.valueOf(pageNumber - 1) + ".html")
                .build();

        NetworkResponse response = Client.request(request);
        //        NetworkResponse response = Client.get("http://gafuk.ru/users/rating" + String.valueOf(pageNumber) + ".html");

        Document document = Jsoup.parse(response.getBody(), Constant.GAFUK_URL);

        Element listElements = document.getElementsByClass("users-list").first();
        Elements elements = listElements.getElementsByClass("users-list-item");

        for (Element element : elements) {

            UserItem userItem = new UserItem();

            userItem.setSmallAvatar(element.getElementsByClass("users-list-avatar").first().getElementsByTag("img").first().absUrl("src"));
            userItem.setNickname(element.getElementsByClass("users-list-nickname").first().getElementsByAttribute("title").first().attr("title"));
            userItem.setReputation(StringUtils.str2int(element.getElementsByClass("users-list-count").first().text()));

            userItems.add(userItem);
        };

        return userItems;
    }

    public List<UserItem> cancelSearch() throws Exception{
        ArrayList<UserItem> userItems = new ArrayList<>();

        NetworkResponse response = Client.get(Constant.GAFUK_USERS_ALL);

        Document document = Jsoup.parse(response.getBody(), Constant.GAFUK_URL);

        Element listElements = document.getElementsByClass("users-list").first();
        Elements elements = listElements.getElementsByClass("users-list-item");

        for (Element element : elements) {

            UserItem userItem = new UserItem();

            userItem.setSmallAvatar(element.getElementsByClass("users-list-avatar").first().getElementsByTag("img").first().absUrl("src"));
            userItem.setNickname(element.getElementsByClass("users-list-nickname").first().getElementsByAttribute("title").first().attr("title"));
            userItem.setReputation(StringUtils.str2int(element.getElementsByClass("users-list-count").first().text()));

            userItems.add(userItem);
        };

        return userItems;
    }

    public SearchUsersResult getSearchResult(String nickname) throws Exception{

        ArrayList<UserItem> userItems = new ArrayList<>();

        RequestBody formBody = new FormBody.Builder()
                .add("name", nickname)
                .add("city", "")
                .add("city_id", "0")
                .add("region_id", "0")
                .add("country_id", "0")
                .add("gosearch", "")
                .build();

        Request request = new Request.Builder()
                .url(Constant.GAFUK_USERS)
                .post(formBody)
                .build();

        NetworkResponse response = Client.request(request);

        Document document = Jsoup.parse(response.getBody(), Constant.GAFUK_URL);

        // заполнение списка пользователей
        Element listUsersElements;
        Elements elements;

        if (document != null
                && document.getElementsByClass("users-list") != null) {

            listUsersElements = document.getElementsByClass("users-list").first();
            elements = listUsersElements.getElementsByClass("users-list-item");

            for (Element element : elements) {

                UserItem userItem = new UserItem();

                userItem.setSmallAvatar(element.getElementsByClass("users-list-avatar").first().getElementsByTag("img").first().absUrl("src"));
                userItem.setNickname(element.getElementsByClass("users-list-nickname").first().getElementsByAttribute("title").first().attr("title"));
                userItem.setReputation(StringUtils.str2int(element.getElementsByClass("users-list-count").first().text()));

                userItems.add(userItem);

            };
        }

        elements = document.getElementsByClass("gafuk-pagination__page");

        final int totalPages;
        if (elements == null
                || elements.size() == 0){
            totalPages = 1;
        }else {
            totalPages = Integer.valueOf(elements.last().text());
        }

        return new SearchUsersResult(userItems, totalPages);

    }

}
