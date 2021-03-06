/**
 * 
 */
package com.booking.util;

import java.io.File;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.CharEncoding;
import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

/**
 * @author ddung
 *
 */
public class EmailSender {
	private static final String MY_EMAIL = "*****************";
	private static final String MY_PASSWORD = "************";

	private static final Logger _log = Logger.getLogger(EmailSender.class);

	public static boolean sendEmail(String toEmail, String name, String pathQRCodeImg) {
		boolean result = false;
		try {
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

			mailSender.setHost("smtp.gmail.com");
			mailSender.setPort(587);
			mailSender.setUsername(MY_EMAIL);
			mailSender.setPassword(MY_PASSWORD);

			Properties javaMailProperties = new Properties();
			javaMailProperties.put("mail.smtp.auth", "true");
			javaMailProperties.put("mail.transport.protocol", "smtp");
			javaMailProperties.put("mail.debug", "true");
			javaMailProperties.put("mail.smtp.starttls.enable", "true");

			mailSender.setJavaMailProperties(javaMailProperties);
			MimeMessagePreparator preparator = new MimeMessagePreparator() {
				public void prepare(MimeMessage mimeMessage) throws Exception {
					MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, CharEncoding.UTF_8);
					message.setTo(toEmail);
					message.setFrom(mailSender.getUsername());
					message.setSubject("Booking Infomation");
					if (!pathQRCodeImg.equals("")) {
						File file = new File(pathQRCodeImg);
						message.addAttachment("QRCODE", file);
					}
					message.setText("Booking successfully: " + name, true);
				}
			};
			mailSender.send(preparator);
			result = true;
		} catch (Exception e) {
			_log.error(e);
		}

		return result;
	}

}
