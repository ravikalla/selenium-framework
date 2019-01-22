package com.bdd.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.openqa.selenium.WebDriver;

public interface IExecutionContext {
	String runAgainst = null;
	String suiteRunID = null;
	String browser = "Chrome";
	String featureName = null;
	String serviceResponse = null;
	String projectName = null;
	String xmlStructure = "";
	WebDriver driver = null;
	Properties config = null;
	public String reportDirectory = null;
	//XMLGenerator test = null;
	HashMap testData = null;
	boolean status = false;
	
	ArrayList<String> testStatus = null;	
}