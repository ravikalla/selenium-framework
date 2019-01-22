package com.bdd.work;

import java.util.Properties;
import java.io.*;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;

import javax.mail.Session;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;
import com.bdd.utilities.ExecutionContext;
import com.bdd.utilities.MyContainer;
import com.bdd.work.Security;

public class EmailService {
	
	static final Logger logger = Logger.getLogger(EmailService.class);
	ExecutionContext context = (ExecutionContext)MyContainer.getInstance(Thread.currentThread().getId());	
	private static final String CONFIGSET = "ConfigSet";
	private static final int PORT = 587;
	private String SUBJECT;
	
	Security security;
	
	public EmailService() {
		this.SUBJECT  = "Krishna";
		//this.SUBJECT = this.context.customer + "Automation execution results";
		this.security = new Security();
	}
	
	public void sendMail(File zipOutPut) throws MessagingException, UnsupportedEncodingException {
		//https://www.programcreek.com/java-api-examples/?class=javax.mail.Transport&method=connect
		String FROM = "contactmkreddy@gmail.com";
		String FROMNAME = "Krishna Reddy Manubolu";
		String SMTP_USERNAME = "contactmkreddy";
		String SMTP_PASSWORD = "ramana";//this.security.decryptPassword("");
		String HOST = "smtp.gmail.com";//"mail-sa-atl.eis.equifax.com";
		String toList = "kmanubolu@gmail.com";
		String ccList = "kmanubolu@gmail.com";
		
		Properties props = System.getProperties();
		props.put("mail.transport.protocol","smtp");
		props.put("mail.smtp.port","587");		
		props.put("mail.smtp.starttls.enable","true");
		props.put("mail.smtp.auth","true");
		
		Session session = Session.getDefaultInstance(props);
		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(FROM,FROMNAME));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		msg.addRecipients(RecipientType.TO, toList);
		msg.addRecipients(RecipientType.CC, ccList);
		msg.setSubject(this.SUBJECT);
		msg.setText("PFA");
		Multipart multipart = new MimeMultipart();
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		DataSource source = new FileDataSource(zipOutPut);
		messageBodyPart.setDataHandler(new DataHandler(source));
		String[] split = zipOutPut.toString().split("\\\\");
		String fileName = split[split.length - 1];
		
		messageBodyPart.setFileName(fileName);
		multipart.addBodyPart(messageBodyPart);
		msg.setContent(multipart);
		msg.setHeader("X-SES-CONFIGURATION-SET", "ConfigSet");
		Transport transport = session.getTransport();
		try {
			logger.info("sending mail...");
			transport.connect(HOST, SMTP_USERNAME, SMTP_PASSWORD);
			transport.sendMessage(msg,msg.getAllRecipients());
		}catch (Exception var2) {
			logger.info("Exception - " + var2);
		}finally {
			
			transport.close();
		}	
	}
	
	
}
