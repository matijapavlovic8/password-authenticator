package command;

import encryption.Crypto;

import javax.crypto.SecretKey;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class DelCommand {
    public static boolean update = false;
    static int dbLen;
    static int sivLen;
    static byte[] passDb;
    static byte[] sivDb;
    private static void read() {
        try (InputStream is1 = new FileInputStream("../saltAndIv.txt");
             InputStream is2 = new FileInputStream("../passdb.txt")) {

            dbLen = (int) Files.size(Path.of("../passdb.txt"));
            sivLen = (int) Files.size(Path.of("../saltAndIv.txt"));

            passDb = new byte[dbLen];
            sivDb = new byte[sivLen];

            is2.read(passDb);
            is1.read(sivDb);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        public static void delete(String masterPassword, String username) {
        boolean deleted = false;
        read();
        try (OutputStream os1 = new FileOutputStream("../saltAndIv.txt");
             OutputStream os2 = new FileOutputStream("../passdb.txt")) {

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
                    String usernameCrypted = pass.substring(0, pass.indexOf(" "));

                    if(usernameCrypted.equals(username)) {
                        if (!update)
                            System.out.println("User successfully removed!");
                        deleted = true;
                    } else {
                        os2.write(encrypted);
                        os1.write(sivDb, j, 28);

                    }
                }

            }
        } catch (IOException e) {
            System.out.println("Password manager wasn't initialized!");
            System.exit(1);
        }
        if (!deleted)
            System.out.println("No user with this username found!");
    }
}
