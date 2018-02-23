package ua.khai.slynko.library.mail;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Mail helper
 * 
 * @author O.Slynko
 *
 */
public class MailHelper {
	static final String USER_NAME = "library.slynko@gmail.com";
	static final String PASSWORD = "library.slynko123";

	public static void sendMail(String mail, String subject, String message) throws MessagingException {
		Message msg = new MimeMessage(getSession());
		msg.setFrom(new InternetAddress(USER_NAME));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail));
		msg.setSubject(subject);
		msg.setText(message);
		Transport.send(msg);
	}

	private static Session getSession() {
		Session session = Session.getInstance(getProperties(), new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(USER_NAME, PASSWORD);
			}
		});
		return session;
	}

	private static Properties getProperties() {
		Properties properties = new Properties();
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "465");
		return properties;
	}
}