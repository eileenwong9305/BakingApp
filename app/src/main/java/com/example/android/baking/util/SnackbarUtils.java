package com.example.android.baking.util;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackbarUtils {

    public static void showSnackbar(View v,String snackbarText) {
        if (v == null || snackbarText == null) {
            return;
        }
        Snackbar.make(v, snackbarText, Snackbar.LENGTH_SHORT).show();
    }
}
