package ru.gafuk.android.rxapi.apiclasses;

import java.util.List;

import io.reactivex.Observable;
import ru.gafuk.android.api.Api;
import ru.gafuk.android.api.dialogs.models.Dialog;

/**
 * Created by aborz on 21.11.2017.
 */

public class DialogsRx {

    public Observable<List<Dialog>> getDialogs() {
        return Observable.fromCallable(() -> Api.DialogsApi().getDialogs());
    }

}
