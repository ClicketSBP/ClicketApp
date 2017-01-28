package be.nielsbril.clicket.app.views;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;

import be.nielsbril.clicket.app.R;
import be.nielsbril.clicket.app.api.ClicketInstance;
import be.nielsbril.clicket.app.api.ClicketService;
import be.nielsbril.clicket.app.helpers.CustomSnackBar;
import be.nielsbril.clicket.app.helpers.Interfaces;
import be.nielsbril.clicket.app.helpers.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity
        implements View.OnClickListener {

    private ProgressDialog progressDialog;
    private AppCompatEditText mTxbEmail;
    private AppCompatEditText mTxbLastname;
    private AppCompatEditText mTxbFirstname;
    private AppCompatEditText mTxbPhone;
    private AppCompatEditText mTxbPassword;
    private AppCompatEditText mTxbConfirmPassword;

    private String mValidation;

    private OnAccountsUpdateListener mOnAccountsUpdateListener;

    private AccountManager mAccountManager;

    private Interfaces.onAccountRegisteredListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener = (Interfaces.onAccountRegisteredListener) LoginActivity.mContext;

        setContentView(R.layout.activity_register);
        progressDialog = new ProgressDialog(RegisterActivity.this, R.style.customDialog);

        mTxbEmail = (AppCompatEditText) findViewById(R.id.txbEmail);
        mTxbLastname = (AppCompatEditText) findViewById(R.id.txbLastname);
        mTxbFirstname = (AppCompatEditText) findViewById(R.id.txbFirstname);
        mTxbPhone = (AppCompatEditText) findViewById(R.id.txbPhone);
        mTxbPassword = (AppCompatEditText) findViewById(R.id.txbPassword);
        mTxbConfirmPassword = (AppCompatEditText) findViewById(R.id.txbConfirmPassword);

        findViewById(R.id.btnRegister).setOnClickListener(this);
        findViewById(R.id.txtLoginLink).setOnClickListener(this);

        mAccountManager = AccountManager.get(this);
        AccountAuthenticatorResponse mAccountAuthenticatorResponse = this.getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRegister:
                showDialog();
                onRegisterClicked();
                break;
            case R.id.txtLoginLink:
                finish();
                break;
        }
    }

    private void showDialog() {
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registering ...");
        progressDialog.show();
    }

    private void stopDialog() {
        progressDialog.dismiss();
    }

    private void onRegisterClicked() {
        String email = mTxbEmail.getText().toString();
        String lastname = mTxbLastname.getText().toString();
        String firstname = mTxbFirstname.getText().toString();
        String phone = mTxbPhone.getText().toString();
        String password = mTxbPassword.getText().toString();
        String confirmPassword = mTxbConfirmPassword.getText().toString();

        if (checkCredentials(email, lastname, firstname, phone, password, confirmPassword)) {
            registerAccount(email, lastname, firstname, phone, password);
        } else {
            showRegisterError();
        }
    }

    private boolean checkCredentials(String email, String lastname, String firstname, String phone, String password, String confirmPassword) {
        boolean isValid = true;

        if (email.equals("") || lastname.equals("") || firstname.equals("") || phone.equals("") || password.equals("") || confirmPassword.equals("")) {
            isValid = false;
            mValidation = "please fill in all fields";
        } else if (!Utils.isEmailValid(email)) {
            isValid = false;
            mValidation = "not a valid email address";
        } else if (!Utils.isPhoneValid(phone)) {
            isValid = false;
            mValidation = "not a valid phone number";
        } else if (!Utils.isPasswordValid(password)) {
            isValid = false;
            mValidation = "password should be at least 8 characters";
        } else if (!password.equals(confirmPassword)) {
            isValid = false;
            mValidation = "passwords don't match";
        }

        return isValid;
    }

    private void registerAccount(String email, String lastname, String firstname, String phone, String password) {
        Call<JsonObject> call = ClicketInstance.getClicketserviceInstance().register(email, lastname, firstname, phone, password);
        call.enqueue(registerCallback);
    }

    Callback<JsonObject> registerCallback = new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (response.isSuccessful()) {
                JsonObject body = response.body();
                String token = body.getAsJsonPrimitive("token").getAsString();
                String email = body.getAsJsonPrimitive("email").getAsString();

                mListener.onAccountRegistered(email, token);
                stopDialog();
                finish();
            } else {
                mValidation = "unknown error";
                showRegisterError();
            }
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.d("Error", t.getMessage());
            mValidation = "unknown error";
            showRegisterError();
        }
    };

    private void showRegisterError() {
        progressDialog.dismiss();
        Snackbar snackbar = Snackbar.make(mTxbEmail, "Error when registering: " + mValidation, Snackbar.LENGTH_SHORT);
        CustomSnackBar.colorSnackBar(snackbar).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mOnAccountsUpdateListener = new OnAccountsUpdateListener() {
            @Override
            public void onAccountsUpdated(Account[] accounts) {

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mAccountManager.addOnAccountsUpdatedListener(mOnAccountsUpdateListener, null, true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mOnAccountsUpdateListener != null) {
            mAccountManager.removeOnAccountsUpdatedListener(mOnAccountsUpdateListener);
        }
    }

}