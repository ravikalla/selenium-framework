package com.bdd.work;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.bdd.utilities.ExecutionContext;
import com.bdd.utilities.MyContainer;
import com.bdd.utilities.Runner;
import com.bdd.utilities.TestNGXMLGenerator;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.Match;

public class ExcelData {
	static final Logger logger = Logger.getLogger(ExcelData.class);
	public ExecutionContext context = (ExecutionContext)MyContainer.getInstance(Thread.currentThread().getId());
	public XSSFWorkbook workbook = null;
	public XSSFSheet sheet = null;
	public XSSFRow row = null;
	public XSSFCell cell = null;
	public FileInputStream fis = null;
	public XSSFRow rowheader = null;
	public FileOutputStream outPut_File = null;
	public File excelPah = null;
	public RestServices restServices = new RestServices();
	Runner runner = new Runner();
	public SoapServices soapServices = new SoapServices();
	public TestNGXMLGenerator testNGXMLGenerator = new TestNGXMLGenerator();
	
	public ExcelData() {
		
	}
	
	public void connectionExcel(String sheetName) {
		try {
			String excel = testNGXMLGenerator.getValues("DataBookName");
			String fileName = excel + ".xlsx";
			String filePath = System.getProperty("user.dir") + "//src//test//resources//Data//" + fileName;
			ClassLoader classLoader = this.getClass().getClassLoader();
			File path = new File(filePath);
			boolean flag = path.exists();
			if (!flag) {
				path = new File(filePath);
			}
			this.fis = new FileInputStream(path);
			this.workbook = new XSSFWorkbook(this.fis);
			this.sheet = this.workbook.getSheet(sheetName);
			this.context.sheet = this.sheet;
		}catch (Exception var1) {
			logger.info("Exception - " + var1);
		}
	}
	public HashMap<String, String> readData(String key, String value){
		HashMap<String, String> data = new HashMap();
		int cellValue = 0;
		int rowValue = 0;
		int rowCount = this.sheet.getPhysicalNumberOfRows();
		this.row = this.sheet.getRow(0);
		int cellCount = this.row.getPhysicalNumberOfCells();
		
		int i = 0;
		for (i = 0; i < cellCount; ++i) {
			if(this.row.getCell(i).getStringCellValue().trim().equals(key.trim())) {
				cellValue = i;
				break;
			}
		}
		
		for (i = 0; i < cellCount; ++i) {
			this.row = this.sheet.getRow(i);
			if(this.row.getCell(cellValue).getStringCellValue().trim().equals(value.trim())) {
				rowValue = i;
				break;
			}
		}
		
		for (i = 0; i < cellCount; ++i) {
			this.rowheader = this.sheet.getRow(0);
			String keyName = this.rowheader.getCell(i).getStringCellValue().trim();
			this.row = this.sheet.getRow(rowValue);
			String valueName = null;
			try {
				if(this.row.getCell(i).getStringCellValue().trim().startsWith("#")) {
					valueName  = this.row.getCell(i).getStringCellValue().substring(1);
				}else {
					valueName  = this.row.getCell(i).getStringCellValue().trim();
				}
				data.put(keyName, valueName);
			}catch (Exception var1) {
				logger.info("Exception - " + var1);
			}
		}
		return data;
	}
	
	public String readDataForJsonValidations(String customer, String expectedVal) {
		
		String path = null;
		int cellValue = 0;
		//int rowValue = false;
		String ev = "";
		int rowCount = this.context.sheet.getPhysicalNumberOfRows();
		for(int i = 0; i < rowCount; ++i) {
			this.row = this.context.sheet.getRow(i);
			/*if(this.row.getCell(1).getCellType() == 0) {
				ev = String.valueOf(Double.valueOf(this.row.getCell(1).getNumericCellValue()).intValue());
			}else {
				try {
					ev = this.row.getCell(1).getStringCellValue();
				} catch(Exception var3) {
					logger.info("Exception - " + var3);
				}
			}*/
			//if(this.row.getCell(cellValue).getStringCellValue().trim().equals(customer.trim()) && ev.trim().equals(expectedVal.trim())) {
			if(this.row.getCell(cellValue).getStringCellValue().trim().equals(customer.trim())) {
				path = this.row.getCell(1).getStringCellValue().trim();	
			}
		}
		return path;
	}
	
	public void passwordDecryption(String header) {
		
		
	}
	public void simulatorTestData() {
		
		
	}
	public void jsonRequestExcel(String customer, String testCaseID, String sheet, String template) {
		this.connectionExcel(sheet);
		this.context.testData = this.readData("TestCaseid",testCaseID);
		Set<String> keyList = this.context.testData.keySet();
		Iterator var6 = keyList.iterator();
		
		while(var6.hasNext()) {
			String key = (String)var6.next();
			if(key.contains("Password")) {
				this.passwordDecryption(key);
			}
		}
		this.simulatorTestData();
		logger.info(this.context.testData + System.lineSeparator());
		this.restServices.restRequest(template);
		logger.info("json request");
	}
	public void xmlRequestExcel(String customer, String testCaseID, String sheet, String template) {
		this.connectionExcel(sheet);
		this.context.testData = this.readData("TestCaseid",testCaseID);
		Set<String> keyList = this.context.testData.keySet();
		Iterator var6 = keyList.iterator();
		
		while(var6.hasNext()) {
			String key = (String)var6.next();
			if(key.contains("Password")) {
				this.passwordDecryption(key);
			}
		}
		this.simulatorTestData();
		logger.info(this.context.testData + System.lineSeparator());
		this.soapServices.prepareXmlRequest(template);
		logger.info("xml request");
	}
	
	public void setTeplate_Excel(String sheet) {
		
		this.connectionExcel(sheet);
		HashMap responseData = this.readData("Template_ID", (String)this.context.testData.get("Template_ID"));
		Pattern pattern = Pattern.compile("\\{[a-z A-Z 0-9]*\\}*");
		String request = (String)responseData.get("Template");
		String var;
		String data;
		for(Matcher match = pattern.matcher(request); match.find(); request = request.replace(var, data)) {
			var = match.group();
			data = (String)this.context.testData.get(var.substring(2, var.length() - 1));
			if(data == null) {
				data = "";
			}
		}
		System.out.println(request);
		this.context.testData.put("REQUEST_TEMPLATE", request);
		logger.info("received data from sheet " + sheet + " " + System.lineSeparator());
		
	}
}
