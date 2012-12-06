package issueclasses;

public class RemoveParameter {
    public RemoveParameter() {
        testMethod("a", "b", "c", "d");
    }

    public void testMethod(String a, String b, String c, String d) {
        System.out.println(a + b);
        System.out.println("");
        if (a != null) {
            a = b;
        }
    }
}
