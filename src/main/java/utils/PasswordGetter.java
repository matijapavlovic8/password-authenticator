package utils;

import encryption.Crypto;


import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * {@code PasswordGetter}
 */
public class PasswordGetter {
    public static String get(final String masterPassword, String username) {
        try (InputStream is1 = new FileInputStream("../saltAndIv.txt");
             InputStream is2 = new FileInputStream("../passdb.txt")) {

            int dbLen = (int) Files.size(Path.of("../passdb.txt"));
            int sivLen = (int) Files.size(Path.of("../saltAndIv.txt"));

            byte[] passDb = new byte[dbLen];
            byte[] sivDb = new byte[sivLen];

            is2.read(passDb);
            is1.read(sivDb);

            for(int i = 0, j = 0; i < dbLen; i += 528, j += 28){
                byte[] encrypted = new byte[528];
                System.arraycopy(passDb, i, encrypted, 0, 528);

                byte[] salt = new byte[16];
                byte[] iv = new byte[12];

                System.arraycopy(sivDb, j, salt, 0, 16);
                System.arraycopy(sivDb, j + 16, iv, 0, 12);
                SecretKey key = Crypto.generateKey(masterPassword.toCharArray(), salt);
                byte[] decrypted = Crypto.decrypt(encrypted, key, iv);
                if (decrypted != null) {
                    String pass = new String(decrypted);
                    String addressCrypted = pass.substring(0, pass.indexOf(" "));
                    pass = pass.substring(256, pass.length() - 1);
                    pass = pass.substring(0, pass.indexOf(" "));
                    if(addressCrypted.equals(username))
                        return pass;

                }
            }
        } catch (IOException e) {
            System.out.println("Password manager wasn't initialized!");
            System.exit(1);
        }
        return null;
    }

}
