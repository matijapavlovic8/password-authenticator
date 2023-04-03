package utils;

public class PasswordStrengthChecker {
    public static boolean check(String password) {
        if (password.length() < 8)
            return false;

        if (!password.matches("^.*[^a-zA-Z0-9].*$"))
            return false;

        if (!password.matches("^.*[A-Z].*$"))
            return false;

        if (!password.matches("^.*[0-9].*$"))
            return false;

        return true;
    }
}
