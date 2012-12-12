package issueclasses.removeparameter;

public class RemoveParameter extends superClass {
    public RemoveParameter() {
        testMethod("a", "b", "c", "d");
        blaat("e");
    }

    @Override
    public void testMethod(String a, String b, String c, String d) {
        System.out.println(a + b);
        System.out.println("");
        if (a != null) {
            a = b;
        }
    }

    public void blaat(String ef) {
        System.out.println("");
    }
}
