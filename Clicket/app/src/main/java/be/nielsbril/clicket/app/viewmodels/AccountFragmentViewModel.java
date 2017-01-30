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

import com.google.gson.JsonObject;

import be.nielsbril.clicket.app.App;
import be.nielsbril.clicket.app.BR;
import be.nielsbril.clicket.app.R;
import be.nielsbril.clicket.app.api.ClicketInstance;
import be.nielsbril.clicket.app.databinding.FragmentAccountBinding;
import be.nielsbril.clicket.app.helpers.AuthHelper;
import be.nielsbril.clicket.app.helpers.CustomSnackbar;
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

    private boolean mEdit = true;
    private String mPhone;

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
                if (mEdit) {
                    edit();
                    mEdit = false;
                } else {
                    save();
                    mEdit = true;
                }
            }
        });

        if (context instanceof Interfaces.changeToolbar) {
            mListener = (Interfaces.changeToolbar) context;
            mListener.setTitle("Account");
            mListener.toggleNavItems("account");
        } else {
            throw new RuntimeException(context.toString() + " must implement changeToolbar");
        }
    }

    public void init() {
        mFragmentAccountBinding.setViewmodel(this);
        notifyPropertyChanged(BR.viewmodel);
    }

    private void edit() {
        mTxbPhone.setEnabled(true);
        mTxbPhone.requestFocus();
        mFab.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_save_white_24dp));
    }

    private void save() {
        mPhone = mTxbPhone.getText().toString();
        if (checkFields(mPhone)) {
            mTxbPhone.setEnabled(false);
            mFab.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mode_edit_white_24dp));
            Call<JsonObject> call = ClicketInstance.getClicketserviceInstance().editUser(getUser().getEmail(), getUser().getName(), getUser().getFirstname(), getUser().getPhone(), String.valueOf(getUser().getInvoice_amount()), AuthHelper.getAuthToken(mContext));
            call.enqueue(saveCallback);
        } else {
            showSnackbar("Error when saving data: not a valid phone number. Format: 0032499999999.");
        }
    }

    private boolean checkFields(String phone) {
        boolean isValid = true;

        if (!Utils.isPhoneValid(phone)) {
            isValid = false;
        }

        return isValid;
    }

    private Callback<JsonObject> saveCallback = new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (response.isSuccessful() && response.body().getAsJsonPrimitive("success").getAsBoolean()) {
                showSnackbar(response.body().getAsJsonPrimitive("info").getAsString());
                ((App) ((Activity) mContext).getApplication()).getUser().setPhone(mPhone);
            } else {
                showSnackbar("Error when saving data: try again later");
                mTxbPhone.setEnabled(false);
                mFab.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mode_edit_white_24dp));

                mTxbPhone.setText(getUser().getPhone());
            }
            notifyPropertyChanged(BR.viewmodel);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.d("Error", t.getMessage());
            showSnackbar("Error when saving data: try again later");
            mTxbPhone.setEnabled(false);
            mFab.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mode_edit_white_24dp));

            mTxbPhone.setText(getUser().getPhone());
            notifyPropertyChanged(BR.viewmodel);
        }
    };

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(mTxbPhone, message, Snackbar.LENGTH_LONG);
        CustomSnackbar.colorSnackBar(snackbar).show();
    }

}