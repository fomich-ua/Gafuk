package ru.gafuk.android.views.drawers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import ru.gafuk.android.App;
import ru.gafuk.android.Constant;
import ru.gafuk.android.MainActivity;
import ru.gafuk.android.R;
import ru.gafuk.android.client.Client;
import ru.gafuk.android.fragments.BaseFragment;
import ru.gafuk.android.fragments.TabManager;
import ru.gafuk.android.fragments.auth.AuthFragment;
import ru.gafuk.android.fragments.blogs.main.BlogsMainFragment;
import ru.gafuk.android.fragments.contacts.ContactsFragment;
import ru.gafuk.android.fragments.news.main.NewsMainFragment;
import ru.gafuk.android.fragments.users.UsersFragment;
import ru.gafuk.android.settings.SettingsActivity;
import ru.gafuk.android.views.drawers.adapters.FragmentsListAdapter;

/**
 * Created by Александр on 22.10.2017.
 */

public class DrawerMenu implements NavigationView.OnNavigationItemSelectedListener{
    private final static String LOG_TAG = Constant.GAFUK_LOG_PREFIX + DrawerMenu.class.getSimpleName();

    private ArrayList<MenuItem> createdMenuItems = new ArrayList<>();

    private final MainActivity activity;

    private final DrawerLayout drawerLayout;

    private final NavigationView navigationView;

    private final NavigationView fragmentsNavigation;
    private RecyclerView fragmentsList;
    private LinearLayoutManager fragmentsListLayoutManager;
    private FragmentsListAdapter fragmentsListAdapter;
    private final Observer loginStateObservable;

    private Bundle savedInstanceState;

    private MenuItem lastActive;
    boolean isFirstSelected = false;

//    public DrawerMenu(DrawerLayout drawer, NavigationView navigationView, NavigationView fragmentsNavigation) {
    public DrawerMenu(MainActivity activity) {

        this.activity = activity;
        this.drawerLayout = activity.findViewById(R.id.drawer_layout); //drawer;
        this.navigationView = activity.findViewById(R.id.nav_view);//navigationView;
        this.fragmentsNavigation = activity.findViewById(R.id.nav_fragments);//fragmentsNavigation;

        fragmentsListAdapter = new FragmentsListAdapter();

        fragmentsListLayoutManager = new LinearLayoutManager(activity);
        fragmentsList = activity.findViewById(R.id.nav_fragments_list);
        fragmentsList.setLayoutManager(fragmentsListLayoutManager);
        fragmentsList.setAdapter(fragmentsListAdapter);

        loginStateObservable = new Observer() {
            @Override
            public void update(Observable observable, Object o) {

                Boolean loginState = (Boolean) o;

                initMenu(null);
            }
        };

        Client.addLoginStateObservable(loginStateObservable);
    }

    public void destroy(){

    }

    public ArrayList<MenuItem> getCreatedMenuItems() {
        return createdMenuItems;
    }

    public void init(Bundle savedInstanceState) {
        initMenu(savedInstanceState);
        initFragmentsList(savedInstanceState);

        this.savedInstanceState = savedInstanceState;
    }

    private void initMenu(Bundle savedInstanceState){
        // TODO: 24.10.2017 видимость пунктов меню в зависимости от допусловий
        createdMenuItems.clear();
        showMenuItem(R.id.nav_auth);

        if (!Client.loggedWithCookie()) {
            createdMenuItems.add(new MenuItem(App.getInstance().getString(R.string.fragment_title_auth),
                    R.drawable.ic_person_add,
                    R.id.nav_auth,
                    AuthFragment.class));
        }else {
            hideMenuItem(R.id.nav_auth);
        }

        createdMenuItems.add(new MenuItem(App.getInstance().getString(R.string.fragment_title_news_list),
                R.drawable.ic_newspaper,
                R.id.nav_news,
                NewsMainFragment.class));

        createdMenuItems.add(new MenuItem(App.getInstance().getString(R.string.fragment_title_blogs_list),
                R.drawable.ic_blog,
                R.id.nav_blogs,
                BlogsMainFragment.class));

        createdMenuItems.add(new MenuItem(App.getInstance().getString(R.string.fragment_title_contacts),
                R.drawable.ic_contacts,
                R.id.nav_contacts,
                ContactsFragment.class));
        setEnabledStateMenuItem(R.id.nav_contacts, Client.loggedWithCookie());

        createdMenuItems.add(new MenuItem(App.getInstance().getString(R.string.fragment_title_users),
                R.drawable.ic_users,
                R.id.nav_users,
                UsersFragment.class));
        setEnabledStateMenuItem(R.id.nav_users, Client.loggedWithCookie());
    }

    private void initFragmentsList(Bundle savedInstanceState){
        fragmentsListAdapter.setItemClickListener((tabFragment, position) -> {
            TabManager.getInstance().select(tabFragment);
            closeFragmentsList();
        });
        fragmentsListAdapter.setCloseClickListener((tabFragment, position) -> {
            TabManager.getInstance().remove(tabFragment);
            if (TabManager.getInstance().getSize() < 1) {
                activity.finish();
            }
        });
        TabManager.getInstance().loadState(savedInstanceState);
        TabManager.getInstance().updateFragmentList();
    }

    public NavigationView getNavigationView(){return navigationView;}

