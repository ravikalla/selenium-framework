package com.bdd.utilities;

import java.io.File;
import java.util.Date;
import com.bdd.utilities.Runner;
import com.bdd.utilities.SingletonRunner;

public class SingletonRunner {

	private static SingletonRunner runner;
	public String ProectName = null;
	public Date suiteStartTime = null;
	public Runner Runner;
	public File directory = null;
	public String requestLogPath = null;
	public String responseLogPath = null;
	public String responseString = null; 
	private SingletonRunner() {
		
	}
	
	public static SingletonRunner getInstance() {
		
		if(runner == null) {
			runner = new SingletonRunner();
		}
		return runner;
		
	}
	
	public void setRunRunner(Runner obj) {
	
		this.Runner = obj;
		
	}
	
	public Runner getRunner() {
		
		return this.Runner;
		
	}
	
	public void setProjectName(String projectName) {
		
		this.ProectName = projectName;
	}
	
	public String getProjectName() {
		
		return this.ProectName;
		
	}

	public void setSuiteStartTime(Date startTime) {
		
		this.suiteStartTime = startTime;
		
	}
	
	public Date getSuiteStartTime() {
		
		return this.suiteStartTime;
	}
	
	public void setDirectory(File dir) {
		
		this.directory = dir;
		
	}
	
	public File getDirectory() {
		
		return this.directory;
	}
	
	public void setRequestLogPath(String path) {
		
		this.requestLogPath = path;
		
	}
	
	public String getRequestLogPath() {
		
		return this.requestLogPath;
	}
	
	public void setResponseLogPath(String path) {
		
		this.responseLogPath = path;
		
	}
	
	public String getResponseLogPath() {
		
		return this.responseLogPath;
	}
	
public void setResponseString(String response) {
		
		this.responseString = response;
		
	}
	
	public String getResponseString() {
		
		return this.responseString;
	}
}
