package autotest.driver;

public class ObjectExtension {
	public static double asDouble(String str) {
		try {
			return Double.parseDouble(str);
		}
		catch(NumberFormatException e) {
			return 0;
		}
	}
}
