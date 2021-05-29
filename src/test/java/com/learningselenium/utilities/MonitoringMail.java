package com.learningselenium.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MonitoringMail {
	
	static Properties emailConfig = new Properties();

	static {
		
		FileInputStream fis;
		try {
			fis = new FileInputStream("src\\test\\resources\\Properties\\EmailConfig.properties");
			emailConfig.load(fis);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public static void sendMail(String attachmentFilePath) throws MessagingException, AddressException
	{
		
		String mailServer = emailConfig.getProperty("server");
		String from = emailConfig.getProperty("from");
		String toValues = emailConfig.getProperty("to");
		String[] to = toValues.split(";");
		
		String subject = emailConfig.getProperty("subject");
		String messageBody = emailConfig.getProperty("messageBody");
		String attachmentPath = attachmentFilePath;		
		String attachName = attachmentPath.substring(attachmentPath.lastIndexOf('\\'),attachmentPath.length());		
		String attachmentName = attachName;	
		
		
		boolean debug = false;
		Properties props = new Properties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.EnableSSL.enable","true");
		props.put("mail.smtp.auth", "true");

		props.put("mail.smtp.host", mailServer); 
		props.put("mail.debug", "true");		
		
	     props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");   
	     props.setProperty("mail.smtp.socketFactory.fallback", "false");   
	     props.setProperty("mail.smtp.port", "465");   
	     props.setProperty("mail.smtp.socketFactory.port", "465"); 	     

		
		  Authenticator auth = new SMTPAuthenticator();
		    Session session = Session.getDefaultInstance(props, auth);

		    session.setDebug(debug);
		
		try
		{
			
			
			Transport bus = session.getTransport("smtp");
			bus.connect();
            Message message = new MimeMessage(session);
        
         //X-Priority values are generally numbers like 1 (for highest priority), 3 (normal) and 5 (lowest).
            
             message.addHeader("X-Priority", "1");
             message.setFrom(new InternetAddress(from));
             InternetAddress[] addressTo = new InternetAddress[to.length];
             for (int i = 0; i < to.length; i++)
      		 addressTo[i] = new InternetAddress(to[i]);
             message.setRecipients(Message.RecipientType .TO, addressTo);
             message.setSubject(subject);
                  
             
             BodyPart body = new MimeBodyPart();

            // body.setText(messageBody);
            body.setContent(messageBody,"text/html");

             BodyPart attachment = new MimeBodyPart();
             DataSource source = new FileDataSource(attachmentPath);
             attachment.setDataHandler(new DataHandler(source));
             attachment.setFileName(attachmentName);
             MimeMultipart multipart = new MimeMultipart();
             multipart.addBodyPart(body);
             multipart.addBodyPart(attachment);
             message.setContent(multipart);
             Transport.send(message);
             System.out.println("Sucessfully Sent mail to All Users");
         	 bus.close();
    		
		}
		catch (MessagingException mex)
		{
            mex.printStackTrace();
        }		
	}

}


class SMTPAuthenticator extends javax.mail.Authenticator {
		
	static Properties emailConfig = new Properties();

	static {
		
		FileInputStream fis;
		try {
			fis = new FileInputStream("src\\test\\resources\\Properties\\EmailConfig.properties");
			emailConfig.load(fis);
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
	}

	public PasswordAuthentication getPasswordAuthentication() {
		String username = emailConfig.getProperty("from");
		String password = emailConfig.getProperty("password");
		return new PasswordAuthentication(username, password);
	}
}