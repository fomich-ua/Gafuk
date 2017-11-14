package ru.gafuk.android.utils;

import java.util.Observable;

/**
 * Created by Александр on 25.10.2017.
 */

public class SimpleObservable extends Observable {

    @Override
    public synchronized boolean hasChanged() {
        return true;
    }

}
