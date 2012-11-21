package issueclasses;

public class UnusedCodeTester {

    public static String string1 = "Si!";

    public static String string2 = "No!";

    public static String MYvariBal = "x";

    public static void main(String[] args) {
        System.out.println(string1);
        used(100);
        used(200);
        used(300);

        OtherClass otherClass = new OtherClass();
        otherClass.method1("xx");
    }

    private String unused(String a, String b, String c) {
        int i = 0;
        if (i == 0) {
        } else if (i == 23) {
        }
        return "YES";
    }

    private static boolean used(int i) {
        return true;
    }
}
