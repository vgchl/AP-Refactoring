public class UnusedCodeTester {

    public static String string1 = "Si!";

    public static String string2 = "No!";

    public static String MYvariBal = "x";

    public static void main(String[] args) {
        System.out.println(string1);
        used();
    }

    private String unused(String a, String b, String c) {
        int i = 0;
        if (i == MAGIC0) {
        } else if (i == MAGIC23) {
        }
        return "YES";
    }

    private static boolean used() {
        return true;
    }

    private static final int MAGIC0 = 0;

    private static final int MAGIC23 = 23;
}
