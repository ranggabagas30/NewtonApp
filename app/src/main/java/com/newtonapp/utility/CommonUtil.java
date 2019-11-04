package com.newtonapp.utility;

import android.Manifest;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresPermission;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.DecodeException;
import com.auth0.android.jwt.JWT;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CommonUtil {

    public static int getRandomIntRange(int min, int max) {
        if (min >= max) throw new IllegalArgumentException("min should be lower than max");

        return new Random().nextInt((max - min) + 1) + min;
    }

    public static int getIndex(String keyword, String[] source) {
        for (int i = 0; i < source.length; i++) {
            if (keyword.equalsIgnoreCase(source[i])) return i;
        }
        return -1;
    }

    public static JWT getJWTtokenDecrypt(String token) throws DecodeException {
        String TAG = "JWT";
        JWT jwtTokenDecrypt = new JWT(token);
        Claim unameClaim = jwtTokenDecrypt.getClaim(Constants.CLAIM_USERNAME);
        Claim kdcabClaim = jwtTokenDecrypt.getClaim(Constants.CLAIM_KDCAB);
        Claim idtekClaim = jwtTokenDecrypt.getClaim(Constants.CLAIM_IDTECHNICIAN);
        Claim catClaim   = jwtTokenDecrypt.getClaim(Constants.CLAIM_CATEGORY);
        Claim iatClaim   = jwtTokenDecrypt.getClaim(Constants.CLAIM_ISSUED_AT);
        Claim nbfClaim   = jwtTokenDecrypt.getClaim(Constants.CLAIM_NOT_BEFORE);
        Claim otp        = jwtTokenDecrypt.getClaim(Constants.CLAIM_OTP);
        Claim sn         = jwtTokenDecrypt.getClaim(Constants.CLAIM_SN);

        Log.d(TAG, "token -> " + jwtTokenDecrypt.toString());
        Log.d(TAG, "{\n" +
                        "\tuname: " + unameClaim.asString() + ",\n" +
                        "\tsn: " + sn.asString() + ",\n" +
                        "\totp: " + otp.asString() + ",\n" +
                        "\tkdcab: " + kdcabClaim.asString() + ",\n" +
                        "\tidtek: " + idtekClaim.asString() + ",\n" +
                        "\tcat: " + catClaim.asString() + ",\n" +
                        "\tiat: " + iatClaim.asString() + ",\n" +
                        "\tnbf: " + nbfClaim.asString() + "\n" +
                    "}");
        return jwtTokenDecrypt;
    }

    @RequiresPermission(Manifest.permission.READ_PHONE_STATE)
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        DebugUtil.d("imei : " + imei);
        return imei;
    }

    public static long getPeriodMillis(long maxCount, long duration, TimeUnit durationTimeUnit) {
        if (durationTimeUnit == TimeUnit.SECONDS) duration *= 1000;
        return duration / maxCount;
    }
}
