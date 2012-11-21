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
    @Deprecated
    public void methodThatHasToBePrivate() {
        System.out.println("This method could be private.");
    }

    @Override
    public void test() {
        // Content
    }
}
