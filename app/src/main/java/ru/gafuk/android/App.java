package ru.gafuk.android;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.preference.PreferenceManager;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;
import android.util.TypedValue;

import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.Observer;

import ru.gafuk.android.utils.SimpleObservable;

/**
 * Created by Александр on 22.10.2017.
 */

public class App extends Application {
    private static App instance;

    private float density = 1.0f;
    private SharedPreferences preferences;
    private SimpleObservable preferenceChangeObservables = new SimpleObservable();

    public App() {
        instance = this;
    }

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    public static Context getContext() {
        return getInstance();
    }

    public void onCreate() {
        super.onCreate();

        FlowManager.init(this);

        density = getResources().getDisplayMetrics().density;

        instance = this;

        getPreferences().registerOnSharedPreferenceChangeListener(preferenceChangeListener);

    }

    public SharedPreferences getPreferences() {
        if (preferences == null)
            preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences;
    }

    @ColorInt
    public static int getColorFromAttr(Context context, @AttrRes int attr) {
        TypedValue typedValue = new TypedValue();
        if (context != null && context.getTheme().resolveAttribute(attr, typedValue, true))
            return typedValue.data;
        else
            return Color.RED;
    }

    /*Only vector icon*/
    public static Drawable getVecDrawable(Context context, @DrawableRes int id) {
        Drawable drawable = AppCompatResources.getDrawable(context, id);
        if (!(drawable instanceof VectorDrawableCompat || drawable instanceof VectorDrawable)) {
            throw new RuntimeException();
        }
        return drawable;
    }

    public float getDensity() {
        return density;
    }

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = (sharedPreferences, key) -> {
        Log.d(Constant.GAFUK_LOG_PREFIX + App.class.getSimpleName(), "Preference changed: " + key);
        if (key == null) return;
        preferenceChangeObservables.notifyObservers(key);
    };

    public void addPreferenceChangeObserver(Observer observer) {
        preferenceChangeObservables.addObserver(observer);
    }

    public void removePreferenceChangeObserver(Observer observer) {
        preferenceChangeObservables.deleteObserver(observer);
    }
}
