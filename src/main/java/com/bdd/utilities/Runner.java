package com.bdd.utilities;

import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.bdd.utilities.ExecutionBinding;
import com.bdd.utilities.SingletonRunner;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import com.bdd.utilities.ExecutionContext;
import com.bdd.utilities.MyContainer;
import com.bdd.utilities.RunCukes;
import com.bdd.utilities.Runner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.*;

public class Runner extends RunCukes{

	public ExecutionContext context;
	static final Logger logger = Logger.getLogger(Runner.class);
	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	public ExtentTest test;
	public static Properties config = null;
	public WebDriver driver;
	public static String reportDirectory = null;
	public Runner () {
		
	}
	
	@BeforeSuite
	public void beforeSuite(ITestContext istx) throws IOException {
		
		//Assign values to singleton class
		SingletonRunner runner = SingletonRunner.getInstance();
		runner.setProjectName(istx.getSuite().getName());
		runner.setSuiteStartTime(new Date());
		
		//Load Properties
		config = new Properties();
		String configProp = System.getProperty("user.dir") + "\\src\\test\\resources\\config\\Sys.properties";
		FileInputStream ip = new FileInputStream(configProp);
		config.load(ip);
		
		//load Log4J properies 
		PropertyConfigurator.configure(System.getProperty("user.dir") + "\\src\\test\\resources\\config\\log4j.properties");

		//Create report folder under target if not available
		File reportFolder = new File("./target/reports");
		if (!reportFolder.exists()) {
			reportFolder.mkdir();	
		}
		//Create htmlReport folder under reports folder if not available
		File htmlReport = new File("./target/reports/htmlReport");
		if (!htmlReport.exists()) {
			htmlReport.mkdir();	
		}
		//Create logFolder under reports folder if not available
		File logFolder = new File("./target/reports/log");
		if (!logFolder.exists()) {
			logFolder.mkdir();	
		}
		//Create Extent Report
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy h:mm a");
		String formattedDate = sdf.format(date).replace(" ", "_").replace(":", "-").replace("/","_");
		reportDirectory = System.getProperty("user.dir") + "/target/reports/htmlReport/Results_" + formattedDate;
		
		File dir = new File("./target/reports/htmlReport/Results_" + formattedDate);
		runner.setDirectory(dir);
		dir.mkdir();
	   
	    // start reporters
	    htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir") + "\\target\\reports\\htmlReport\\Results_" + formattedDate + "\\Execution_Report.html");
		// create ExtentReports and attach reporter(s)
	    extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        logger.info("Thread ID - " + Thread.currentThread().getId() +" before suite completed execution");
	}
	
	@Parameters({"FeatureName","tags"})
	@BeforeTest
	public void beforeTest(ITestContext ctx, String FeatureName, String tags) throws Exception {
		logger.info("Thread ID - " + Thread.currentThread().getId() +" before test started execution");
		Injector injector = Guice.createInjector(new Module[] {new ExecutionBinding()});
		this.context = (ExecutionContext)injector.getInstance(ExecutionContext.class);
		MyContainer.putInstance(Thread.currentThread().getId(), this.context);
		this.context = (ExecutionContext)MyContainer.getInstance(Thread.currentThread().getId());
		
		this.context.projectName = ctx.getSuite().getName();
		this.context.featureName = ctx.getCurrentXmlTest().getParameter("FeatureName");		       
		 // creates a toggle for the given test, adds all log events under it    
		this.context.test = extent.createTest(ctx.getCurrentXmlTest().getName());
		logger.info("Thread ID - " + Thread.currentThread().getId() +" before test completed execution");
	}
	
	@Test
	public void runCuke(ITestContext istx )throws Exception {
		
		logger.info("Thread ID - " + Thread.currentThread().getId() +" test started execution");
		super.RunningOfCukes(this.OptionsSpecification(istx));
		logger.info("Thread ID - " + Thread.currentThread().getId() +" test completed execution");
	}

	public String[] OptionsSpecification(ITestContext istx) {
		String tags = "";
		List<String> runParams = new ArrayList();
		String[] Options_Runtime = new String[7];
		Options_Runtime[0] = System.getProperty("user.dir") + "/src/test/resources/features/" + istx.getCurrentXmlTest().getParameter("FeatureName");
		Options_Runtime[1] = "--glue";
		Options_Runtime[2] = "com.com";
		Options_Runtime[1] = "--glue";	
		Options_Runtime[2] = "stepdefinition";
		Options_Runtime[3] = "--glue";
		Options_Runtime[4] = "service";
		Options_Runtime[5] = "--plugin";
		Options_Runtime[6] = "json:target/cucumerReport.json";
				
		for (int i = 0; i < Options_Runtime.length; i++) {
			runParams.add(Options_Runtime[i]);
		}
		if (System.getProperty("tagsparam") != null) {
			runParams.add("--tags");
			runParams.add(System.getProperty("tagsparam"));
		} else {
			tags = istx.getCurrentXmlTest().getParameter("tags");
			if(tags != null && !tags.equals("")) {
				runParams.add("--tags");
				runParams.add(tags);
			}
		}
				
		return (String[])runParams.toArray(new String[runParams.size()]);
	}
	@AfterTest
	public void afterTest() {
		logger.info("Thread ID - " + Thread.currentThread().getId() +" after test started execution");
		if (this.context.driver != null) {
			this.context.driver.close();
		}
		logger.info("Thread ID - " + Thread.currentThread().getId() +" after test completed execution");
	}
	@AfterSuite
	public void afterSuite() {
        // calling flush writes everything to the log file
		logger.info("Thread ID - " + Thread.currentThread().getId() +" after suite started execution");
        extent.flush();
		logger.info("Thread ID - " + Thread.currentThread().getId() +" after suite completed execution");
	}
	
	public void createRequestResponseLogs(String testName, String type, String text) {
		FileOutputStream logFile = null;
		
		try {
			SingletonRunner singleRunner = SingletonRunner.getInstance();
			File dir = new File (singleRunner.getDirectory() + "//" + type);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			if (type.equalsIgnoreCase("Request")) {
				logFile = new FileOutputStream(String.valueOf(dir + "/Request_" + testName + ".log"));
				singleRunner.setRequestLogPath(String.valueOf(dir + "/Request_" + testName + ".log"));
				logFile.write(text.getBytes());
			}else if (type.equalsIgnoreCase("Response")) {
				logFile = new FileOutputStream(String.valueOf(dir + "/Response_" + testName + ".log"));
				singleRunner.setResponseLogPath(String.valueOf(dir + "/Response_" + testName + ".log"));
				logFile.write(text.getBytes());
			}
		}catch (Exception var15) {
			logger.info("Exception - " + var15);
		}finally {
			try {
				if (logFile != null) {
					logFile.close();
				}
			}catch(Exception var14) {
				logger.info("Exception - " + var14);
			}
		}
	}
}
