package command;

import utils.ForceChangeSetter;

public class ForcepassCommand {

    public static void force(String masterPassword, String username) {
        ForceChangeSetter.set(masterPassword, username);
        System.out.println("User will be forced to change the password!");
    }
}
