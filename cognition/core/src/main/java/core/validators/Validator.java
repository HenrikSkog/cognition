package core.validators;

public class Validator {
    public static boolean isValidUUID(String UUID) {
        return UUID.length() == 36 && UUID.split("-").length == 5;
    }
}
