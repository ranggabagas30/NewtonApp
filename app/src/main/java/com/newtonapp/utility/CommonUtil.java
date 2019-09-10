package com.newtonapp.utility;

import android.text.TextUtils;

public class CommonUtil {

    public static boolean isLoginValidated(String username, String password) {
        boolean isValid = false;
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            isValid = true;
        }
        return isValid;
    }
}
