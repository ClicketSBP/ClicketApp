package be.nielsbril.clicket.app.helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;

import java.util.regex.Pattern;

import be.nielsbril.clicket.app.R;
import permissions.dispatcher.PermissionRequest;

public class Utils {

    private static final Pattern phonePattern = Pattern.compile("^((\\+|00)32\\s?|0)4(60|[789]\\d)(\\s?\\d{2}){3}$");

    public static void showRationaleDialog(Context context, String message, final PermissionRequest request) {
        new AlertDialog.Builder(context, R.style.customDialog)
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.proceed();
                    }
                })
                .setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setTitle("Permission denied")
                .setMessage(message)
                .show();
    }

    public static boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPhoneValid(String phone) { return phonePattern.matcher(phone).matches(); }

    public static boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

}