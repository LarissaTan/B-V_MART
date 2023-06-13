package com.example.bv_mart.util;

import android.widget.Toast;

public class ToastUtil {
    private ToastUtil() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort( String message)
    {
            Toast.makeText(AppContext.getInstance(), message, Toast.LENGTH_SHORT).show();
    }
}
