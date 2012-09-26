public class UnusedCodeTester {

	public static String string1 = "Si!";
 	public static String string2 = "No!";

	public static void main(String[] args) {
    	System.out.println(string1);    
    	used();
    }
    
    private String unused() {

    	return "YES";
    	
    }
    
    private static boolean used() {
    	return true;
    }
}