package com.newtonapp.utility;

import android.text.TextUtils;

import java.util.Random;

public class CommonUtil {

    public static boolean isLoginValidated(String username, String password) {
        boolean isValid = false;
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            isValid = true;
        }
        return isValid;
    }

    public static int getRandomIntRange(int min, int max) {
        if (min >= max) throw new IllegalArgumentException("min should be lower than max");

        return new Random().nextInt((max - min) + 1) + min;
    }
}
