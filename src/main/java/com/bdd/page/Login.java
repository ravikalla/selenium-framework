package com.bdd.page;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.bdd.utilities.CoreUtils;
import com.bdd.utilities.ExecutionContext;
import com.bdd.utilities.MyContainer;
import com.bdd.utilities.Runner;

public class Login extends CoreUtils{
	static final Logger logger = Logger.getLogger(Login.class);
	ExecutionContext context = (ExecutionContext)MyContainer.getInstance(Thread.currentThread().getId());
	
	public By edt_UserName = By.id("username");
	public By edt_Password = By.id("password");
	public By ele_Login = By.id("login");
	public By ele_WelcomeText = By.xpath("/html/body/table[2]/tbody/tr[1]/td[1]");
	public By ele_LogOut = By.xpath("/html/body/table[2]/tbody/tr[1]/td[2]/a[4]");
	
	public void lauchApplication(String url)
			throws InterruptedException {
			this .context.driver.manage().timeouts().implicitlyWait(55, TimeUnit.SECONDS);
			this .context.driver.get(url);
		 	Thread.sleep(1000);
		 	this.context.test.log(Status.PASS, "Thread ID - " + Thread.currentThread().getId() + " - application opened in '" + Runner.config.getProperty("Region") + "' region - url:" + url);
		 	logger.info("Thread ID - " + Thread.currentThread().getId() + " - application opened in '" + Runner.config.getProperty("Region") + "' region - url:" + url);
	}

	public void login(String userName, String password) throws Exception {

		SafeAction(edt_UserName, userName,"edt_UserName");
		SafeAction(edt_Password, password,"edt_Password");
	}

	public void clickOnSignIn() throws Exception {

		SafeAction(ele_Login, "","ele_Login");
	}

	public void validateHomePage() throws IOException {

		if (this.context.driver.findElement(ele_WelcomeText).getText() == "Welcome to AdactIn Group of Hotels") {
			logger.info("Thread ID - " + Thread.currentThread().getId() + " - expected text 'Welcome to AdactIn Group of Hotels' matched with actual text");
			this.context.test.log(Status.PASS, "Thread ID - " + Thread.currentThread().getId() + " - expected text 'Welcome to AdactIn Group of Hotels' matched with actual text");
		}else {
			logger.info("Thread ID - " + Thread.currentThread().getId() + " - expected text 'Welcome to AdactIn Group of Hotels' not matched with actual text");
			this.context.test.fail("Thread ID - " + Thread.currentThread().getId() + " - expected text 'Welcome to AdactIn Group of Hotels' not matched with actual text", MediaEntityBuilder.createScreenCaptureFromPath(CaptureScreen(this.context.driver)).build());
		}
	}

	public void LogOut() throws Exception {
		
		SafeAction(ele_LogOut, "","ele_LogOut");
	}

}