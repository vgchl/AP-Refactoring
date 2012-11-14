package issueclasses;

public class OtherClass {

    public OtherClass() {
        System.out.println("Test");
        methodThatHasToBePrivate();
        method1("yy");
    }

    public void method1(String s) {
        System.out.println("This method has to be public: " + s);
    }

    public void methodThatHasToBePrivate() {
        System.out.println("This method could be private.");
    }
}
