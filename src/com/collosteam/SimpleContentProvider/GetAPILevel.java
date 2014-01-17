package com.collosteam.SimpleContentProvider;

import android.os.Build;

/**
 * Created by Droid on 17.01.14.
 */
public class GetAPILevel {
    public static final int get() {
        return Build.VERSION.SDK_INT;
    }
}
