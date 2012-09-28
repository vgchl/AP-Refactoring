public class UnusedCodeTester {

	public static String string1 = "Si!";
 	public static String string2 = "No!";
    public static String MYvariBal = "x";

	public static void main(String[] args) {
    	System.out.println(string1);    
    	used();
    }
    
    private String unused(String a,
                          String b,
                          String c) {

    	return "YES";
    	
    }
    
    private static boolean used() {
    	return true;
    }
}