package ru.gafuk.android.api.contacts;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import ru.gafuk.android.Constant;
import ru.gafuk.android.api.contacts.interfaces.IContactsApi;
import ru.gafuk.android.api.contacts.models.ContactItem;
import ru.gafuk.android.api.contacts.models.MessageContacts;
import ru.gafuk.android.client.Client;
import ru.gafuk.android.client.NetworkResponse;

/**
 * Created by Александр on 25.10.2017.
 */

public class ContactsApi implements IContactsApi {

    public static final String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    @Override
    public List<ContactItem> getContacts() throws Exception {

        // для корректной работы сообщений - необходим полный логин
//        RequestBody formBody1 = new FormBody.Builder()
//                .add("act", "getNewMessages")
//                .build();
//
//        Request request1 = new Request.Builder()
//                .url(Constant.GAFUK_LOGIN_STRING)
//                .build();
//
//        NetworkResponse response1 = Client.request(request1);

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

        return gson.fromJson(jsonContacts, new TypeToken<ArrayList<ContactItem>>() {}.getType());
    }

    @Override
    public ArrayList<MessageContacts>getContactMessages(int contact_id) throws Exception{

        RequestBody formBody = new FormBody.Builder()
                .add("act", "getMessages")
                .add("contact_id", Integer.toString(contact_id))
                .build();

        Request request = new Request.Builder()
                .url(Constant.GAFUK_MESSENGER)
                .post(formBody)
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .build();

        NetworkResponse response = Client.request(request);

        if (response.getBody().isEmpty()) return new ArrayList<>();

        JsonObject jsonObject = (JsonObject) new JsonParser().parse(response.getBody());
        JsonArray jsonMessages = jsonObject.getAsJsonArray("messages");

        Type type = new TypeToken<ArrayList<MessageContacts>>() {}.getType();

        final Gson gson = new GsonBuilder()
                .setDateFormat(dateTimeFormat)
                .create();

        return gson.fromJson(jsonMessages, type);
    }
}
