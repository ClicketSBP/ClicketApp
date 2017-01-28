package be.nielsbril.clicket.app.helpers;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class CustomSnackBar {

    private static final int black = 0xff172430;
    private static final int white = 0xffeeeff7;

    private static View getSnackBarLayout(Snackbar snackbar) {
        if (snackbar != null) {
            return snackbar.getView();
        }
        return null;
    }

    public static Snackbar colorSnackBar(Snackbar snackbar) {
        View backgroundView = getSnackBarLayout(snackbar);
        if (backgroundView != null) {
            backgroundView.setBackgroundColor(black);
        }

        View colorView = snackbar.getView();
        TextView textView = (TextView) colorView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(white);

        return snackbar;
    }

}