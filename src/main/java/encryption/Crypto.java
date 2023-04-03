package encryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

public class Crypto {

    public static SecretKey generateKey(final char[] masterPassword, byte[] salt) {
          try {
              SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
              KeySpec keySpec = new PBEKeySpec(masterPassword, salt, 100, 256);
              return new SecretKeySpec(factory.generateSecret(keySpec).getEncoded(), "AES");
          } catch (NoSuchAlgorithmException | InvalidKeySpecException e){
              throw new RuntimeException(e.getMessage());
          }
    }

    public static byte[] getNextSalt(){
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    public static byte[] getIV(){
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public static byte[] encrypt(byte[] plaintext, SecretKey secretKey, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
            return cipher.doFinal(plaintext);
        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] decrypt(byte[] encrypted, SecretKey secretKey, byte[] iv){
        try{
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
            return cipher.doFinal(encrypted);
        } catch (InvalidAlgorithmParameterException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (BadPaddingException ignored){

        }
        return null;
    }

    public static byte[] padString(String str){
        byte[] res;
        str = str + (" ").repeat(256 - str.length());
        res = str.getBytes(StandardCharsets.UTF_8);
        return res;
    }

}
