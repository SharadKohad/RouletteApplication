package util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Constant
{
    public static final String URL = "http://site1.bidbch.com/api/";
    public static boolean isValidEmail(String email)
    {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static int TOTAL_BALANCE = 0;
    public static int BOUNCE_CASH = 0;
    public static int TOTAL_DEPOSIT_CASH = 0;
}
