package issueclasses;

public class OtherClass extends ParentClass {

    public String steel = "";

    public OtherClass() {
        UnusedCodeTester.MYvariBal = "xdjfj";
        System.out.println(UnusedCodeTester.MYvariBal);
        System.out.println("Test");
        methodThatHasToBePrivate();
        method1("yy");
    }

    public void method1(String s) {
        System.out.println("This method has to be public: " + s);
        this.steel = "hard";
    }

    public boolean isAuthorAGoatThatWritesBadCode() {
        return true;
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
