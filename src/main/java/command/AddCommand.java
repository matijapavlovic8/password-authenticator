package command;


import application.UserMgmt;
import utils.PasswordSetter;
import utils.PasswordStrengthChecker;

import java.io.Console;
import java.util.Arrays;

public class AddCommand {
    public static boolean update;

    public static void add(String masterPassword, String username) {

        Console console = System.console();

        char[] pass = console.readPassword("Password: ");
        char[] passRepeat;
        if (PasswordStrengthChecker.check(new String(pass))) {
            passRepeat = console.readPassword("Repeat password: ");
            if (!Arrays.equals(pass, passRepeat)) {
                System.out.println("User add failed. Password mismatch.");
                System.exit(0);
            }
            String password = new String(pass);
            PasswordSetter.put(masterPassword, username, password);
            if (!update)
                System.out.println("User " + username + " successfully added!");


        } else {
            System.out.println("Password must be at least 8 characters long and contain at least one" +
                    "capital letter, one number and one non alphanumeric character!");
            System.exit(0);
        }


    }

}
