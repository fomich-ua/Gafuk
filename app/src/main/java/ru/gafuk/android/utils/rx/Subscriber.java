package ru.gafuk.android.utils.rx;

import android.support.annotation.NonNull;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ru.gafuk.android.fragments.BaseFragment;
import ru.gafuk.android.utils.ErrorHandler;

/**
 * Created by Александр on 24.10.2017.
 */

public class Subscriber<T> {
    private BaseFragment fragment;

    public Subscriber() {
    }

    public Subscriber(BaseFragment fragment) {
        this.fragment = fragment;
    }

    private void handleErrorRx(Throwable throwable) {
        handleErrorRx(throwable, null);
    }

    private void handleErrorRx(Throwable throwable, View.OnClickListener listener) {
        ErrorHandler.handle(fragment, throwable, listener);
    }

    public Disposable subscribe(@NonNull Observable<T> observable, @NonNull Consumer<T> onNext, @NonNull T onErrorReturn) {
        return subscribe(observable, onNext, onErrorReturn, null);
    }

    public Disposable subscribe(@NonNull Observable<T> observable, @NonNull Consumer<T> onNext, @NonNull T onErrorReturn, View.OnClickListener onErrorAction) {
        Disposable disposable = observable.onErrorReturn(throwable -> {
            handleErrorRx(throwable, onErrorAction);
            return onErrorReturn;
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, throwable -> handleErrorRx(throwable, onErrorAction));
        if (fragment != null) {
            fragment.getDisposable().add(disposable);
        }
        return disposable;
    }
}
