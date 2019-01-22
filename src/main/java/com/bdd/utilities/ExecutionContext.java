package com.bdd.utilities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentTest;

import com.bdd.utilities.IExecutionContext;

public class ExecutionContext implements IExecutionContext{

	public String suiteName = null;
	public String attributeValue = null;
	public String responseAttribute = null;
	public String customer = null;
	public String projectName = null;
	public String tegName = null;
	public String responseString = null;
	public String serviceResponse = null;
	public String browser = "Chrome";
	public String simulator = null;
	public String runAgainst = null;
	public String featureName = null;
	public String testName = null;
	public String testCaseID = null;
	public int responseStatus = 0;
	public boolean status = false;
	
	public WebDriver driver = null;
	public Properties config = null;
	public String reportDirectory = null;
	//public XMLGenerator test = null;
	//public ReportGenerator = null;
	public ExtentTest test;
	public List<String> dateTime = new ArrayList();
	public List<String> stepStatus = new ArrayList();
	public List<String> description = new ArrayList();
	
	public LinkedHashMap<String, String> scenarioName = new LinkedHashMap();
	public Map<String, String> testData = null;
	public HashMap<String, String> serviceResponseHeaders = new HashMap();
	
	public ArrayList<String> requestPath = new ArrayList();
	public ArrayList<String> responsePath = new ArrayList();
	public ArrayList<String> testStatus = new ArrayList();
	
	public Date suiteEndTime = null;
	public Date testStartTime = null;
	public Date testEndTime = null;
	
	public XSSFSheet sheet;
	
	public ExecutionContext() {
		
	}
	
	public void setTestData(String k, String v) {
		
		this.testData.put(k, v);	
	
	}
	
}
