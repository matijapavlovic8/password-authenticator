package application;

import command.AddCommand;
import command.DelCommand;
import command.ForcepassCommand;
import command.PasswdCommand;
import utils.ForceChangeRemove;
import utils.ForcedChangeGetter;
import utils.PasswordGetter;
import utils.PasswordSetter;

import java.io.File;
import java.io.IOException;

public class UserMgmt {

    private static String masterPassword = "master";
    public static String getPassword(String username) {
        return PasswordGetter.get(masterPassword, username);
    }
    public static boolean checkForced(String username) { return ForcedChangeGetter.get(masterPassword, username); }
    public static void forcedChangeDone(String username, String newPass) {
        DelCommand.update = true;
        ForceChangeRemove.remove(masterPassword, username);
        DelCommand.delete(masterPassword, username);
        PasswordSetter.put(masterPassword, username, newPass);
        DelCommand.update = false;
    }
    public static void main(String[] args) {
        System.out.println
                (",------.                                                 ,--.   ,---.            ,--.  ,--.                 ,--.     ,--.   \n" +
                "|  .--. ' ,--,--. ,---.  ,---.,--.   ,--. ,---. ,--.--.,-|  |  /  O  \\ ,--.,--.,-'  '-.|  ,---.  ,--.  ,--./   |    /    \\  \n" +
                "|  '--' |' ,-.  |(  .-' (  .-'|  |.'.|  || .-. ||  .--' .-. | |  .-.  ||  ||  |'-.  .-'|  .-.  |  \\  `'  / `|  |   |  ()  | \n" +
                "|  | --' \\ '-'  |.-'  `).-'  `)   .'.   |' '-' '|  |  \\ `-' | |  | |  |'  ''  '  |  |  |  | |  |   \\    /   |  |.--.\\    /  \n" +
                "`--'      `--`--'`----' `----''--'   '--' `---' `--'   `---'  `--' `--' `----'   `--'  `--' `--'    `--'    `--''--' `--'   ");


        if (args.length != 2) {
            throw new IllegalArgumentException("User Management program takes exactly two arguments!");
        }
        String username = args[1];

        try {
            File passDB = new File("../passdb.txt");
            File saltAndIv = new File("../saltAndIv.txt");
            File forcedList = new File("../forced.txt");
            File saltAndIvForce = new File("../saltAndIvForced.txt");
            passDB.createNewFile();
            saltAndIv.createNewFile();
            forcedList.createNewFile();
            saltAndIvForce.createNewFile();
        } catch (IOException exc){
            throw new RuntimeException(exc);
        }

        switch (args[0]) {
            case "add" -> AddCommand.add(masterPassword, username);
            case "passwd" -> PasswdCommand.changePass(masterPassword, username);
            case "del" -> DelCommand.delete(masterPassword, username);
            case "forcepass" -> ForcepassCommand.force(masterPassword, username);
            default -> throw new IllegalArgumentException("Illegal command given!");
        }
    }
}
