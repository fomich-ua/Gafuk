package ru.gafuk.android.api.blogs.interfaces;

import android.support.annotation.Nullable;

import java.util.List;

/**
 * Created by Александр on 23.10.2017.
 */

public interface IBlogApi {
    List<? extends IBlogItem> getBlogs(@Nullable int pageNumber) throws Exception;
}
