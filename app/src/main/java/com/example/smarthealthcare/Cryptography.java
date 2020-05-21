package com.example.smarthealthcare;

import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Cryptography {

    String name;
    String Desc;

    public byte encryptkey[] = {9,115,51,86,105,4,-31,-23,-68,88,17,20,3,-105,119,-53};
    public Cipher cipher,dicipher;
    public SecretKeySpec secretKeySpec;

    Cryptography() throws NoSuchPaddingException, NoSuchAlgorithmException {
        cipher = Cipher.getInstance("AES");
    }
    public static String encrypt(String name, String Desc) throws Exception {
        SecretKeySpec key = (SecretKeySpec) generateKey(Desc);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encal = c.doFinal(name.getBytes());
        String encryval = Base64.encodeToString(encal,Base64.DEFAULT);


        return encryval;
    }
    public static String decrypt(String outptString, String toString) throws Exception {
        SecretKeySpec key = generateKey(toString);
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decodeval = Base64.decode(outptString,Base64.DEFAULT);
        byte[] deval = (byte[]) c.doFinal(decodeval,0,decodeval.length);
        String decrt = new String(deval);
        Log.d("key", String.valueOf(key));
        return decrt;
    }

    public static SecretKeySpec generateKey(String toString1) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = toString1.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"ECC");
        return secretKeySpec;
    }


}
