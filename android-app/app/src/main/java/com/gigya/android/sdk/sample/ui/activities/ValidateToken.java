package com.gigya.android.sdk.sample.ui.activities;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Base64;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.RSAPublicKeySpec;

public class ValidateToken {

    /***
     * https://developers.gigya.com/display/GD/How+To+Validate+A+Gigya+id_token
     * @param tokenData
     * @param keySignatureString
     * @param publicKey
     * @param expString
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean validateTokeb(String tokenData, String keySignatureString, String publicKey, String expString) {
        String nString = publicKey;
        keySignatureString = keySignatureString.replace('-', '+'); // 62nd char of encoding
        keySignatureString = keySignatureString.replace('_', '/'); // 63rd char of encoding
        byte[] keySignature = Base64.getDecoder().decode(keySignatureString.getBytes());

        nString = nString.replace('-', '+'); // 62nd char of encoding
        nString = nString.replace('_', '/'); // 63rd char of encoding
        byte[] n = Base64.getDecoder().decode(nString.getBytes());
        byte[] e = Base64.getDecoder().decode(expString.getBytes());

        try {
            BigInteger nBigInt = new BigInteger(1, n);
            BigInteger eBigInt = new BigInteger(1, e);
            RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(nBigInt, eBigInt);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey rsa = fact.generatePublic(rsaPubKey);
            Signature rsaSig = Signature.getInstance("SHA256withRSA");
            rsaSig.initVerify(rsa);
            rsaSig.update(tokenData.getBytes("UTF-8"));
            boolean result =  rsaSig.verify(keySignature);
            if(result) {
                System.out.println("Valid Signature!");
                return true;
            }
            else {
                System.out.println("Invalid Signature");
            }
        }
        catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return false;
    }
}
