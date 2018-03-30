package ua.khai.slynko.library.web.command.outOfControl;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.exception.DBException;
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
	public String execute(HttpServletRequest request, HttpServletResponse response)	throws AppException {
		restorePasswordAndSendEmail(request);
		return Path.PAGE_LOGIN_REDERECT;
	}

	private User findUser(HttpServletRequest request) throws DBException {
		User user = null;
		if (isLoginEntered(request)) {
			user = DBManager.getInstance().findUserByLogin(request.getParameter("loginOrEmail"));
		} else if (isEmailEntered(request)) {
			user = DBManager.getInstance().findUserByEmail(request.getParameter("loginOrEmail"));
		}
		return user;
	}

	private boolean isLoginEntered(HttpServletRequest request) {
		return request.getParameter("forgotPassData").equals("login");
	}

	private boolean isEmailEntered(HttpServletRequest request) {
		return request.getParameter("forgotPassData").equals("email");
	}

	private void restorePasswordAndSendEmail(HttpServletRequest request) throws DBException {
		User user = findUser(request);
		if (user != null) {
			String newPassword = RandomStringUtils.randomAlphabetic(PASSWORD_LENGTH);
			try {
				MailHelper.sendMail(user.getEmail(),
						"Library forgot password",
						"Your login is: " + user.getLogin() + ", new account password is: " + newPassword);
				DBManager.getInstance().updateUserPasswordByEmail(Password.hash(newPassword), user.getEmail());
				request.getSession().setAttribute("passwordRestorationIsSuccessful", true);
			} catch (MessagingException ex) {
				LOG.trace("Mail has not been sent. Some error with mail sending");
				request.getSession().setAttribute("passwordRestorationIsSuccessful", false);
			}
		} else {
			request.getSession().setAttribute("passwordRestorationUserIsNotFound", true);
		}
		request.setAttribute("sendRedirect", true);
	}
}