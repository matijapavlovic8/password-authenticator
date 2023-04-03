package application;


import utils.PasswordStrengthChecker;

import java.io.Console;

public class Login {
    public static void main(String[] args) {
        System.out.println
                (",------.                                                 ,--.   ,---.            ,--.  ,--.                 ,--.     ,--.   \n" +
                "|  .--. ' ,--,--. ,---.  ,---.,--.   ,--. ,---. ,--.--.,-|  |  /  O  \\ ,--.,--.,-'  '-.|  ,---.  ,--.  ,--./   |    /    \\  \n" +
                "|  '--' |' ,-.  |(  .-' (  .-'|  |.'.|  || .-. ||  .--' .-. | |  .-.  ||  ||  |'-.  .-'|  .-.  |  \\  `'  / `|  |   |  ()  | \n" +
                "|  | --' \\ '-'  |.-'  `).-'  `)   .'.   |' '-' '|  |  \\ `-' | |  | |  |'  ''  '  |  |  |  | |  |   \\    /   |  |.--.\\    /  \n" +
                "`--'      `--`--'`----' `----''--'   '--' `---' `--'   `---'  `--' `--' `----'   `--'  `--' `--'    `--'    `--''--' `--'   ");


        Console console = System.console();

        if (args.length != 1) {
            throw new IllegalArgumentException("Program takes exactly one argument!");
        }

        String username = args[0];
        String fetchedPass = UserMgmt.getPassword(username);
        String password = "";
        int counter = 0;
        if (UserMgmt.checkForced(username)) {
            System.out.println("You are forced to change your password by the system administrator!");
            char[] arr = console.readPassword("Enter your old password: ");
            password = new String(arr);
            String newPassword = "";
            if (password.equals(fetchedPass)) {
                arr = console.readPassword("New password: ");
                newPassword = new String(arr);
                if (!PasswordStrengthChecker.check(newPassword)) {
                    System.out.println("Password must be at least 8 characters long and contain at least one" +
                            "capital letter, one number and one non alphanumeric character!");
                }
                arr = console.readPassword("Repeat password: ");
                String repeated = new String(arr);
                if (repeated.equals(newPassword)) {
                    UserMgmt.forcedChangeDone(username, newPassword);
                    System.out.println("Login successful!");
                    System.exit(0);
                } else {
                    System.out.println("Username or password incorrect!");
                    System.exit(0);
                }
            }
        }

        while (counter < 3) {
            counter++;
            char[] arr = console.readPassword("Password: ");
            password = new String(arr);

            if (password.equals(fetchedPass)) {
                System.out.println("Login successful!");
                System.exit(0);
            }
            System.out.println("Username or password incorrect! " + (3 - counter) + " tries left!");
        }

        System.exit(0);

    }
}
