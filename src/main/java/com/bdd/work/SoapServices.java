package com.bdd.work;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import java.util.Iterator;
import java.util.Set;
import com.bdd.utilities.ExecutionContext;
import com.bdd.utilities.MyContainer;
import com.bdd.utilities.Runner;
import com.bdd.utilities.SingletonRunner;
import com.bdd.utilities.TestNGXMLGenerator;

public class SoapServices {
	
	public ExecutionContext context = (ExecutionContext)MyContainer.getInstance(Thread.currentThread().getId());
	Runner runner = new Runner();
	static final Logger logger = Logger.getLogger(RestServices.class);
	
	public SoapServices() {
		SingletonRunner singletonRunner = SingletonRunner.getInstance();
		singletonRunner.setRunRunner(runner);
	}
	public String loadRequest(String requestName) {
		
		String template = "";
		InputStream is = null;		
		BufferedReader bReader = null;
		FileReader reader = null;
		try {
			String sourcePath =  System.getProperty("user.dir") +  "\\src\\main\\resources\\extendedfiles\\" + requestName + ".txt";
			reader = new FileReader(sourcePath);
			bReader = new BufferedReader(reader);
			String newLine = System.lineSeparator();
			String temp = "";
			while ((temp = bReader.readLine()) != null) {
				template = template + temp + newLine;
			   }

		} catch (Exception var29) {
			logger.info("exception - " + var29);
		} finally {
			try {
				if (bReader != null) {
					bReader.close();
				}
			} catch(Exception var30) {
				logger.info(var30.getMessage());
			}
			try {
				if (is != null) {
					is.close();
				}
			} catch(Exception var31) {
				logger.info(var31.getMessage());
			}
			try {
				if (reader != null) {
					reader.close();
				}
			} catch(Exception var32) {
				logger.info(var32.getMessage());
			}			
		}
		return template;
	}
	
	public void prepareXmlRequest(String fileName) {
		
		String val = this.loadRequest(fileName);
		Set<String> keyList = this.context.testData.keySet();
		String data = "empty";
		String keyAttribute = null;
		Iterator var6 = keyList.iterator();
		
		while(var6.hasNext()) {
			
			String key = (String)var6.next();
			try {
				data = (String)this.context.testData.get(key);
			}catch(Exception var18) {
				logger.info(var18.getMessage());
			}
			
			if (data != null) {
					val = val.replace("${" + key + "}", data);
			}
		}	
		logger.info(val);
		this.context.testData.put("TEMPLATE", val);
		SingletonRunner singletonRunner = SingletonRunner.getInstance();
		singletonRunner.getRunner().createRequestResponseLogs((String)this.context.testCaseID, "Request", val);
		logger.info("json request created and saved in path - " + fileName );
	}
	
	public void sendSoapRequest() {
		String requestData = (String)this.context.testData.get("TEMPLATE");
		TestNGXMLGenerator testNGXMLGenerator = new TestNGXMLGenerator();
		try {
			
			String region = testNGXMLGenerator.getValues("Region");
			String endPontURL = testNGXMLGenerator.getValues("EndPontURL" + region);
			logger.info("end point url - " + endPontURL);
			CloseableHttpClient client = HttpClients.createDefault();
		    HttpPost httpPost = new HttpPost(endPontURL);
		    StringEntity entity = new StringEntity(requestData);
		    httpPost.setEntity(entity);
		    if (endPontURL != null){
				String header = testNGXMLGenerator.getValues("Header" + region);
				//logger.info("end point url - " + header);
				String[] headerValues = header.split(";");
				for(int i = 0; i < headerValues.length; ++i) {
					String[] values = headerValues[i].split(":");
					httpPost.setHeader(values[0],values[1]);	
				}
			}
		    CloseableHttpResponse response = client.execute(httpPost);
		    if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code :" +  response.getStatusLine().getStatusCode());
			}
		    
		    HttpEntity entity1 = response.getEntity();
		    String responseString = EntityUtils.toString(entity1);
			logger.info("XML Response  - " + responseString);
			SingletonRunner singletonRunner = SingletonRunner.getInstance();
			this.context.testData.put("responseString", responseString);
			this.context.testData.put("serviceResponse", responseString);
			singletonRunner.getRunner().createRequestResponseLogs((String)this.context.testCaseID, "Response", this.context.testData.get("responseString"));
			 client.close();
		}catch(Exception var1) {
			logger.info("response failed " + var1 + System.lineSeparator());
			this.context.status = true;
		}
	}
		
}
