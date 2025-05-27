package org.yavar007.misc;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class CryptoHelper {
    private static final String secret = "b007clientsCnema";
    public static String encrypt(String strToEncrypt) {
        try {
            IvParameterSpec iv = generateIv();
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] encrypted = cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(iv.getIV()) + ":" + Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            return e.getMessage();
        }
    }
    public static String decrypt(String strToDecrypt) {
        try {
            String[] parts = strToDecrypt.split(":");
            IvParameterSpec iv = new IvParameterSpec(Base64.getDecoder().decode(parts[0]));
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(parts[1]));
            return new String(original, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "";
        }
    }
    private static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}
