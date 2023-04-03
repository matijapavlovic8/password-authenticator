package command;


public class PasswdCommand {
    public static void changePass(String masterPassword, String username) {
        DelCommand.update = true;
        AddCommand.update = true;
        DelCommand.delete(masterPassword, username);
        AddCommand.add(masterPassword, username);
        DelCommand.update = false;
        AddCommand.update = false;
        System.out.println("Password changed successfully!");
    }
}
