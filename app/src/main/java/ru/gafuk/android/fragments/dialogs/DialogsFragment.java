package ru.gafuk.android.fragments.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;

import ru.gafuk.android.App;
import ru.gafuk.android.R;
import ru.gafuk.android.fragments.BaseFragment;

/**
 * Created by aborz on 18.11.2017.
 */

public class DialogsFragment extends BaseFragment {

    public DialogsFragment() {
        configuration.setDefaultTitle(App.getInstance().getString(R.string.fragment_title_contacts));
        configuration.setAlone(true);
        configuration.setNeedAuth(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
