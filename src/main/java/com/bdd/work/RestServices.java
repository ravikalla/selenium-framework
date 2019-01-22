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

public class RestServices {
	
	public ExecutionContext context = (ExecutionContext)MyContainer.getInstance(Thread.currentThread().getId());
	Runner runner = new Runner();
	static final Logger logger = Logger.getLogger(RestServices.class);
	
	public RestServices() {
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
	
	public void restRequest(String fileName) {
		
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
			
			if (data != null && data.equals("*")) {
				val = val.replace("${" + key + "}", "");
			}else if(data != null) {
				if(data.equals("pending TransactionID")) {	
					//val = val.replace("${" + key + "}", this.context.pendingTransactionID_resubmit);				
				}else {
					val = val.replace("${" + key + "}", data);
				}
			}else {
				
				if (key.contains("_")) {
					String [] actKey = key.split("_");
					keyAttribute = actKey[1];
				}
				
				val = val.replace("\"name\":\"" + key + "\",", "");
				val = val.replace("\"name\":\"" + key + "\"", "");
				
				val = val.replace("\"" + keyAttribute + "\":\"${" + key + "}\",", "");
				val = val.replace("\"" + keyAttribute + "\":\"${" + key + "}\"", "");
				
				val = val.replace("\"value\":\"${" + key + "}\",", "");
				val = val.replace("\"value\":\"${" + key + "}\"", "");

				val = val.replace("\"" + key + "\":\"${" + key + "}\",", "");
				val = val.replace("\"" + key + "\":\"${" + key + "}\"", "");
				
				int fromIndex = 0;
				int lastComma;
				for (lastComma = 0; lastComma < val.length(); ++lastComma) {
						int openIndex = val.indexOf("{", fromIndex);
						int closeIndex = val.indexOf("}", openIndex);
						
						if (closeIndex >= val.length() || closeIndex == -1 || openIndex == -1) {
							break;
						}
						
						if (val.substring(openIndex +1,  closeIndex -1).trim().isEmpty()) {
							if (val.substring(closeIndex + 1, closeIndex + 2).equals(",")) {
								val = val.replace(val.substring(openIndex, closeIndex + 2), "");
								fromIndex = closeIndex + 2;
							}else {
								val = val.replace(val.substring(openIndex, closeIndex + 1), "");
								fromIndex = closeIndex = 1;
							}						
						}else {
							fromIndex = closeIndex = 1;
						}
				}
				
				int lastCamma = 0;
				int firstCamma;
				for (firstCamma = 0; firstCamma < val.length(); ++firstCamma) {
					int lastSqBracket = val.indexOf("]", lastCamma);
					if (firstCamma >= val.length() || lastSqBracket == -1 || lastCamma == -1) {
						break;
					}
					
					String subString = val.substring(0, lastSqBracket);
					lastCamma = subString.lastIndexOf(",");
					if (val.substring(lastCamma + 1, lastSqBracket).trim().equalsIgnoreCase("")) {
						val = val.substring(0,lastCamma) + val.substring(lastCamma + 1);
					}
					
					lastCamma = lastSqBracket +1;
					firstCamma = lastSqBracket +1;
					
				}
				
				firstCamma = 0;
				
				for (int i = 0; i < val.length(); ++i) {
					int firstSqBracket = val.indexOf("[", firstCamma);
					if (i >= val.length() || firstSqBracket == -1 || firstCamma == -1) {
						break;
					}
					
					String firstSubString = val.substring(firstSqBracket, val.length()); 
					firstCamma = firstSubString.indexOf(",");
					if(firstCamma >= 0 && firstSubString.substring(firstSubString.indexOf("[") + 1, firstCamma).trim().equalsIgnoreCase("")); {
						val = val.substring(0, val.indexOf(",", firstSqBracket)) + val.substring(val.indexOf(",", firstSqBracket) +1);
					}
					firstCamma = firstSqBracket + 1;
					i = firstSqBracket + 1;		
				}
			}
		}	
		logger.info(val);
		this.context.testData.put("TEMPLATE", val);
		SingletonRunner singletonRunner = SingletonRunner.getInstance();
		singletonRunner.getRunner().createRequestResponseLogs((String)this.context.testCaseID, "Request", val);
		logger.info("json request created and saved in path - " + fileName );
	}
	
	public void jsonRequest() {
		this.getResponse((String)this.context.testData.get("TEMPLATE"));
	}
	
	public void getResponse(String requestData) {
		//https://www.baeldung.com/httpclient-post-http-request
		//https://www.mkyong.com/java/apache-httpclient-examples/
		TestNGXMLGenerator testNGXMLGenerator = new TestNGXMLGenerator();
		try {
			String url = testNGXMLGenerator.getValues("EndPontURLINT");
			logger.info("end point url - " + url);
			CloseableHttpClient client = HttpClients.createDefault();
		    HttpPost httpPost = new HttpPost(url);
		    StringEntity entity = new StringEntity(requestData);
		    httpPost.setEntity(entity);
		    //httpPost.setHeader("Accept", "application/json");
		    //httpPost.setHeader("Content-type", "application/json");
		    if (testNGXMLGenerator.getValues("EndPontURLINT") != null){
				String header = testNGXMLGenerator.getValues("HeaderINT");
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
			logger.info("Response  - " + responseString);
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