    public void firstSelect(){
        if (isFirstSelected)
            return;
        isFirstSelected = true;

        String className = Client.loggedWithCookie() ? NewsMainFragment.class.getSimpleName() : AuthFragment.class.getSimpleName();
        String last = App.getInstance().getPreferences().getString("menu_drawer_last", className);
        last = (Client.loggedWithCookie() && last.equals(AuthFragment.class.getSimpleName())) ? ContactsFragment.class.getSimpleName() : last;

        Log.d(LOG_TAG, "Last item " + last);

        MenuItem item = null;

        if (this.savedInstanceState != null) {

            BaseFragment tabFragment = TabManager.getInstance().get(TabManager.getActiveTag());

            if (tabFragment != null) {
                item = findMenuItem(tabFragment.getClass());
            }

            if (item != null) {
                item.setAttachedTabTag(tabFragment.getTag());
                item.setActive(true);
                lastActive = item;
            } else {
                item = findMenuItem(last);
            }

            Log.d(LOG_TAG, "Final item " + item);

            if (item == null) {
                item = createdMenuItems.get(0);
            }
            selectMenuItem(item);
        }else {
            item = findMenuItem(last);
        }

        if (item == null) {
            item = createdMenuItems.get(0);
        }
        selectMenuItem(item);
    }

    public void selectMenuItem(MenuItem item) {
        Log.d(LOG_TAG, "selectMenuItem " + item);

        if (item == null) return;

        try {

            BaseFragment tabFragment = TabManager.getInstance().get(item.getAttachedTabTag());

            if (tabFragment == null) {
                for (BaseFragment fragment : TabManager.getInstance().getFragments()) {
                    if (fragment.getClass() == item.getTabClass() && fragment.getConfiguration().isMenu()) {
                        tabFragment = fragment;
                        break;
                    }
                }
            }

            if (tabFragment == null) {
                tabFragment = item.getTabClass().newInstance();
                tabFragment.getConfiguration().setMenu(true);
                TabManager.getInstance().add(tabFragment);
                item.setAttachedTabTag(tabFragment.getTag());
            } else {
                TabManager.getInstance().select(tabFragment);
            }

            if (lastActive != null)
                lastActive.setActive(false);
            item.setActive(true);
            lastActive = item;
            navigationView.setCheckedItem(item.getMenuRes());
            App.getInstance().getPreferences().edit().putString("menu_drawer_last", item.getTabClass().getSimpleName()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MenuItem findMenuItem(Class<? extends BaseFragment> classObject) {
        for (MenuItem item : createdMenuItems) {
            if (item.getTabClass() == classObject)
                return item;
        }
        return null;
    }

    public MenuItem findMenuItem(String className) {
        for (MenuItem item : createdMenuItems) {
            if (item.getTabClass() != null && item.getTabClass().getSimpleName().equals(className))
                return item;
        }
        return null;
    }

    public MenuItem findMenuItem(@IdRes int menuRes){
        for (MenuItem item : createdMenuItems){
            if (item.getMenuRes() == menuRes){
                return item;
            }
        }
        return null;
    }

    public void showMenuItem(MenuItem menuItem){
        showMenuItem(menuItem.getMenuRes());
    }

    public void showMenuItem(@IdRes int menuRes){
        navigationView.getMenu().findItem(menuRes).setVisible(false);
    }

    public void hideMenuItem(MenuItem menuItem){
        navigationView.getMenu().findItem(menuItem.getMenuRes()).setVisible(false);
    }

    public void hideMenuItem(@IdRes int menuRes){
        navigationView.getMenu().findItem(menuRes).setVisible(false);
    }

    public void setEnabledStateMenuItem(MenuItem menuItem, boolean state){
        navigationView.getMenu().findItem(menuItem.getMenuRes()).setEnabled(state);
    }

    public void setEnabledStateMenuItem(@IdRes int menuRes, boolean state){
        navigationView.getMenu().findItem(menuRes).setEnabled(state);
    }

    /* основное меню*/
    public void openMenu() {
        drawerLayout.openDrawer(navigationView);
    }

    public boolean isMenuOpen() {
        return drawerLayout.isDrawerOpen(navigationView);
    }

    public void closeMenu() {
        drawerLayout.closeDrawer(navigationView);
    }

    public void toggleMenu() {
        if (drawerLayout.isDrawerOpen(navigationView)) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    /* список открытых фрагментов*/
    public void openFragmentsList() {
        drawerLayout.openDrawer(fragmentsNavigation);
    }

    public boolean isFragmentsListOpen() {
        return drawerLayout.isDrawerOpen(fragmentsNavigation);
    }

    public void closeFragmentsList() {
        drawerLayout.closeDrawer(fragmentsNavigation);
    }

    public void toggleFragmentsList() {
        if (drawerLayout.isDrawerOpen(fragmentsNavigation)) {
            closeFragmentsList();
        } else {
            openFragmentsList();
        }
    }

    public void notifyFragmentsListChanged() {
        fragmentsListAdapter.notifyDataSetChanged();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(android.view.MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        MenuItem menuItem = findMenuItem(id);
        if (menuItem != null){
            selectMenuItem(menuItem);
        }else {
            switch (id){
                case R.id.nav_settings:
                    activity.startActivity(new Intent(activity, SettingsActivity.class));
            }
        }
//        if (id == R.id.nav_news) {
//
//        } else if (id == R.id.nav_blogs) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
