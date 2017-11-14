package ru.gafuk.android.api.news.interfaces;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by Александр on 23.10.2017.
 */

public interface INewsApi {
    List<? extends INewsItem> getNews(@Nullable int pageNumber) throws Exception;
}
