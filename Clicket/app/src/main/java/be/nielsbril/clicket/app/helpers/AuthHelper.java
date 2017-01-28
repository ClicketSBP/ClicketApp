package be.nielsbril.clicket.app.helpers;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.gson.JsonObject;

import java.io.IOException;

import be.nielsbril.clicket.app.api.ClicketInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthHelper {

    private static String mToken;
    private static Account mAccount;

    public static Account getAccount(Context context) {
        AccountManager accountManager = AccountManager.get(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Account[] accounts = accountManager.getAccountsByType(Contract.ACCOUNT_TYPE);

        if (accounts.length > 0) {
            mAccount = accounts[0];
            return mAccount;
        } else {
            return null;
        }
    }

    public static String getEmail(Context context) {
        return mAccount.name;
    }

    public static String getAuthToken(Context context) {
        if (mToken.equals("")) {
            AccountManager accountManager = AccountManager.get(context);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Account[] accounts = accountManager.getAccountsByType(Contract.ACCOUNT_TYPE);

            if (accounts.length > 0) {
                mAccount = accounts[0];
                try {
                    mToken = new TokenTask(context).execute().get();
                } catch (Exception e) {
                    Log.d("Error: ", e.getMessage());
                    return "";
                }
            } else {
                return null;
            }
        }

        return mToken;
    }

    private static class TokenTask extends AsyncTask<Void, Void, String> {
        Context context;

        public TokenTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                AccountManager accountManager = AccountManager.get(context);
                return accountManager.blockingGetAuthToken(mAccount, "access_token", false);
            } catch (Exception e) {
                Log.d("Error: ", e.getMessage());
                return "";
            }
        }
    }

    public static Boolean isTokenExpired(Context context) {
        boolean expired = false;

        Call<JsonObject> call = ClicketInstance.getClicketserviceInstance().user(getAuthToken(context));
        try {
            Response<JsonObject> response = call.execute();
            if (response.isSuccessful()) {
                expired = true;
            }
        } catch (IOException e) {
            Log.d(AuthHelper.class.getName(), "Error: " + e.getMessage());
        }

        return expired;
    }

    public static Boolean isTokenValid(Context context) {
        return true;
    }

    public static Boolean isLoggedIn(Context context) {
        AccountManager accountManager = AccountManager.get(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        Account[] accounts = accountManager.getAccountsByType(Contract.ACCOUNT_TYPE);

        return accounts.length > 0;
    }

    public static void logUserOff(Context context) {
        AccountManager accountManager = AccountManager.get(context);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Account[] accounts = accountManager.getAccountsByType(Contract.ACCOUNT_TYPE);

        for (Account account : accounts) {
            removeAccount(context, account);
        }
    }

    public static void logUserOff(Context context, Account account) {
        removeAccount(context, account);
    }

    private static void removeAccount(Context context, Account account) {
        AccountManager accountManager = AccountManager.get(context);

        if (Build.VERSION.SDK_INT >= 22) {
            accountManager.removeAccount(account, (Activity) context, null, null);
        } else {
            accountManager.removeAccount(account, null, null);
        }
    }

}