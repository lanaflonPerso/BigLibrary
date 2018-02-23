package ua.khai.slynko.library.web.command.outOfControl;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.mail.MailHelper;
import ua.khai.slynko.library.security.Password;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * Forgot password command
 * 
 * @author O.Slynko
 * 
 */
public class ForgotPasswordCommand extends Command {

	private static final long serialVersionUID = -3071536593627692473L;
	private static final Logger LOG = Logger.getLogger(ForgotPasswordCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");

		// obtain dbManager and session
		DBManager dbmanager = DBManager.getInstance();
		HttpSession session = request.getSession();

		// set redirect path
		String address = Path.PAGE_LOGIN_REDERECT;

		// obtain email or login
		String forgotPassData = request.getParameter("forgotPassData");
		String loginOrEmail = request.getParameter("loginOrEmail");

		LOG.info("Forgot password data: --> " + forgotPassData);
		LOG.info("Login or email: --> " + loginOrEmail);

		// obtain user by login or email
		User user = null;
		if (forgotPassData.equals("login")) {
			user = dbmanager.findUserByLogin(loginOrEmail);
		} else if (forgotPassData.equals("email")) {
			user = dbmanager.findUserByEmail(loginOrEmail);
		}
		LOG.debug("User -->" + user);

		// check if user is null
		if (user != null) {
			// init restoration message params
			String emailToSend = user.getEmail();
			String newPassword = RandomStringUtils.randomAlphabetic(8);
			String subject = "Library forgot password";
			String message = "Your login is: " + user.getLogin() + ", new account password is: " + newPassword;

			LOG.trace("Email to send: --> " + emailToSend);
			LOG.trace("Subject: --> " + subject);
			LOG.trace("Message: --> " + emailToSend);

			// send mail and update user password in db
			try {
				MailHelper.sendMail(emailToSend, subject, message);
				dbmanager.updateUserPasswordByEmail(Password.hash(newPassword), emailToSend);
				session.setAttribute("passwordRestorationIsSuccessful", true);
			} catch (MessagingException ex) {
				LOG.trace("Mail has not been sent. Some error with mail sending");
				session.setAttribute("passwordRestorationIsSuccessful", false);
			} catch (NoSuchAlgorithmException e) {
				LOG.trace("Mail has not been sent. Some error with password hash algorithm");
				session.setAttribute("passwordRestorationIsSuccessful", false);
			}
		} else {
			// set user is not found into session
			session.setAttribute("passwordRestorationUserIsNotFound", true);
		}

		// set redirect true
		request.setAttribute("sendRedirect", true);

		LOG.debug("Command finished");
		
		return address;
	}
}