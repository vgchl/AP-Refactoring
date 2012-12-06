package issueclasses;

/**
 * Created with IntelliJ IDEA.
 * User: Sjoerd van den Top
 * Date: 5-12-12
 * Time: 14:36
 * To change this template use File | Settings | File Templates.
 */
public class RemoveParameter {

    public RemoveParameter() {
        testMethod("", "", "", "");
    }

    public void testMethod(String a, String b, String c, String d) {
        System.out.println(a + b);
        System.out.println("");
        if (a != null) {
            a = b;
        }
    }
}
