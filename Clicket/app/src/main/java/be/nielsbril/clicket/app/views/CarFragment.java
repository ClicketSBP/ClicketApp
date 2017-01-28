package be.nielsbril.clicket.app.views;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import be.nielsbril.clicket.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CarFragment extends Fragment {


    public CarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        return textView;
    }

    public static Fragment newInstance() {
        return new CarFragment();
    }
}
