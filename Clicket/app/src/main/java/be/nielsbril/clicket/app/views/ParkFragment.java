package be.nielsbril.clicket.app.views;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nielsbril.clicket.app.R;
import be.nielsbril.clicket.app.databinding.FragmentAccountBinding;
import be.nielsbril.clicket.app.databinding.FragmentParkBinding;
import be.nielsbril.clicket.app.viewmodels.AccountFragmentViewModel;
import be.nielsbril.clicket.app.viewmodels.ParkFragmentViewModel;

public class ParkFragment extends Fragment {

    private ParkFragmentViewModel mParkFragmentViewModel;

    public static Fragment newInstance() {
        return new ParkFragment();
    }

    public ParkFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentParkBinding fragmentParkBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_park, container, false);
        mParkFragmentViewModel = new ParkFragmentViewModel(getActivity(), fragmentParkBinding);
        return fragmentParkBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mParkFragmentViewModel.init();
    }

}