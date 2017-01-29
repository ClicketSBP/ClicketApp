package be.nielsbril.clicket.app.views;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nielsbril.clicket.app.R;
import be.nielsbril.clicket.app.databinding.FragmentCarBinding;
import be.nielsbril.clicket.app.viewmodels.CarFragmentViewModel;

public class CarFragment extends Fragment {

    private CarFragmentViewModel mCarFragmentViewModel;

    public static Fragment newInstance() {
        return new CarFragment();
    }

    public CarFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentCarBinding fragmentCarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_car, container, false);
        fragmentCarBinding.rvCars.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        fragmentCarBinding.rvCars.setItemAnimator(new DefaultItemAnimator());
        mCarFragmentViewModel = new CarFragmentViewModel(getActivity(), fragmentCarBinding);
        return fragmentCarBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mCarFragmentViewModel.init();
    }

}