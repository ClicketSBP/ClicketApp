package be.nielsbril.clicket.app.viewmodels;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;

import be.nielsbril.clicket.app.BR;
import be.nielsbril.clicket.app.R;
import be.nielsbril.clicket.app.api.CarsResult;
import be.nielsbril.clicket.app.api.ClicketInstance;
import be.nielsbril.clicket.app.databinding.FragmentCarBinding;
import be.nielsbril.clicket.app.helpers.ApiHelper;
import be.nielsbril.clicket.app.helpers.AuthHelper;
import be.nielsbril.clicket.app.helpers.CustomSnackbar;
import be.nielsbril.clicket.app.helpers.Interfaces;
import be.nielsbril.clicket.app.helpers.Utils;
import be.nielsbril.clicket.app.models.Car;
import be.nielsbril.clicket.app.views.AddCarFragment;
import rx.functions.Action1;

public class CarFragmentViewModel extends BaseObservable {

    private Interfaces.changeToolbar mListener;
    private Interfaces.navigate mNavigator;

    private Context mContext;
    private FragmentCarBinding mFragmentCarBinding;
    private FloatingActionButton mFab;

    private ObservableField<String> message = new ObservableField<String>();

    public ObservableField<String> getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message.set(message);
    }

    private ObservableArrayList<Car> cars = null;

    public ObservableArrayList<Car> getCars() {
        return cars;
    }

    public void setCars(ObservableArrayList<Car> cars) {
        this.cars = cars;
    }

    public CarFragmentViewModel(Context context, FragmentCarBinding fragmentCarBinding) {
        mContext = context;
        mFragmentCarBinding = fragmentCarBinding;
        mFab = (FloatingActionButton) fragmentCarBinding.getRoot().findViewById(R.id.fabAdd);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNavigator.navigateFragment(AddCarFragment.newInstance(), "addCarFragment");
            }
        });

        if (context instanceof Interfaces.changeToolbar) {
            mListener = (Interfaces.changeToolbar) context;
            mListener.setTitle("Cars");
            mListener.toggleNavItems("car");
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
        mFragmentCarBinding.setViewmodel(this);
        setMessage("Loading ...");
        loadCars();
        notifyPropertyChanged(BR.viewmodel);
    }

    private void loadCars() {
        setCars(new ObservableArrayList<Car>());
        ApiHelper.subscribe(ClicketInstance.getClicketserviceInstance().cars(AuthHelper.getAuthToken(mContext)), new Action1<CarsResult>() {
            @Override
            public void call(CarsResult carsResult) {
                if (carsResult != null && carsResult.isSuccess()) {
                    if (carsResult.getData().size() == 0) {
                        setMessage("No cars found");
                        mFragmentCarBinding.txtMessage.setVisibility(View.VISIBLE);
                        mFragmentCarBinding.rvCars.setVisibility(View.GONE);
                        notifyPropertyChanged(BR.viewmodel);
                    } else {
                        mFragmentCarBinding.txtMessage.setVisibility(View.GONE);
                        mFragmentCarBinding.rvCars.setVisibility(View.VISIBLE);
                        for (int i = 0, l = carsResult.getData().size(); i < l; i++) {
                            cars.add(carsResult.getData().get(i));
                            notifyPropertyChanged(BR.viewmodel);
                        }
                    }
                } else {
                    if (Utils.isNetworkConnected(mContext)) {
                        setMessage("Error: try again later");
                    } else {
                        Snackbar snackbar = Snackbar.make(mFab, "No internet connection. Please turn on your internet signal first.", Snackbar.LENGTH_LONG);
                        CustomSnackbar.colorSnackBar(snackbar).show();
                        setMessage("No internet connection");
                    }
                    mFragmentCarBinding.txtMessage.setVisibility(View.VISIBLE);
                    mFragmentCarBinding.rvCars.setVisibility(View.GONE);
                    notifyPropertyChanged(BR.viewmodel);
                }
            }
        });
    }

}