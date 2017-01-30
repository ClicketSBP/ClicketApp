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
import be.nielsbril.clicket.app.databinding.FragmentHistoryBinding;
import be.nielsbril.clicket.app.viewmodels.HistoryFragmentViewModel;

public class HistoryFragment extends Fragment {

    private HistoryFragmentViewModel mHistoryFragmentViewModel;

    public static Fragment newInstance() {
        return new HistoryFragment();
    }

    public HistoryFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentHistoryBinding fragmentHistoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false);
        fragmentHistoryBinding.rvHistory.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        fragmentHistoryBinding.rvHistory.setItemAnimator(new DefaultItemAnimator());
        mHistoryFragmentViewModel = new HistoryFragmentViewModel(getActivity(), fragmentHistoryBinding);
        return fragmentHistoryBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mHistoryFragmentViewModel.init();
    }

}