package be.nielsbril.clicket.app.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;

import com.google.gson.JsonObject;

import be.nielsbril.clicket.app.BR;
import be.nielsbril.clicket.app.R;
import be.nielsbril.clicket.app.api.ClicketInstance;
import be.nielsbril.clicket.app.databinding.FragmentAddCarBinding;
import be.nielsbril.clicket.app.helpers.AuthHelper;
import be.nielsbril.clicket.app.helpers.CustomSnackbar;
import be.nielsbril.clicket.app.helpers.Interfaces;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCarFragmentViewModel extends BaseObservable {

    private Interfaces.changeToolbar mListener;

    private Context mContext;
    private FragmentAddCarBinding mFragmentAddCarBinding;

    private AppCompatEditText mTxbCar;
    private AppCompatEditText mTxbLicensePlate;
    private CheckBox mChDefaultCar;

    private String mValidation = "";

    public AddCarFragmentViewModel(Context context, FragmentAddCarBinding fragmentAddCarBinding) {
        mContext = context;
        mFragmentAddCarBinding = fragmentAddCarBinding;
        mTxbCar = (AppCompatEditText) fragmentAddCarBinding.getRoot().findViewById(R.id.txbCar);
        mTxbLicensePlate = (AppCompatEditText) fragmentAddCarBinding.getRoot().findViewById(R.id.txbLicensePlate);
        mChDefaultCar = (CheckBox) fragmentAddCarBinding.getRoot().findViewById(R.id.chDefaultCar);

        FloatingActionButton mFabBack = (FloatingActionButton) fragmentAddCarBinding.getRoot().findViewById(R.id.fabBack);
        FloatingActionButton mFabSave = (FloatingActionButton) fragmentAddCarBinding.getRoot().findViewById(R.id.fabSave);

        mFabBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).getFragmentManager().popBackStack();
                View focus = ((Activity) mContext).getCurrentFocus();
                if (focus != null) {
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
                }
            }
        });

        mFabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });

        if (context instanceof Interfaces.changeToolbar) {
            mListener = (Interfaces.changeToolbar) context;
            mListener.setTitle("Add car");
            mListener.toggleNavItems("car");
        } else {
            throw new RuntimeException(context.toString() + " must implement changeToolbar");
        }
    }

    public void init() {
        mFragmentAddCarBinding.setViewmodel(this);
        notifyPropertyChanged(BR.viewmodel);
    }

    private void add() {
        String name = mTxbCar.getText().toString();
        String licensePlate = mTxbLicensePlate.getText().toString().toUpperCase();
        String defaultCar = String.valueOf(mChDefaultCar.isChecked());
        if (checkFields(name, licensePlate)) {
            Call<JsonObject> call = ClicketInstance.getClicketserviceInstance().addCar(name, licensePlate, defaultCar, AuthHelper.getAuthToken(mContext));
            call.enqueue(saveCallback);
        } else {
            showSnackbar(mValidation);
        }
    }

    private boolean checkFields(String name, String licensePlate) {
        boolean isValid = true;

        if (name.equals("") || licensePlate.equals("")) {
            isValid = false;
            mValidation = "Error when saving data: please fill in all fields";
        } else if (licensePlate.length() < 7) {
            isValid = false;
            mValidation = "Error when saving data: not a valid license plate number";
        }

        return isValid;
    }

    private Callback<JsonObject> saveCallback = new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (response.isSuccessful() && response.body().getAsJsonPrimitive("success").getAsBoolean()) {
                showSnackbar(response.body().getAsJsonPrimitive("info").getAsString());
                ((Activity) mContext).getFragmentManager().popBackStack();
            } else {
                mValidation = (!response.body().getAsJsonPrimitive("info").getAsString().equals("") ? response.body().getAsJsonPrimitive("info").getAsString() : "Error when logging in: unknown error");
                showSnackbar(mValidation);
            }
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.d("Error", t.getMessage());
            showSnackbar("Error when saving data: try again later");
        }
    };

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(mTxbCar, message, Snackbar.LENGTH_LONG);
        CustomSnackbar.colorSnackBar(snackbar).show();
    }

    public void stop() {
        View focus = ((Activity) mContext).getCurrentFocus();
        if (focus != null) {
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        }
    }

}