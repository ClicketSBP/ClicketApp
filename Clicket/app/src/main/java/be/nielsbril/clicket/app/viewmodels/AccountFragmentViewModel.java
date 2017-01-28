package be.nielsbril.clicket.app.viewmodels;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;
import com.google.gson.JsonObject;

import be.nielsbril.clicket.app.App;
import be.nielsbril.clicket.app.R;
import be.nielsbril.clicket.app.api.ClicketInstance;
import be.nielsbril.clicket.app.databinding.FragmentAccountBinding;
import be.nielsbril.clicket.app.helpers.AuthHelper;
import be.nielsbril.clicket.app.helpers.CustomSnackBar;
import be.nielsbril.clicket.app.helpers.Interfaces;
import be.nielsbril.clicket.app.helpers.Utils;
import be.nielsbril.clicket.app.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragmentViewModel extends BaseObservable {

    private Interfaces.changeToolbar mListener;

    private Context mContext;
    private FragmentAccountBinding mFragmentAccountBinding;
    private FloatingActionButton mFab;
    private AppCompatEditText mTxbPhone;

    private boolean edit = true;

    private User user = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AccountFragmentViewModel(Context context, FragmentAccountBinding fragmentAccountBinding) {
        mContext = context;
        mFragmentAccountBinding = fragmentAccountBinding;
        mFab = (FloatingActionButton) fragmentAccountBinding.getRoot().findViewById(R.id.fabUser);
        mTxbPhone = (AppCompatEditText) fragmentAccountBinding.getRoot().findViewById(R.id.txbPhone);
        setUser(((App) ((Activity) mContext).getApplication()).getUser());

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edit) {
                    edit();
                    edit = false;
                } else {
                    save();
                    edit = true;
                }
            }
        });

        if (context instanceof Interfaces.changeToolbar) {
            mListener = (Interfaces.changeToolbar) context;
            mListener.setTitle("Account");
        } else {
            throw new RuntimeException(context.toString() + " must implement headerChangedListener");
        }
    }

    public void init() {
        mFragmentAccountBinding.setViewmodel(this);
        notifyPropertyChanged(BR.viewmodel);
    }

    private void edit() {
        mTxbPhone.setEnabled(true);
        mFab.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_save_white_24dp));
    }

    private void save() {
        String phone = mTxbPhone.getText().toString();
        if (checkFields(phone)) {
            mTxbPhone.setEnabled(false);
            mFab.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mode_edit_white_24dp));
            Call<JsonObject> call = ClicketInstance.getClicketserviceInstance().editUser(getUser().getEmail(), getUser().getName(), getUser().getFirstname(), getUser().getPhone(), getUser().getInvoice_amount(), AuthHelper.getAuthToken(mContext));
            call.enqueue(editCallback);
        }
    }

    private boolean checkFields(String phone) {
        boolean isValid = true;

        if (!Utils.isEmailValid(phone)) {
            isValid = false;
        }

        return isValid;
    }

    Callback<JsonObject> editCallback = new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (response.isSuccessful() && response.body().getAsJsonPrimitive("success").getAsBoolean()) {
                // TODO: 28/01/2017 edit user 
            } else {
                showError("Error when saving data: try again later");
                mTxbPhone.setEnabled(false);
                mFab.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mode_edit_white_24dp));

                mTxbPhone.setText(getUser().getPhone());
            }
            notifyPropertyChanged(BR.viewmodel);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.d("Error", t.getMessage());
            showError("Error when saving data: try again later");
            mTxbPhone.setEnabled(false);
            mFab.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mode_edit_white_24dp));

            mTxbPhone.setText(getUser().getPhone());
            notifyPropertyChanged(BR.viewmodel);
        }
    };

    private void showError(String error) {
        Snackbar snackbar = Snackbar.make(mTxbPhone, error, Snackbar.LENGTH_SHORT);
        CustomSnackBar.colorSnackBar(snackbar).show();
    }

}