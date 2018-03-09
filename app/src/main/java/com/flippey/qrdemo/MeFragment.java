package com.flippey.qrdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by flippey on 2018/3/9 15:07.
 */

public class MeFragment extends Fragment {

    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_me, container, false);
        }
        return mView;
    }

    public static MeFragment newInstance() {
        Bundle args = new Bundle();
        MeFragment fragmentMe = new MeFragment();
        fragmentMe.setArguments(args);
        return fragmentMe;
    }
}
