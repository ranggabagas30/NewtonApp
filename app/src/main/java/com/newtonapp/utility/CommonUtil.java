package com.newtonapp.utility;

import android.text.TextUtils;
import android.util.Log;

import com.auth0.android.jwt.Claim;
import com.auth0.android.jwt.DecodeException;
import com.auth0.android.jwt.JWT;

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

    public static JWT getJWTtokenDecrypt(String token) throws DecodeException {
        String TAG = "JWT";
        JWT jwtTokenDecrypt = new JWT(token);
        Claim unameClaim = jwtTokenDecrypt.getClaim(Constants.CLAIM_USERNAME);
        Claim kdcabClaim = jwtTokenDecrypt.getClaim(Constants.CLAIM_KDCAB);
        Claim idtekClaim = jwtTokenDecrypt.getClaim(Constants.CLAIM_IDTECHNICIAN);
        Claim catClaim   = jwtTokenDecrypt.getClaim(Constants.CLAIM_CATEGORY);
        Claim iatClaim   = jwtTokenDecrypt.getClaim(Constants.CLAIM_ISSUED_AT);
        Claim nbfClaim   = jwtTokenDecrypt.getClaim(Constants.CLAIM_NOT_BEFORE);
        Log.d(TAG, "token -> " + jwtTokenDecrypt.toString());
        Log.d(TAG, "{\n" +
                        "\tuname: " + unameClaim.asString() + ",\n" +
                        "\tkdcab: " + kdcabClaim.asString() + ",\n" +
                        "\tidtek: " + idtekClaim.asString() + ",\n" +
                        "\tcat: " + catClaim.asString() + ",\n" +
                        "\tiat: " + iatClaim.asString() + ",\n" +
                        "\tnbf: " + nbfClaim.asString() + "\n" +
                    "}");
        return jwtTokenDecrypt;
    }
}
