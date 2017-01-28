package be.nielsbril.clicket.app.views;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;

import be.nielsbril.clicket.app.R;
import be.nielsbril.clicket.app.api.ClicketInstance;
import be.nielsbril.clicket.app.helpers.AuthHelper;
import be.nielsbril.clicket.app.helpers.Contract;
import be.nielsbril.clicket.app.helpers.CustomSnackBar;
import be.nielsbril.clicket.app.helpers.Interfaces;
import be.nielsbril.clicket.app.helpers.Utils;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RuntimePermissions
public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener,
        Interfaces.onAccountRegisteredListener {

    public static Context mContext;

    private ProgressDialog progressDialog;
    private AppCompatEditText mTxbEmail;
    private AppCompatEditText mTxbPassword;

    private String mValidation;

    private OnAccountsUpdateListener mOnAccountsUpdateListener;

    private AccountManager mAccountManager;
    private AccountAuthenticatorResponse mAccountAuthenticatorResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(LoginActivity.this, R.style.customDialog);

        mTxbEmail = (AppCompatEditText) findViewById(R.id.txbEmail);
        mTxbPassword = (AppCompatEditText) findViewById(R.id.txbPassword);

        findViewById(R.id.btnLogin).setOnClickListener(this);
        findViewById(R.id.txtRegisterLink).setOnClickListener(this);

        mAccountManager = AccountManager.get(this);
        mAccountAuthenticatorResponse = this.getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (mAccountAuthenticatorResponse != null) {
            mAccountAuthenticatorResponse.onRequestContinued();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                showDialog();
                onSignInClicked();
                break;
            case R.id.txtRegisterLink:
                showRegisterActivity();
                break;
        }
    }

    private void showDialog() {
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating ...");
        progressDialog.show();
    }

    private void stopDialog() {
        progressDialog.dismiss();
    }

    private void onSignInClicked() {
        String mEmail = mTxbEmail.getText().toString();
        String mPassword = mTxbPassword.getText().toString();

        if (checkCredentials(mEmail, mPassword)) {
            LoginActivityPermissionsDispatcher.loginWithCheck(this, mEmail, mPassword);
        } else {
            showLoginError();
        }
    }

    private boolean checkCredentials(String email, String password) {
        boolean isValid = true;

        if (email.equals("") || password.equals("")) {
            isValid = false;
            mValidation = "please fill in all fields";
        } else if (!Utils.isEmailValid(email)) {
            isValid = false;
            mValidation = "not a valid email address";
        }

        return isValid;
    }

    @NeedsPermission(Manifest.permission.GET_ACCOUNTS)
    public void login(String email, String password) {
        Call<JsonObject> call = ClicketInstance.getClicketserviceInstance().login(email, password);
        call.enqueue(loginCallback);
    }

    Callback<JsonObject> loginCallback = new Callback<JsonObject>() {
        @Override
        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
            if (response.isSuccessful() && response.body().getAsJsonPrimitive("success").getAsBoolean()) {
                JsonObject body = response.body();
                String token = body.getAsJsonPrimitive("token").getAsString();
                String email = body.getAsJsonPrimitive("email").getAsString();

                LoginActivityPermissionsDispatcher.addAccountWithCheck(LoginActivity.this, email, token);
            } else {
                mValidation = (!response.body().getAsJsonPrimitive("info").getAsString().equals("") ? response.body().getAsJsonPrimitive("info").getAsString() : "unknown error");
                showLoginError();
            }
        }

        @Override
        public void onFailure(Call<JsonObject> call, Throwable t) {
            Log.d("Error", t.getMessage());
            mValidation = "unknown error";
            showLoginError();
        }
    };

    @NeedsPermission(Manifest.permission.GET_ACCOUNTS)
    public void addAccount(String email, String token) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Account[] accountsByType = mAccountManager.getAccountsByType(Contract.ACCOUNT_TYPE);
        Account account;

        if (accountsByType.length == 0) {
            account = new Account(email, Contract.ACCOUNT_TYPE);
            mAccountManager.addAccountExplicitly(account, null, null);
            mAccountManager.setAuthToken(account, "access_token", token);
        } else if (!email.equals(accountsByType[0].name)) {
            AuthHelper.logUserOff(this, accountsByType[0]);
            account = new Account(email, Contract.ACCOUNT_TYPE);
            mAccountManager.addAccountExplicitly(account, null, null);
            mAccountManager.setAuthToken(account, "access_token", token);
        } else {
            account = accountsByType[0];
            mAccountManager.setAuthToken(account, "access_token", token);
        }

        Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, token);
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, email);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Contract.ACCOUNT_TYPE);

        if (mAccountAuthenticatorResponse != null) {
            Bundle bundle = intent.getExtras();
            intent.putExtra(AccountManager.KEY_AUTHTOKEN, token);
            intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, email);
            intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Contract.ACCOUNT_TYPE);
            mAccountAuthenticatorResponse.onResult(bundle);
        }

        setResult(RESULT_OK, intent);
        mContext = null;
        progressDialog.dismiss();
        finish();
    }

    private void showLoginError() {
        progressDialog.dismiss();
        Snackbar snackbar = Snackbar.make(mTxbEmail, "Error when logging in: " + mValidation, Snackbar.LENGTH_SHORT);
        CustomSnackBar.colorSnackBar(snackbar).show();
    }

    private void showRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!AuthHelper.isLoggedIn(this)) {
            ActivityCompat.finishAffinity(LoginActivity.this);
        }
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

    @Override
    public void onAccountRegistered(String email, String token) {
        LoginActivityPermissionsDispatcher.addAccountWithCheck(this, email, token);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LoginActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.GET_ACCOUNTS)
    public void showRationaleForAccounts(PermissionRequest request) {
        Utils.showRationaleDialog(this, "Accounts permission needed to log in", request);
    }

    @OnPermissionDenied(Manifest.permission.GET_ACCOUNTS)
    public void onAccountsDenied() {
        stopDialog();
        AuthHelper.logUserOff(this);
        Snackbar snackbar = Snackbar.make(mTxbEmail, "Accounts permission denied, consider accepting to use this app", Snackbar.LENGTH_SHORT);
        CustomSnackBar.colorSnackBar(snackbar).show();
    }

    @OnNeverAskAgain(Manifest.permission.GET_ACCOUNTS)
    public void onAccountsNeverAskAgain() {
        stopDialog();
        AuthHelper.logUserOff(this);
        Snackbar snackbar = Snackbar.make(mTxbEmail, "Accounts permission denied with never ask again", Snackbar.LENGTH_SHORT);
        CustomSnackBar.colorSnackBar(snackbar).show();
    }

}