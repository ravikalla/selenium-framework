package stepdefinition;

import com.bdd.page.Login;
import com.bdd.page.Search;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import com.bdd.utilities.*;
public class SetpDefinition {
	
public Login login = new Login();
public Search search = new Search();
public CoreUtils coreUtils = new CoreUtils();

	@Given("^User opens the browser")
	public void User_opens_the_browser()throws Throwable {
		coreUtils.loadDriver();
	}
	@Then("^User is able Launch the hotel application using \"([^\"]*)\"$")
	public void user_is_able_Launch_the_hotel_application_using(String arg1)throws Throwable {
		login.lauchApplication(arg1);
	}

	@When("^User enters the \"([^\"]*)\" and \"([^\"]*)\"$")
	public void user_enters_the_and(String arg1, String arg2) throws Throwable {

		login.login(arg1, arg2);
	}

	@When("^User clicks the Log in button$")
	public void user_clicks_the_Log_in_button() throws Throwable {
		login.clickOnSignIn();
	}

	@Then("^User naviaged to home page$")
	public void user_naviaged_to_home_page() throws Throwable {
		login.validateHomePage();
	}
	
	
	@And("^user enters the required information in search hotel page$")
	public void user_enters_the_required_information_in_search_hotel_page() throws Throwable {
	
		search.enterRoomSearchInfo();
	}
	
	@And("^user clicks the search button$")
	public void user_clicks_the_search_button() throws Throwable {
	
		search.clickSearchOnSearchPage();
	}
	
	@Then("^user navigates to Select Hotel page$")
	public void user_navigates_to_Select_Hotel_page() throws Throwable {
		search.validateHotelRoomSearch();
	}
	
	@Then("^LogOut application$")
	public void LogOut_application() throws Throwable {
		login.LogOut();
	}
	
}