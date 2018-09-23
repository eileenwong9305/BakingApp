package com.example.android.baking.util;

import java.util.Locale;

public class BindingUtils {

    public static String decimalFormat(float f) {
        if (f == (long) f) {
            return String.format(Locale.US, "%d", (long) f);
        } else {
            return String.format("%s", f);
        }
    }

}
