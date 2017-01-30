package be.nielsbril.clicket.app.views;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nielsbril.clicket.app.R;
import be.nielsbril.clicket.app.databinding.FragmentAddCarBinding;
import be.nielsbril.clicket.app.viewmodels.AddCarFragmentViewModel;

public class AddCarFragment extends Fragment {

    private AddCarFragmentViewModel mAddCarFragmentViewModel;

    public static Fragment newInstance() {
        return new AddCarFragment();
    }

    public AddCarFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentAddCarBinding fragmentAddCarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_car, container, false);
        mAddCarFragmentViewModel = new AddCarFragmentViewModel(getActivity(), fragmentAddCarBinding);
        return fragmentAddCarBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mAddCarFragmentViewModel.init();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAddCarFragmentViewModel.stop();
    }
}