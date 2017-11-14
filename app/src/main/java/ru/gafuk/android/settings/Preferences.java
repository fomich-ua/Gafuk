package ru.gafuk.android.settings;

import android.content.SharedPreferences;

import ru.gafuk.android.App;

/**
 * Created by Александр on 26.10.2017.
 */

public class Preferences {
    private static SharedPreferences preferences() {
        return App.getInstance().getPreferences();
    }

    public final static class News {
        private final static String PREFIX = "news.";

        public final static String ITEM_VIEW = PREFIX + "item_view";
        public final static String PROVIDER = PREFIX + "provider";
        public final static String LOAD_ON_SCROLL = PREFIX + "load_on_scroll";

        public static String getItemView(){return preferences().getString(ITEM_VIEW, "compat");};

        public static String getProvider(){return preferences().getString(PROVIDER, "RSS");};

        public static Boolean getLoadOnScroll(){return preferences().getBoolean(LOAD_ON_SCROLL, false);};
    }

    public final static class Blog {
        private final static String PREFIX = "blog.";

        public final static String ITEM_VIEW = PREFIX + "item_view";
        public final static String PROVIDER = PREFIX + "provider";
        public final static String LOAD_ON_SCROLL = PREFIX + "load_on_scroll";

        public static String getItemView(){return preferences().getString(ITEM_VIEW, "compat");};

        public static String getProvider(){return preferences().getString(PROVIDER, "RSS");};

        public static Boolean getLoadOnScroll(){return preferences().getBoolean(LOAD_ON_SCROLL, false);};
    }

    public final static class Users{
        private final static String PREFIX = "users.";

        public final static String LOAD_ON_SCROLL = PREFIX + "load_on_scroll";

        public static Boolean getLoadOnScroll(){return preferences().getBoolean(LOAD_ON_SCROLL, false);};
    }
}
