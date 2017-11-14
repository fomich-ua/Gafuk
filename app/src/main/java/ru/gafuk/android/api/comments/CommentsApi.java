package ru.gafuk.android.api.comments;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;

import ru.gafuk.android.api.comments.interfaces.ICommentsApi;
import ru.gafuk.android.api.comments.models.Comment;
import ru.gafuk.android.utils.DateUtils;
import ru.gafuk.android.utils.StringUtils;

/**
 * Created by Александр on 14.11.2017.
 */

public class CommentsApi implements ICommentsApi {

    @Override
    public String addComment(String target, int target_id, int parent_id) {
        return null;
    }

    @Override
    public String editComment(int comment_id, String csrf_token) {
        return null;
    }

    @Override
    public String deleteComment(int comment_id, String csrf_token) {
        return null;
    }

    @Override
    public String loadComments(String target, int target_id) {
        return null;
    }

    @Override
    public String voteComment(int comment_id, int vote) {
        return null;
    }

    public static ArrayList<Comment> parseComments(Document document){

        final DateUtils dateUtils = DateUtils.getInstance();

        ArrayList<Comment> result = new ArrayList<>();

        Elements elements = document.getElementsByClass("cm_ajax_list").first().getElementsByTag("table");

        for (Element element : elements){
            if (!element.hasAttr("id")) continue;

            final int id = StringUtils.str2int(element.attr("id").replace("c", ""));
            final int level = element.getElementsByClass("cmm_border").size();
            final Date date = dateUtils.commentsDateFromString(element.getElementsByClass("cmm_date").first().text());
            final int rating = StringUtils.str2int(element.getElementsByClass("gafuk-rating").first().text());
            final String user_avatar = element.getElementsByClass("usr_img_small").first().absUrl("src");
            final String user_nickname = element.getElementsByClass("cmm_author").first().text();
            final String user_url = element.getElementsByClass("cmm_author").first().child(0).absUrl("href");
            final String text = element.getElementsByClass("cmm_text").first().text();

            result.add(new Comment(
                    id,
                    level,
                    date,
                    rating,
                    user_avatar,
                    user_nickname,
                    user_url,
                    text)
            );
        }

        return result;
    }
}
