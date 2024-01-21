package com.qa.gorest.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.qa.gorest.frameworkexceptions.APIFrameworkException;

public class ConfigurationManager {
 //This class will help to read the code/configuration from the properties file. To read the Properties file, we need to have two java classes, Properties and FileInputStream class.
	
	private Properties prop;
	private FileInputStream ip;
	
	public Properties initProp() {
		prop = new Properties(); //Creating object of Properties class.
		
		// Maven: cmd line argument:
		// mvn clean install -Denv="qa" //env is a environment variable and we will get to know it bcoz of '-D' flag. No space will be given between -D and environment variable name.
		// mvn clean install
		//With this approach we need to run our TCs from the Maven instead of testng_regression.xml, if ran from .xml file,  it will always run on QA environment by default as written in the code line 28-30

		//In Java we read this env variable using System class.	It will run it at the run time.
		String envName = System.getProperty("env");

		try {
			if (envName == null) {
				System.out.println("no env is given...hence running tests on QA env... ");
				ip = new FileInputStream("./src/test/resources/config/qa.config.properties");
			} else {
				System.out.println("Running tests on env: " + envName);

				switch (envName.toLowerCase().trim()) {
				case "qa":
					ip = new FileInputStream("./src/test/resources/config/qa.config.properties");
					break;
				case "dev":
					ip = new FileInputStream("./src/test/resources/config/dev.config.properties");
					break;
				case "stage":
					ip = new FileInputStream("./src/test/resources/config/stage.config.properties");
					break;
				case "prod":
					ip = new FileInputStream("./src/test/resources/config/config.properties");
					break;

				default:
					System.out.println("Please pass the right env name..." + envName);
					throw new APIFrameworkException("WRONG ENV IS Given");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
//		try { //This was before using multi environment stup.
//			ip = new FileInputStream("./src/test/resources/config/config.properties");//creating object of the FileInputStream class where we will pass the path of the config.properties file. Now, this ip will make the connection with the config.properties file.
			try {
				prop.load(ip); //Now, prop reference variable of Properties file will have all the access to the content inside the config.properties file.
			} catch (IOException e) {
				e.printStackTrace();
			}
//		} catch (FileNotFoundException e) { //Always use try catch in Framework as when we use throw keyword, the method which will call this method will also have to throw declaration unnecessary.
//			e.printStackTrace();
//		} 
		return prop; //Since prop is a Properties class reference, so we will write Properties instead of void at the method declaration.
	}
}
	
