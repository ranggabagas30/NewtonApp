package com.newtonapp.utility;

import android.text.TextUtils;

public class StringUtil {

    public static String UniqueKey(String keyId, String keyName) throws NullPointerException {
        if (TextUtils.isEmpty(keyId))
            throw new NullPointerException("ERROR: key id is null");

        if (TextUtils.isEmpty(keyName))
            throw new NullPointerException("ERROR: key name is null");

        return keyId.concat("_").concat(keyName);
    }
}
