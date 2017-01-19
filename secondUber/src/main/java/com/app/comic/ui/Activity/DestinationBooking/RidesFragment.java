package com.app.comic.ui.Activity.DestinationBooking;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.comic.R;
import com.app.comic.base.BaseFragment;
import com.app.comic.utils.SharedPrefManager;

import butterknife.ButterKnife;


public class RidesFragment extends BaseFragment {

    // Validator Attributes
    private SharedPrefManager pref;
    Activity act;

    public static RidesFragment newInstance() {

        RidesFragment fragment = new RidesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = getActivity();

        //MainApplication.get(getActivity()).createScopedGraph(new LoginModule(this)).inject(this);
        //RealmObjectController.clearCachedResult(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.secondhand_homepage, container, false);
        ButterKnife.inject(this, view);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        //presenter.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        //presenter.onPause();
    }

}
