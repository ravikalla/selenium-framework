package com.bdd.utilities;

import java.util.NoSuchElementException;
import org.testng.Assert;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class CoreUtils{
	static final Logger logger = Logger.getLogger(CoreUtils.class);
	ExecutionContext context = (ExecutionContext)MyContainer.getInstance(Thread.currentThread().getId());
	public WebDriver driver;
	
	public void loadDriver() {
		String browser = Runner.config.getProperty("Browser");
		String driverPath = System.getProperty("user.dir") + "\\src\\main\\resources\\drivers";
		if (browser.toUpperCase().contains("CH")) {
			System.setProperty("webdriver.chrome.driver", driverPath + "\\chromedriver.exe");
			ChromeOptions options = new ChromeOptions();
			options.setExperimentalOption("useAutomationExtension", false);
			options.addArguments("no-sandbox");
			options.addArguments("start-maximized");
			options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
			this.context.driver = new ChromeDriver(options);
		}
		if (browser.toUpperCase().contains("FF")) {
			this.context.driver = new FirefoxDriver();
		}
		if (browser.toUpperCase().contains("IE")) {
			System.setProperty("webdriver.ie.driver", driverPath + "\\IEDriverServer.exe");
			this.context.driver = new InternetExplorerDriver();
		}
		//this.context.test.log(Status.PASS, "Thread ID - " + Thread.currentThread().getId() + " - Open application");
	}
		
		/**
	 * @function Use to perform any action in the application(click/edit/drop
	 *           down/scroll)
	 * @param element
	 * @param value
	 * @param ColumnName
	 * @return true/false
	 * @throws Exception
	 */
	public Boolean SafeAction(By element, String value, String ColumnName) throws Exception {
		WebElement ele =null;
		Boolean returnValue = true;
		Actions objActions = null;
		objActions = new Actions(this.context.driver);
		JavascriptExecutor js = (JavascriptExecutor) this.context.driver;
		String elementType = ColumnName.substring(0, 3);
		String objectName = ColumnName.substring(4);
		boolean elementClickable = WaitUntilClickable(element, Integer.valueOf(Runner.config.getProperty("LONGESTWAIT")));
		if (elementClickable == true) {
			Boolean f = this.context.driver.findElements(element).size() != 0;
			if (!f) {
				returnValue = false;
			} else {
				highlightElement(element);
				try {
					ele = this.context.driver.findElement(element);
					returnValue = true;
				} catch (Exception e) {
					returnValue = false;
				}
			}
		} else {
			returnValue = false;
		}
		if (returnValue) {
			switch (elementType.toUpperCase()) {
			case "EDT":
				ele.clear();
				ele.sendKeys(value);
				//ele.sendKeys(Keys.TAB);
				returnValue = true;
				this.context.test.log(Status.PASS, "Thread ID - " + Thread.currentThread().getId() + " - value '" + value + "' entered in '" + objectName + "' editbox");
				logger.info("Thread ID - " + Thread.currentThread().getId() + " - value '" + value + "' entered in '" + objectName + "' editbox");
				break;
			case "BTN":
				ele.click();
				returnValue = true;
				this.context.test.log(Status.PASS, "Thread ID - " + Thread.currentThread().getId() + " - clicked on '" + objectName + "' element");
				logger.info("Thread ID - " + Thread.currentThread().getId() + " - clicked on '" + objectName + "' element");
				break;
			case "ELE":
				Action objMouseClick1 = objActions.click(ele).build();
				objMouseClick1.perform();
				returnValue = true;
				this.context.test.log(Status.PASS, "Thread ID - " + Thread.currentThread().getId() + " - clicked on '" + objectName + "' element");
				logger.info("Thread ID - " + Thread.currentThread().getId() + " - clicked on '" + objectName + "' element");
				break;
			case "DBL":
				objActions.click(ele);
				Action objMousedblClick = objActions.doubleClick(ele).build();
				objMousedblClick.perform();
				returnValue = true;
				this.context.test.log(Status.PASS, "Thread ID - " + Thread.currentThread().getId() + " - clicked on '" + objectName + "' element");
				logger.info("Thread ID - " + Thread.currentThread().getId() + " - clicked on '" + objectName + "' element");
				break;
			case "LST":
				Select listbox = new Select(ele);
				  listbox.selectByValue(value);
				returnValue = true;
				this.context.test.log(Status.PASS, "Thread ID - " + Thread.currentThread().getId() + " - value '" + value + "' selected from '" + objectName + "' listbox");
				logger.info("Thread ID - " + Thread.currentThread().getId() + " - value '" + value + "' selected from '" + objectName + "' listbox");
				break;
			case "SCL":
				((JavascriptExecutor) this.context.driver).executeScript("arguments[0].scrollIntoView();", this.context.driver.findElement(element));
				returnValue = true;
				returnValue = JavaScript(js);
				break;
			}
		} else {
			//this.context.test.log(Status.FAIL, "Thread ID - " + Thread.currentThread().getId() + " - " + objectName + " not identified");
			 // log with snapshot
			logger.info("Thread ID - " + Thread.currentThread().getId() + " - " + objectName + " element not identified");
			this.context.test.fail("Thread ID - " + Thread.currentThread().getId() + " - " + objectName + " element not identified", MediaEntityBuilder.createScreenCaptureFromPath(CaptureScreen(this.context.driver)).build());
			Assert.fail("Thread ID - " + Thread.currentThread().getId() + " - " + objectName + " element not identified");
		}
		return returnValue;
	}
	/**
	 * @function Highlights on current working element or locator
	 * @param driver
	 * @param locator
	 * @throws Exception
	 */
	public void highlightElement(By locator) throws Exception {
			WebElement element = this.context.driver.findElement(locator);
			String attributevalue = "border:10px solid green;";
			JavascriptExecutor executor = (JavascriptExecutor) this.context.driver;
			String getattrib = element.getAttribute("style");
			executor.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, attributevalue);
			Thread.sleep(100);
			executor.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, getattrib);
	}
	
	public boolean JavaScript(JavascriptExecutor js) throws Exception {
		boolean status = false;
		for (int i = 1; i <= Integer.parseInt("10000"); i++) {
			Boolean isAjaxRunning = Boolean.valueOf(js.executeScript("return Ext.Ajax.isLoading();") 
					.toString());
			if (!isAjaxRunning.booleanValue()) {
				status = true;
				break;
			}
			Thread.sleep(1000);// wait for one secnod then check if ajax is
								// completed
		}
		return status;
	}
	
	/**
	 * @function This function use to wait untill the next element is ready to click
	 * @param bylocator
	 * @param iWaitTime
	 * @return true/false
	 * @throws Exception
	 */
	public boolean WaitUntilClickable(By bylocator, int iWaitTime) throws Exception {
		
		boolean bFlag = false;
		WebDriverWait wait = new WebDriverWait(this.context.driver, iWaitTime);
		try {
			wait.until(ExpectedConditions.elementToBeClickable(bylocator));
			if (this.context.driver.findElement((bylocator)).isDisplayed()) {
				bFlag = true;
			}
		} catch (NoSuchElementException e) {
			e.printStackTrace();

			bFlag = false;
		} catch (Exception e) {
			e.printStackTrace();
			bFlag = false;
		}
		return bFlag;
	}
	
	public String CaptureScreen(WebDriver driver){
		//String workingDirectory = System.getProperty("user.dir");
		DateFormat dateFormat = new SimpleDateFormat("MMddyyyy_HHmmss");
		Date date = new Date();
		String ImagesPath = Runner.reportDirectory + "\\Screenshot" + dateFormat.format(date) + ".jpg";
		TakesScreenshot oScn = (TakesScreenshot) driver;
	    File oScnShot = oScn.getScreenshotAs(OutputType.FILE);
	    File oDest = new File(ImagesPath);
	    try {
	    	FileUtils.copyFile(oScnShot, oDest);
	    } catch (IOException e) {System.out.println(e.getMessage());}
	    return ImagesPath;
	}
}