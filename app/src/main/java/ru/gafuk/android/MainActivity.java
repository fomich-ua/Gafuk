package ru.gafuk.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.lang.reflect.Field;

import ru.gafuk.android.client.NetworkStateReceiver;
import ru.gafuk.android.fragments.BaseFragment;
import ru.gafuk.android.fragments.TabManager;
import ru.gafuk.android.utils.WebViewsProvider;
import ru.gafuk.android.views.drawers.DrawerMenu;

public class MainActivity extends AppCompatActivity implements TabManager.TabListener {

    public static final String LOG_TAG = Constant.GAFUK_LOG_PREFIX + MainActivity.class.getSimpleName();

    private DrawerMenu drawerMenu;

    private WebViewsProvider webViewsProvider;

    private final View.OnClickListener toggleListener = view -> drawerMenu.toggleMenu();
    private final View.OnClickListener removeTabListener = view -> backHandler(true);

    private NetworkStateReceiver receiver;

    public MainActivity() {
        webViewsProvider = new WebViewsProvider();
        TabManager.init(this, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            Field field = Toolbar.class.getDeclaredField("mTitleTextView");
            field.setAccessible(true);
            Object value = field.get(toolbar);
            if (value != null) {
                TextView textView = (TextView) value;
                textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                textView.setHorizontallyScrolling(true);
                textView.setMarqueeRepeatLimit(3);
                textView.setSelected(true);
            }
        }catch (Exception ex){

        }


//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show());

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        // обработка выбора меню
//        drawerMenu = new DrawerMenu(drawerLayout, navigationView, fragmentsNavigation);
        drawerMenu = new DrawerMenu(this);
        drawerMenu.init(savedInstanceState);

        navigationView.setNavigationItemSelectedListener(drawerMenu);

        receiver = new NetworkStateReceiver();
        receiver.registerReceiver();

        checkIntent(getIntent());

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "onResume");
        Log.d(LOG_TAG, "TabManager active tab: " + TabManager.getActiveIndex() + " : " + TabManager.getActiveTag());
        if (receiver != null)
            receiver.registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LOG_TAG, "onPause");
        if (receiver != null)
            receiver.unregisterReceiver();
    }

    @Override
    protected void onDestroy() {
        Log.d(LOG_TAG, "onDestroy");
        super.onDestroy();

        if (receiver != null) {
            receiver.unregisterReceiver();
        }
        if (drawerMenu != null) {
            drawerMenu.destroy();
        }
//        if (drawerHeader != null) {
//            drawerHeader.destroy();
//        }
        if (webViewsProvider != null) {
            webViewsProvider.destroy();
        }
    }

    @Override
    public void onBackPressed() {

        if (drawerMenu.isMenuOpen()) {
            drawerMenu.closeMenu();
            return;
        }
        if (drawerMenu.isFragmentsListOpen()) {
            drawerMenu.closeFragmentsList();
            return;
        }
        backHandler(false);
    }

    public void backHandler(boolean fromToolbar) {
        BaseFragment active = TabManager.getInstance().getActive();
        if (active == null) {
            finish();
            return;
        }
        if (fromToolbar || !active.onBackPressed()) {
            TabManager.getInstance().remove(active);
            if (TabManager.getInstance().getSize() < 1) {
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        checkIntent(intent);
    }

    void checkIntent(Intent intent) {
        if (intent == null || intent.getData() == null) {
            if (TabManager.getInstance().getSize() == 0) {
                drawerMenu.firstSelect();
            }
            return;
        }

//        new Handler().post(() -> {
//            Log.d(LOG_TAG, "Handler.post checkIntent: " + intent);
//            boolean handled = IntentHandler.handle(intent.getData().toString());
//            if (!handled || TabManager.getInstance().getSize() == 0) {
//                drawers.firstSelect();
//            }
//            setIntent(null);
//        });
    }

    public WebViewsProvider getWebViewsProvider() {
        return webViewsProvider;
    }

    public DrawerMenu getDrawerMenu(){
        return drawerMenu;
    }

    public void updateFragmentsList() {
        drawerMenu.notifyFragmentsListChanged();
    }

    public void hideKeyboard() {
        if (MainActivity.this.getCurrentFocus() != null)
            ((InputMethodManager) MainActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onAddTab(BaseFragment fragment) {
        Log.d(LOG_TAG, "onAddTab: " + fragment);
        getSupportActionBar().setTitle(fragment.getTitle());
    }

    @Override
    public void onRemoveTab(BaseFragment fragment) {

    }

    @Override
    public void onSelectTab(BaseFragment fragment) {
        Log.d(LOG_TAG, "onSelectTab: " + fragment);
        getSupportActionBar().setTitle(fragment.getTitle());
    }

    @Override
    public void onChange() {
        BaseFragment active = TabManager.getInstance().getActive();
        Log.d(LOG_TAG, "onChange: activeFragment = " + active);
        if (active!=null){
            getSupportActionBar().setTitle(active.getTitle());
            ru.gafuk.android.views.drawers.MenuItem menuItem = drawerMenu.findMenuItem(active.getClass());
            if (menuItem!=null){
                drawerMenu.getNavigationView().setCheckedItem(menuItem.getMenuRes());
            }
        }
        updateFragmentsList();
    }

}
