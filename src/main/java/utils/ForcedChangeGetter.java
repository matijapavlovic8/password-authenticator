package utils;

import encryption.Crypto;

import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ForcedChangeGetter {


    public static boolean get(final String masterPassword, String username) {
        try (InputStream is1 = new FileInputStream("../saltAndIvForced.txt");
             InputStream is2 = new FileInputStream("../forced.txt")) {

            int forcedLen = (int) Files.size(Path.of("../forced.txt"));
            int sivLen = (int) Files.size(Path.of("../saltAndIvForced.txt"));

            byte[] forced = new byte[forcedLen];
            byte[] sivDb = new byte[sivLen];

            is2.read(forced);
            is1.read(sivDb);



            for(int i = 0, j = 0; i < forcedLen; i += 272, j += 28){
                byte[] encrypted = new byte[272];
                System.arraycopy(forced, i, encrypted, 0, 272);

                byte[] salt = new byte[16];
                byte[] iv = new byte[12];

                System.arraycopy(sivDb, j, salt, 0, 16);
                System.arraycopy(sivDb, j + 16, iv, 0, 12);
                SecretKey key = Crypto.generateKey(masterPassword.toCharArray(), salt);
                byte[] decrypted = Crypto.decrypt(encrypted, key, iv);
                if (decrypted != null) {
                    String pass = new String(decrypted);
                    String usernameCrypted = pass.substring(0, pass.indexOf(" "));
                    if(usernameCrypted.equals(username))
                        return true;

                }
            }
        } catch (IOException e) {
            System.out.println("Password manager wasn't initialized!");
            System.exit(1);
        }
        return false;
    }
}
