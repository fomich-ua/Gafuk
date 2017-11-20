package ru.gafuk.android.api.dialogs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import ru.gafuk.android.Constant;
import ru.gafuk.android.api.dialogs.models.Dialog;
import ru.gafuk.android.client.Client;
import ru.gafuk.android.client.NetworkResponse;

/**
 * Created by aborz on 18.11.2017.
 */

public class DialogsApi {

    public List<Dialog> getDialogs() throws Exception {
        RequestBody formBody = new FormBody.Builder()
                .add("act", "getContacts")
                .add("recipient_id", "0")
                .build();
        Request request = new Request.Builder()
                .url(Constant.GAFUK_MESSENGER)
                .post(formBody)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .build();

        NetworkResponse response = Client.request(request);

        if (response.getBody().isEmpty()) return new ArrayList<>();

        final JsonObject jsonObject = (JsonObject) new JsonParser().parse(response.getBody());
        final JsonArray jsonContacts = jsonObject.getAsJsonArray("contacts");

        final Gson gson = new GsonBuilder().create();

        return gson.fromJson(jsonContacts, new TypeToken<ArrayList<Dialog>>() {}.getType());
    }
}
