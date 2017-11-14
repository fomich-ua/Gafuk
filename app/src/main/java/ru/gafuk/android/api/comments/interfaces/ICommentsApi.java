package ru.gafuk.android.api.comments.interfaces;

/**
 * Created by Александр on 14.11.2017.
 */

public interface ICommentsApi {

    String addComment(String target, int target_id, int parent_id);

    String editComment(int comment_id, String csrf_token);

    String deleteComment(int comment_id, String csrf_token);

    String loadComments(String target, int target_id);

    String voteComment(int comment_id, int vote);

}
