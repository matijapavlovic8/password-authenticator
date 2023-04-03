package utils;

import encryption.Crypto;

import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ForceChangeRemove {
    static int forcedLen;
    static int sivLen;
    static byte[] forcedDb;
    static byte[] sivDb;
    private static void read() {
        try (InputStream is1 = new FileInputStream("../saltAndIvForced.txt");
             InputStream is2 = new FileInputStream("../forced.txt")) {

            forcedLen = (int) Files.size(Path.of("../forced.txt"));
            sivLen = (int) Files.size(Path.of("../saltAndIvForced.txt"));

            forcedDb = new byte[forcedLen];
            sivDb = new byte[sivLen];

            is2.read(forcedDb);
            is1.read(sivDb);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void remove(String masterPassword, String username) {
        read();
        boolean deleted = false;
        try (OutputStream os1 = new FileOutputStream("../saltAndIvForced.txt");
             OutputStream os2 = new FileOutputStream("../forced.txt")) {

            for(int i = 0, j = 0; i < forcedLen; i += 272, j += 28){
                byte[] encrypted = new byte[272];
                System.arraycopy(forcedDb, i, encrypted, 0, 272);

                byte[] salt = new byte[16];
                byte[] iv = new byte[12];

                System.arraycopy(sivDb, j, salt, 0, 16);
                System.arraycopy(sivDb, j + 16, iv, 0, 12);
                SecretKey key = Crypto.generateKey(masterPassword.toCharArray(), salt);
                byte[] decrypted = Crypto.decrypt(encrypted, key, iv);
                if (decrypted != null) {
                    String pass = new String(decrypted);
                    String usernameCrypted = pass.substring(0, pass.indexOf(" "));

                    if(usernameCrypted.equals(username)) {
                        System.out.println("Password successfully changed!");
                        deleted = true;
                    } else {
                        os2.write(encrypted);
                        os1.write(sivDb, j, 28);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Password authenticator wasn't initialized properly!");
            System.exit(1);
        }
        if(!deleted)
            System.out.println("No user with this username found!");
    }
}
