package com.bdd.page;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.bdd.utilities.CoreUtils;
import com.bdd.utilities.ExecutionContext;
import com.bdd.utilities.MyContainer;

public class Search extends CoreUtils{
	static final Logger logger = Logger.getLogger(Search.class);
	ExecutionContext context = (ExecutionContext)MyContainer.getInstance(Thread.currentThread().getId());
	public By lst_Location = By.name("location");
	public By lst_room_nos = By.name("room_nos");
	public By edt_datepick_in = By.name("datepick_in");
	public By edt_datepick_out = By.name("datepick_out");
	public By lst_adult_room = By.name("adult_room");
	public By ele_Submit = By.name("Submit");
	public By ele_continue = By.name("continue");
	public By ele_WelcomeText = By.xpath("/html/body/table[2]/tbody/tr[1]/td[1]");
	public void clickOnSearch() throws Exception {

		SafeAction(ele_Submit, "","ele_Submit");

	}
	
	public void enterRoomSearchInfo()
			throws Exception {
		
		Thread.sleep(500);
		SafeAction(lst_Location, "Sydney","lst_Location"); 
		Thread.sleep(500);
		//SafeAction(lst_room_nos, "2 - Two","lst_room_nos"); 
		//Thread.sleep(500);
		//SafeAction(edt_datepick_in, "","edt_datepick_in"); 
		//Thread.sleep(500);
		//SafeAction(edt_datepick_out, "","edt_datepick_out"); 
		//Thread.sleep(500);		
		//SafeAction(lst_adult_room, "2 - Two","lst_adult_room"); 
		//Thread.sleep(500);
	}
	
	
	public void clickSearchOnSearchPage() throws Exception {
		SafeAction(ele_WelcomeText, "","ele_WelcomeText");
		SafeAction(ele_Submit, "","ele_Submit");
	}
	
	
	public void validateHotelRoomSearch() throws IOException {
		if (this .context.driver.findElement(ele_WelcomeText).getText() == "Welcome to AdactIn Group of Hotels") {
			logger.info("Thread ID - " + Thread.currentThread().getId() + " - expected text 'Welcome to AdactIn Group of Hotels' matched with actual text");
			this.context.test.log(Status.PASS, "Thread ID - " + Thread.currentThread().getId() + " - expected text 'Welcome to AdactIn Group of Hotels' matched with actual text");
		}else {
			logger.info("Thread ID - " + Thread.currentThread().getId() + " - expected text 'Welcome to AdactIn Group of Hotels' not matched with actual text");
			this.context.test.fail("Thread ID - " + Thread.currentThread().getId() + " - expected text 'Welcome to AdactIn Group of Hotels' not matched with actual text", MediaEntityBuilder.createScreenCaptureFromPath(CaptureScreen(this.context.driver)).build());
		}
	}
}