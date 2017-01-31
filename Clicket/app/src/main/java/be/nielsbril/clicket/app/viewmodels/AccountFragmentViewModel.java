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
    private AppCompatEditText mTxbName;
    private AppCompatEditText mTxbFirstname;
    private AppCompatEditText mTxbPhone;

    private boolean mEdit = true;
    private String mName;
    private String mFirstname;
    private String mPhone;
    private String mValidation;

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
        mTxbName = (AppCompatEditText) fragmentAccountBinding.getRoot().findViewById(R.id.txbName);
        mTxbFirstname = (AppCompatEditText) fragmentAccountBinding.getRoot().findViewById(R.id.txbFirstname);
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
        mTxbName.setEnabled(true);
        mTxbFirstname.setEnabled(true);
        mTxbPhone.setEnabled(true);
        mTxbName.requestFocus();
        mFab.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_save_white_24dp));
    }

    private void save() {
        mName = mTxbName.getText().toString();
        mFirstname = mTxbFirstname.getText().toString();
        mPhone = mTxbPhone.getText().toString();
        if (checkFields(mName, mFirstname, mPhone)) {
            mTxbName.setEnabled(false);
            mTxbFirstname.setEnabled(false);
            mTxbPhone.setEnabled(false);
            mFab.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mode_edit_white_24dp));
            Call<JsonObject> call = ClicketInstance.getClicketserviceInstance().editUser(getUser().getEmail(), mName, mFirstname, mPhone, String.valueOf(getUser().getInvoice_amount()), AuthHelper.getAuthToken(mContext));
            call.enqueue(saveCallback);
        } else {
            showSnackbar(mValidation);
        }
    }

    private boolean checkFields(String name, String firstname, String phone) {
        boolean isValid = true;

        if (name.equals("") || firstname.equals("") || phone.equals("")) {
            mValidation = "Error when saving data: please supply all fields";
            isValid = false;
        } if (!Utils.isPhoneValid(phone)) {
            mValidation = "Error when saving data: not a valid phone number. Format: 0032499999999.";
            isValid = false;
        }

        return isValid;
    }

    private Callback<JsonObject> saveCallback = new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (response.isSuccessful() && response.body().getAsJsonPrimitive("success").getAsBoolean()) {
                showSnackbar(response.body().getAsJsonPrimitive("info").getAsString());
                ((App) ((Activity) mContext).getApplication()).getUser().setName(mName);
                ((App) ((Activity) mContext).getApplication()).getUser().setFirstname(mFirstname);
                ((App) ((Activity) mContext).getApplication()).getUser().setPhone(mPhone);
                mListener.setDrawerItems(mFirstname + " " + mName);
            } else {
                showSnackbar("Error when saving data: try again later");
                mTxbName.setEnabled(false);
                mTxbFirstname.setEnabled(false);
                mTxbPhone.setEnabled(false);
                mFab.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mode_edit_white_24dp));

                mTxbName.setText(getUser().getName());
                mTxbFirstname.setText(getUser().getFirstname());
                mTxbPhone.setText(getUser().getPhone());
            }
            notifyPropertyChanged(BR.viewmodel);
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.d("Error", t.getMessage());
            showSnackbar("Error when saving data: try again later");
            mTxbName.setEnabled(false);
            mTxbFirstname.setEnabled(false);
            mTxbPhone.setEnabled(false);
            mFab.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_mode_edit_white_24dp));

            mTxbName.setText(getUser().getName());
            mTxbFirstname.setText(getUser().getFirstname());
            mTxbPhone.setText(getUser().getPhone());
            notifyPropertyChanged(BR.viewmodel);
        }
    };

    private void showSnackbar(String message) {
        Snackbar snackbar = Snackbar.make(mTxbPhone, message, Snackbar.LENGTH_LONG);
        CustomSnackbar.colorSnackBar(snackbar).show();
    }

}