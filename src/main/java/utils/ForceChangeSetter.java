package utils;

import encryption.Crypto;

import javax.crypto.SecretKey;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ForceChangeSetter {

    public static void set(String masterPassword, String username) {

        byte[] paddedAddress = Crypto.padString(username);

        try (OutputStream os1 = new FileOutputStream("../forced.txt", true);
             OutputStream os2 = new FileOutputStream("../saltAndIvForced.txt", true)) {

            byte[] salt = Crypto.getNextSalt();
            byte[] iv = Crypto.getIV();

            SecretKey key = Crypto.generateKey(masterPassword.toCharArray(), salt);

            os2.write(salt);
            os2.write(iv);

            byte[] paddedPair = new byte[256];

            System.arraycopy(paddedAddress, 0, paddedPair, 0, 256);

            paddedPair = Crypto.encrypt(paddedPair, key, iv);

            os1.write(paddedPair);

        } catch (IOException e) {
            throw new RuntimeException("Something went wrong!");
        }
    }
}
