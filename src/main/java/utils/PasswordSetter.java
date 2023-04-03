package utils;

import encryption.Crypto;

import javax.crypto.SecretKey;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class PasswordSetter {
    public static void put(final String masterPassword, String username, String password){
        byte[] paddedAddress = Crypto.padString(username);
        byte[] paddedPass = Crypto.padString(password);

        try(OutputStream os1 = new FileOutputStream("../saltAndIV.txt", true);
            OutputStream os2 = new FileOutputStream("../passdb.txt", true)){

            byte[] salt = Crypto.getNextSalt();
            SecretKey key = Crypto.generateKey(masterPassword.toCharArray(), salt);

            byte[] iv = Crypto.getIV();
            os1.write(salt);
            os1.write(iv);
            byte[] paddedPair = new byte[512];


            System.arraycopy(paddedAddress, 0, paddedPair, 0, 256);
            System.arraycopy(paddedPass, 0, paddedPair, 256, 256);

            paddedPair = Crypto.encrypt(paddedPair, key, iv);

            os2.write(paddedPair);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
