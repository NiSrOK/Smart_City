package com.example.smartcity;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String hash(String in){
        String codedIn = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(in.getBytes());
            BigInteger bigInt = new BigInteger(1, digest.digest());
            codedIn = bigInt.toString(16);
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return codedIn;
    }
}
