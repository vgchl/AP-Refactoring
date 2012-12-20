package issueclasses;

public class UnusedCodeTester extends OtherClass {

    public String naam;

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
        used(300);

        OtherClass otherClass = new OtherClass();
        otherClass.method1("xx");
        otherClass.steel = "hallo";
        WtFperMinute = otherClass.steel;
    }

    private String unused(String a, String b, String c) {
        int i = 0;
        if (i == 0) {
        } else if (i == 23) {
        }
        char a = a;
        if ('a' == a) {
        	
        }
        
        if (unused("asdf", "ghjk", "qwer").size() == 0) {
        	
        }

        return "YES";
    }

    private static boolean used(int i) {
        return true;
    }
}
