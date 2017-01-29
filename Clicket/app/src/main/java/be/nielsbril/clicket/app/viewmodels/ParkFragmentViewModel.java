package be.nielsbril.clicket.app.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import be.nielsbril.clicket.app.BR;
import be.nielsbril.clicket.app.R;
import be.nielsbril.clicket.app.api.CarResult;
import be.nielsbril.clicket.app.api.ClicketInstance;
import be.nielsbril.clicket.app.databinding.FragmentParkBinding;
import be.nielsbril.clicket.app.helpers.ApiHelper;
import be.nielsbril.clicket.app.helpers.AuthHelper;
import be.nielsbril.clicket.app.helpers.CustomSnackBar;
import be.nielsbril.clicket.app.helpers.Interfaces;
import be.nielsbril.clicket.app.models.Car;
import be.nielsbril.clicket.app.views.AddCarFragment;
import rx.functions.Action1;

public class ParkFragmentViewModel extends BaseObservable implements AdapterView.OnItemSelectedListener {

    private Interfaces.changeToolbar mListener;
    private Interfaces.navigate mNavigator;

    private Context mContext;
    private FragmentParkBinding mFragmentParkBinding;
    private Spinner mSpCars;
    private AppCompatButton mBtnStart;
    private AppCompatButton mBtnStop;
    private AppCompatButton mBtnHistory;

    private int mId;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    private ArrayList<String> mCars = new ArrayList<String>();
    private ArrayList<Integer> mCarIds = new ArrayList<Integer>();

    public ParkFragmentViewModel(Context context, FragmentParkBinding fragmentParkBinding) {
        mContext = context;
        mFragmentParkBinding = fragmentParkBinding;
        mSpCars = (Spinner) fragmentParkBinding.getRoot().findViewById(R.id.spCars);
        mBtnStart = (AppCompatButton) fragmentParkBinding.getRoot().findViewById(R.id.btnStart);
        mBtnStop = (AppCompatButton) fragmentParkBinding.getRoot().findViewById(R.id.btnStop);
        mBtnHistory = (AppCompatButton) fragmentParkBinding.getRoot().findViewById(R.id.btnHistory);

        mSpCars.setOnItemSelectedListener(this);

        if (context instanceof Interfaces.changeToolbar) {
            mListener = (Interfaces.changeToolbar) context;
            mListener.setTitle("Clicket");
            mListener.toggleNavItems("park");
        } else {
            throw new RuntimeException(context.toString() + " must implement changeToolbar");
        }

        if (context instanceof Interfaces.navigate) {
            mNavigator = (Interfaces.navigate) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement navigate");
        }
    }

    public void init() {
        mFragmentParkBinding.setViewmodel(this);
        loadCars();
        notifyPropertyChanged(BR.viewmodel);
    }

    private void loadCars() {
        ApiHelper.subscribe(ClicketInstance.getClicketserviceInstance().cars(AuthHelper.getAuthToken(mContext)), new Action1<CarResult>() {
            @Override
            public void call(CarResult carResult) {
                if (carResult != null && carResult.isSuccess()) {
                    if (carResult.getData().size() == 0) {
                        showSnackbar("Please add a car first");
                        mNavigator.navigateFragment(AddCarFragment.newInstance(), "addCarFragment");
                    } else {
                        for (int i = 0, l = carResult.getData().size(); i < l; i++) {
                            Car car = carResult.getData().get(i);
                            mCars.add(car.getName() + " (" + car.getLicense_plate() + ")");
                            mCarIds.add(car.get_id());
                        }

                        ArrayAdapter<String> carsAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, mCars);
                        carsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpCars.setAdapter(carsAdapter);
                    }
                } else {
                    showSnackbar("Error: try again later");
                    mBtnStart.setEnabled(false);
                    mBtnStop.setEnabled(false);
                }
            }
        });
    }

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(mSpCars, message, Snackbar.LENGTH_SHORT);
        CustomSnackBar.colorSnackBar(snackbar).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (mCars.size() != 0) {
            String car = mCars.get(i);
            mId = mCarIds.get(i);
            showSnackbar(car + " selected");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        showSnackbar("Please select a car");
    }

}