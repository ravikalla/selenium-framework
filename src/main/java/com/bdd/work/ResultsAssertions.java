package com.bdd.work;

import org.apache.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.bdd.utilities.ExecutionContext;
import com.bdd.utilities.MyContainer;
import com.jayway.jsonpath.JsonPath;

public class ResultsAssertions {

	ExecutionContext context = (ExecutionContext)MyContainer.getInstance(Thread.currentThread().getId());
	public int responseStatus;
	public String responseString;
	public ExcelData data = new ExcelData();
	public RestServices restServices = new RestServices();
	static final Logger logger = Logger.getLogger(ResultsAssertions.class);
	XMLUtilitySupport xmlUtilitySupport;
	
	public ResultsAssertions() {
		xmlUtilitySupport = new XMLUtilitySupport();
	}
	public void jsonResponseAssertions(String expectedVal, String scenario) {
		
		String jsonPath = this.data.readDataForJsonValidations(scenario, expectedVal);
		String jsonFile = this.context.testData.get("serviceResponse");
		String result = this.getValueFromJsonFile(jsonFile, jsonPath);
		if (result != null) {
			if (result.startsWith("\"")) {
				result = result.substring(1, result.length() - 1);
			}
			if (expectedVal.equalsIgnoreCase(result.trim())) {
				logger.info("PASS:::Verifying status from " + scenario + " Json response. Actual value '" + result + "' Expected value '" + expectedVal + "'" + System.lineSeparator());
			}else {
				logger.info("FAIL:::Verifying status from " + scenario + " Json response. Actual value '" + result + "' Expected value '" + expectedVal + "'" + System.lineSeparator());
			}
		}else {
			logger.info("Json path " + jsonPath + " is null");
		}
	}
	
	public String getValueFromJsonFile(String jsonFile, String jsonPath) {
		try {
			JSONParser parser = new JSONParser();
			Object jsonObject = parser.parse(jsonFile);
			String result = JsonPath.read(jsonObject, jsonPath).toString();
			return result;//.substring(result.indexOf("[") + 1, result.indexOf("]"));
		} catch (Exception var5) {
			logger.info("Exception - " + var5);
			return null;
		}
	}
	
	public void xmlResponseAssertions(String reportName, String childName, String expectedVal) {
		
		//String jsonPath = this.data.readDataForJsonValidations(scenario, expectedVal);
		//String jsonFile = this.context.testData.get("serviceResponse");
		boolean isMatch = false;
		String nodeText = null;
		NodeList result =(NodeList) xmlUtilitySupport.getValueFromResponse(this.context.testData.get("responseString"), "//" + reportName +  "//" + childName);
		
		for (int i = 0; i < result.getLength(); i++) {
			Node node = result.item(i);
			nodeText = node.getTextContent();
			
			if (nodeText.equals(expectedVal)) {
				isMatch = true;
				break;
			}
		}
		
		if(isMatch) {
			logger.info("Pass::: Tag of " + reportName + "in " + childName + " matches with value as '" + expectedVal + "'");
		}else {
			logger.info("FAIL::: Tag of " + reportName + "in " + childName + " not matched with value as '" + expectedVal + "'");
		}
			
	}
	
}
