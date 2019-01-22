package com.bdd.work;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import com.bdd.work.EmailService;

public class Work {

	public static void main(String[] args) throws UnsupportedEncodingException, MessagingException {
		
		File zipOutPut = new File("C:\\Selenium\\WorkSpace\\New Text Document.zip");
		
		EmailService emailService = new EmailService();
		emailService.sendMail(zipOutPut);
		
	}

}
