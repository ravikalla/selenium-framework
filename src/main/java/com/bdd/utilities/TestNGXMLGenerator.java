package com.bdd.utilities;

import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.*;
import java.io.File;
import java.io.FileInputStream;  
import org.apache.log4j.Logger;
import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;
import com.bdd.utilities.ExecutionContext;
import com.bdd.utilities.MyContainer;
import com.bdd.utilities.TestNGXMLGenerator;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

public class TestNGXMLGenerator {

	Connection con = null;
	Connection testSuiteConn = null;
	String suiteName = null;
	String parallel = null;
	String simulator = null;
	String browser = null;
	String runAgainst = null;
	String threadCount = null;
	String tags = null;
	String cvsFile = null;
	public String projectName = null;
	public int responseStatus;
	public ExecutionContext context;
	static final Logger logger = Logger.getLogger(TestNGXMLGenerator.class);
	
	public TestNGXMLGenerator() {
		
	}
	public TestNGXMLGenerator(String projName) {
		suiteName = projName;
		try {
			Fillo fillo = new Fillo();
			this.con = fillo.getConnection(System.getProperty("user.dir") + "//src//test//resources//data//TestSuite.xlsx");
			this.context = (ExecutionContext)MyContainer.getInstance(Thread.currentThread().getId());
		} catch (FilloException var4) {	
		}
	}
	
	public static void main(String... args) {

		
		try {

			String projName = args[0];//"TestNGClass";
			TestNGXMLGenerator xmlGenerator = new TestNGXMLGenerator(projName);
			xmlGenerator.createTestNG();
			
		}catch(Exception var3) {
			
		}	
	}
	
	public String getValues(String key) {
		
		FileInputStream ip = null;
		
		try {
			Properties prop = new Properties();
			String configProp = System.getProperty("user.dir") + "\\src\\test\\resources\\config\\Sys.properties";
			String config = System.getProperty("user.dir") + "\\src\\test\\resources\\config\\Sys.properties";
			
			
			if (new File(configProp).exists()) {
				ip = new FileInputStream(configProp);
			}else {
				ip = new FileInputStream(config);				
			}
			prop.load(ip);
			String var6 = prop.getProperty(key);
			return var6;
		}catch(Exception var4) {
			
		}
		return null;		
	}
	
	public void createTestNG() {	
		try {
			this.browser = this.getValues("Browser");
			this.parallel = this.getValues("Parallel");
			this.threadCount = this.getValues("ThreadCount");
			this.suiteName = this.getValues("TestSuite");
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			Document doc = dbBuilder.newDocument();
			Element rootElement = doc.createElement("suite");
			doc.appendChild(rootElement);
			
			Attr rootNameAttribute = doc.createAttribute("name");
			rootNameAttribute.setValue(this.suiteName);
			
			Attr rootParallelAttribute = doc.createAttribute("parallel");
			rootParallelAttribute.setValue(this.parallel);
			
			Attr rootThreadCountAttribute = doc.createAttribute("thread-count");
			rootThreadCountAttribute.setValue(this.threadCount);
			
			rootElement.setAttributeNode(rootNameAttribute);
			rootElement.setAttributeNode(rootParallelAttribute);
			rootElement.setAttributeNode(rootThreadCountAttribute);
			
			String query = "Select * from Tests";
			Recordset recordSet = this.con.executeQuery(query);
			int rowCount = recordSet.getCount();
			
			Element classesElement;
			for (int i = 1; i <= rowCount; ++i) {
				while(recordSet.next()) {
					if (recordSet.getField("Execute").toUpperCase().trim().equals("TRUE")) {
						Element testElement = doc.createElement("test");
						rootElement.appendChild(testElement);
						Attr testNameAttribute = doc.createAttribute("name");
						testNameAttribute.setValue(recordSet.getField("Test"));
						testElement.setAttributeNode(testNameAttribute);
						
						classesElement = doc.createElement("classes");
						testElement.appendChild(classesElement);
						//Feature Name
						Element parameterFeatureElement = doc.createElement("parameter");
						Attr featureAttribute = doc.createAttribute("name");
						featureAttribute.setValue("FeatureName");
						parameterFeatureElement.setAttributeNode(featureAttribute);
						Attr featureValue = doc.createAttribute("value");
						featureValue.setValue(recordSet.getField(i).value());
						parameterFeatureElement.setAttributeNode(featureValue);
						classesElement.appendChild(parameterFeatureElement);
						
						//Tags
						Element parameterTagElement = doc.createElement("parameter");
						Attr runTagAttribute = doc.createAttribute("name");
						runTagAttribute.setValue("tags");
						parameterTagElement.setAttributeNode(runTagAttribute);
						Attr runTagValue = doc.createAttribute("value");
						if (this.tags != null) {
							runTagValue.setValue("@" + this.tags); 
						}else {
							runTagValue.setValue("@" + recordSet.getField("Tags"));
						}
						parameterTagElement.setAttributeNode(runTagValue);
						classesElement.appendChild(parameterTagElement);
						//Class
						Element classElement = doc.createElement("class");
						Attr classNameAttribute = doc.createAttribute("name");
						classNameAttribute.setValue("com.bdd.utilities.Runner");
						classElement.setAttributeNode(classNameAttribute);
						classesElement.appendChild(classElement);
					}
				}
			}
			recordSet.close();
			this.con.close();
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			classesElement = null;
			File file;
			String baseDire = (String)System.getProperties().get("basedir");
			if(baseDire != null) {
				file = new File(baseDire + "/target/Execution.xml");
			}else {
				file =new File(System.getProperty("user.dir") + "/target/Execution.xml");
			}
			StreamResult results = new StreamResult(file);
			transformer.transform(source, results);
			StreamResult consoleResult = new StreamResult(System.out);
			transformer.transform(source, consoleResult);
			
		}catch(Exception var37) {
			System.out.println(var37);			
		}
	}
	
}
