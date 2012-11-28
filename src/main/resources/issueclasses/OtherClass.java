package issueclasses;

public class OtherClass extends ParentClass {

    public OtherClass() {
        System.out.println("Test");
        methodThatHasToBePrivate();
        method1("yy");
    }

    public void method1(String s) {
        System.out.println("This method has to be public: " + s);
    }

    /**
     * My Comment
     */

    public String methodThatHasToBePrivate() {
        System.out.println("This method could be private.");
        return null;
    }

    @Override
    public void test() {
        // Content
    }
}
