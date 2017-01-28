package be.nielsbril.clicket.app.views;


import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nielsbril.clicket.app.R;
import be.nielsbril.clicket.app.databinding.FragmentAccountBinding;
import be.nielsbril.clicket.app.viewmodels.AccountFragmentViewModel;

public class AccountFragment extends Fragment {

    private AccountFragmentViewModel mAccountFragmentViewModel;

    public static Fragment newInstance() {
        return new AccountFragment();
    }

    public AccountFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentAccountBinding fragmentAccountBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false);
        mAccountFragmentViewModel = new AccountFragmentViewModel(getActivity(), fragmentAccountBinding);
        return fragmentAccountBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAccountFragmentViewModel.init();
    }

}