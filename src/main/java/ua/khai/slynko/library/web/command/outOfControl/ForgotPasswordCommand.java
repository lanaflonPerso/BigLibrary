package ua.khai.slynko.library.web.command.outOfControl;

import javax.mail.MessagingException;
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

	private static final Logger LOG = Logger.getLogger(ForgotPasswordCommand.class);
	private static final Integer PASSWORD_LENGTH = 8;

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws AppException {
		DBManager dbmanager = DBManager.getInstance();
		HttpSession session = request.getSession();
		String forgotPassData = request.getParameter("forgotPassData");
		String loginOrEmail = request.getParameter("loginOrEmail");
		User user = null;
		if (forgotPassData.equals("login")) {
			user = dbmanager.findUserByLogin(loginOrEmail);
		} else if (forgotPassData.equals("email")) {
			user = dbmanager.findUserByEmail(loginOrEmail);
		}
		if (user != null) {
			String emailToSend = user.getEmail();
			String newPassword = RandomStringUtils.randomAlphabetic(PASSWORD_LENGTH);
			String subject = "Library forgot password";
			String message = "Your login is: " + user.getLogin() + ", new account password is: " + newPassword;

			try {
				MailHelper.sendMail(emailToSend, subject, message);
				dbmanager.updateUserPasswordByEmail(Password.hash(newPassword), emailToSend);
				session.setAttribute("passwordRestorationIsSuccessful", true);
			} catch (MessagingException ex) {
				LOG.trace("Mail has not been sent. Some error with mail sending");
				session.setAttribute("passwordRestorationIsSuccessful", false);
			}
		} else {
			session.setAttribute("passwordRestorationUserIsNotFound", true);
		}
		request.setAttribute("sendRedirect", true);
		return Path.PAGE_LOGIN_REDERECT;
	}
}