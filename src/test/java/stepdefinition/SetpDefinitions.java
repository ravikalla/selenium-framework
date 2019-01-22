package stepdefinition;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import com.bdd.utilities.ExecutionContext;
import com.bdd.utilities.MyContainer;
import com.bdd.work.ExcelData;
import com.bdd.work.ResultsAssertions;
import com.bdd.work.SoapServices;
import com.bdd.work.RestServices;

public class SetpDefinitions {
	ExecutionContext context = (ExecutionContext)MyContainer.getInstance(Thread.currentThread().getId());
	ExcelData excelData = new ExcelData();
	SoapServices soapServices = new SoapServices();
	RestServices restServices = new RestServices();
	ResultsAssertions resultsAssertions = new ResultsAssertions(); 
	
	@Given("^Create JSON request using JSON template \"(.*?)\" and connect data sheet \"(.*?)\" with testcase ID \"(.*?)\"$")
	public void Create_JSON_request(String template, String sheet, String testCaseID) {
		this.context.testCaseID = testCaseID;
		this.excelData.jsonRequestExcel("Krishna", testCaseID, sheet, template);
	}
	@When("^I submit the JSON request$")
	public void I_submit_the_JSON_request(){
		this.restServices.jsonRequest();
	}
	@Then("^Connect Execel \"(.*?)\"$")
	public void Connect_Execel(String sheetName){
		this.excelData.connectionExcel(sheetName);
	}

	@When("^Validating \"(.*?)\" from \"(.*?)\" JSON response$")
	public void Validating_ExpectedValue_from_JSON_response(String expectedVal, String scenario) {
		this.resultsAssertions.jsonResponseAssertions(expectedVal, scenario);
	}
	
	@Given("^Create xml request using xml template \"(.*?)\" and connect data sheet \"(.*?)\" with testcase ID \"(.*?)\"$")
	public void Create_xml_request(String template, String sheet, String testCaseID) {
		this.context.testCaseID = testCaseID;
		this.excelData.xmlRequestExcel("Krishna", testCaseID, sheet, template);
	}
	@When("^I submit the xml request$")
	public void I_submit_the_xml_request(){
		this.soapServices.sendSoapRequest();
	}

	@Then("^Validating tag \"(.*?)\" of \"(.*?)\" as \"(.*?)\" from XML Response$")
	public void Validating_tag_of_as_from_XML_Response(String reportName, String childName, String expectedVal) {
		this.resultsAssertions.xmlResponseAssertions(reportName, childName, expectedVal);
	}

}