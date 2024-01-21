package com.qa.gorest.frameworkexceptions;

public class APIFrameworkException extends RuntimeException {
 //This class extends RuntimeException class which comes from java.lang package. Internally if we want to create a custom exception, it needs to extend this RuntimeException class.
	
	public APIFrameworkException(String mesg) { //constructor
		super(mesg);//super() is used to call the parent class constructor, and whatever message we are getting, we are passing it to the RuntimeException. So if any Exception comes during Runtime, we will display the same custom message.
	}
}
