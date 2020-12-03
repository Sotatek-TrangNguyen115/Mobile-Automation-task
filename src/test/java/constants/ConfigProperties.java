package constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigProperties {
	private static ConfigProperties instance;
	private Properties properties;
	private ConfigProperties() throws IOException {
		properties = new Properties();
		InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");
		properties.load(input);
	}
	
	public static ConfigProperties getInstance() throws IOException {
		 if(instance == null) {
			 synchronized(ConfigProperties.class) {
				 if(null == instance) {					 
					 instance  = new ConfigProperties();
				 }
			 }
		 }
		 return instance;
	 }
	
	 public Properties systemProperties() {
		 return properties;
	 }
}
