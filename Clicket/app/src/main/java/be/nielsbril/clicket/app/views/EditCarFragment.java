package be.nielsbril.clicket.app.views;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.nielsbril.clicket.app.R;
import be.nielsbril.clicket.app.databinding.FragmentEditCarBinding;
import be.nielsbril.clicket.app.models.Car;
import be.nielsbril.clicket.app.viewmodels.EditCarFragmentViewModel;

public class EditCarFragment extends Fragment {

    private static final String ARG_car = "be.nielsbril.clicket.app.car";

    private EditCarFragmentViewModel mEditCarFragmentViewModel;
    private Car mCar;

    public static Fragment newInstance(Car car) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_car, car);

        EditCarFragment editCarFragment = new EditCarFragment();
        editCarFragment.setArguments(args);
        return editCarFragment;
    }

    public EditCarFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mCar = args.getParcelable(ARG_car);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentEditCarBinding fragmentEditCarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_car, container, false);
        mEditCarFragmentViewModel = new EditCarFragmentViewModel(getActivity(), fragmentEditCarBinding, mCar);
        return fragmentEditCarBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mEditCarFragmentViewModel.init();
    }

    @Override
    public void onStop() {
        super.onStop();
        mEditCarFragmentViewModel.stop();
    }
}