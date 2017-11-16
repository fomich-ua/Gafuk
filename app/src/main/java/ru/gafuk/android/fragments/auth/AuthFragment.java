package ru.gafuk.android.fragments.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import ru.gafuk.android.App;
import ru.gafuk.android.R;
import ru.gafuk.android.api.auth.models.AuthForm;
import ru.gafuk.android.client.Client;
import ru.gafuk.android.fragments.BaseFragment;
import ru.gafuk.android.fragments.TabManager;
import ru.gafuk.android.fragments.contacts.ContactsFragment;
import ru.gafuk.android.fragments.news.main.NewsMainFragment;
import ru.gafuk.android.rxapi.RxApi;
import ru.gafuk.android.utils.rx.Subscriber;
import ru.gafuk.android.views.drawers.DrawerMenu;
import ru.gafuk.android.views.drawers.MenuItem;

/**
 * Created by Александр on 26.10.2017.
 */

public class AuthFragment extends BaseFragment {
    private EditText nick, password;
    private Button sendButton;
    private Button skipButton;

    private ProgressBar loginProgress;
    private CheckBox rememberAuth;

    private Subscriber<Boolean> loginSubscriber = new Subscriber<>(this);

    public AuthFragment() {
        configuration.setAlone(true);
        configuration.setDefaultTitle(App.getInstance().getString(R.string.fragment_title_auth));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        baseInflateFragment(inflater, R.layout.auth_fragment);

        nick = (EditText) findViewById(R.id.auth_login);
        password = (EditText) findViewById(R.id.auth_password);

        rememberAuth = (CheckBox) findViewById(R.id.auth_remember);

        loginProgress = (ProgressBar) findViewById(R.id.login_progress);

        skipButton = (Button) findViewById(R.id.auth_skip);
        sendButton = (Button) findViewById(R.id.auth_send);

        skipButton.setOnClickListener(v -> {
            DrawerMenu drawerMenu = getMainActivity().getDrawerMenu();
            MenuItem menuItem = null;

            menuItem = drawerMenu.findMenuItem(AuthFragment.class);
            drawerMenu.hideMenuItem(menuItem);
            TabManager.getInstance().remove(AuthFragment.this);

            menuItem = drawerMenu.findMenuItem(NewsMainFragment.class);
            drawerMenu.selectMenuItem(menuItem);
        });

        sendButton.setOnClickListener(view -> tryLogin());

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!nick.getText().toString().isEmpty()
                        && !password.getText().toString().isEmpty()) {
                    if (!sendButton.isEnabled())
                        sendButton.setEnabled(true);
                } else {
                    if (sendButton.isEnabled())
                        sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        nick.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void tryLogin(){
        AuthForm authForm = new AuthForm();

        authForm.setNick(nick.getText().toString());
        authForm.setPassword(password.getText().toString());
        authForm.setRemember(rememberAuth.isChecked() ? "1" : "0");

        loginProgress.setVisibility(View.VISIBLE);
        sendButton.setVisibility(View.INVISIBLE);

        hidePopupWindows();

        loginSubscriber.subscribe(RxApi.Auth().login(authForm), this::showLoginResult, false, v -> loadData());
    }

    private void showLoginResult(boolean loginResult) {
        Log.d(AuthFragment.class.getSimpleName(), "showLoginResult " + loginResult);
        Client.notifyLoginStateObservables(loginResult);
        if (!loginResult) {
            password.setText("");
            loginProgress.setVisibility(View.GONE);
            sendButton.setVisibility(View.VISIBLE);

//            Snackbar.make(getCoordinatorLayout(), "Неудачный логин", Snackbar.LENGTH_SHORT)
//                    .show();
        }else {

            DrawerMenu drawerMenu = getMainActivity().getDrawerMenu();
            MenuItem menuItem = null;
//
//            menuItem = drawerMenu.findMenuItem(AuthFragment.class);
//            drawerMenu.hideMenuItem(menuItem);
            TabManager.getInstance().remove(AuthFragment.this);
//
            menuItem = drawerMenu.findMenuItem(ContactsFragment.class);
            drawerMenu.selectMenuItem(menuItem);

        }
    }
}
