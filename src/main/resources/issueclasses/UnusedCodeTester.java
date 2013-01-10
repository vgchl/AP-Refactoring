package issueclasses;

public class UnusedCodeTester extends OtherClass {

    public String naam;

    public static final int CONSTANT_NAME = 0;

    public static String string1 = "Si!";

    public static String string2 = "No!";

    @SuppressWarnings("unchecked")
    public static String MYvariBal = "x";

    private String someVaribal = "xdfdsf";

    private static String WtFperMinute;

    public static void main(String[] args) {
        System.out.println(string1);
        used(100);
        used(200);
        used(130);

        OtherClass otherClass = new OtherClass();
        otherClass.method1("xx");
        otherClass.steel = "hallo";
        WtFperMinute = otherClass.steel;
    }

    private String unused(String a, String b, String c) {
        int i = 20;
        if (i == 30) {
        } else if (i == 23) {
        }
        char ax = 'a';
        if ('a' == ax) {
        	
        }
        
        if (unused("asdf", "ghjk", "qwer").length() == 50) {
        	
        }

        return "YES";
    }

    private static boolean used(int i) {
        return true;
    }
}
