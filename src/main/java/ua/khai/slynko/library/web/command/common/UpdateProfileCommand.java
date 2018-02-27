package ua.khai.slynko.library.web.command.common;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.security.Password;
import ua.khai.slynko.library.web.abstractCommand.Command;

import static ua.khai.slynko.library.constant.Constants.EMAIL_MAX_LENGTH;
import static ua.khai.slynko.library.constant.Constants.EMAIL_MIN_LENGTH;
import static ua.khai.slynko.library.constant.Constants.EMAIL_PATTERN;
import static ua.khai.slynko.library.constant.Constants.LOGIN_MAX_LENGTH;
import static ua.khai.slynko.library.constant.Constants.LOGIN_MIN_LENGTH;
import static ua.khai.slynko.library.constant.Constants.PASSWORD_MAX_LENGTH;
import static ua.khai.slynko.library.constant.Constants.PASSWORD_MIN_LENGTH;
import static ua.khai.slynko.library.constant.Constants.STRING_MAX_LENGTH;

/**
 * Update profile command.
 * 
 * @author O.Slynko
 * 
 */
public class UpdateProfileCommand extends Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) throws AppException {
		if (!inputDataIsValid(request)) {
			return Path.PAGE_UPDATE_PROFILE;
		} else {
			User user = buildUser(request);
			DBManager.getInstance().updateUser(user);
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			session.setAttribute("redirectPage", Path.COMMAND_SETTINGS);
			session.setAttribute("profileUpdateIsSuccessful", true);
			request.setAttribute("sendRedirect", true);
			return Path.PAGE_HOME_REDERECT;
		}
	}

	private User buildUser(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("user");
		user.setLogin(request.getParameter("login"));
			user.setPassword(Password.hash(StringUtils.isEmpty(request.getParameter("newPassword1"))
					? request.getParameter("oldPassword1")
					: request.getParameter("newPassword1")));
		user.setFirstName(request.getParameter("firstName"));
		user.setLastName(request.getParameter("lastName"));
		user.setEmail(request.getParameter("email"));
		return user;
	}

	private boolean inputDataIsValid(HttpServletRequest request) throws AppException {
		boolean isValid = true;
		User user = (User) request.getSession().getAttribute("user");

		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String login = request.getParameter("login");
		String email = request.getParameter("email");
		String oldPassword1 = request.getParameter("oldPassword1");
		String oldPassword2 = request.getParameter("oldPassword2");
		String newPassword1 = request.getParameter("newPassword1");
		String newPassword2 = request.getParameter("newPassword2");

		ResourceBundle rb = getResourceBundle(request.getSession());
		if (StringUtils.isEmpty(firstName) && StringUtils.isEmpty(lastName)
				&& StringUtils.isEmpty(login) && StringUtils.isEmpty(email)) {
			request.setAttribute("fillInMessage", rb.getString("signup.fieldsAreNotFilled"));
			isValid = false;
			return isValid;
		}
		if (StringUtils.isEmpty(firstName)) {
			request.setAttribute("firstNameMessage", rb.getString("signup.firstNameTooShort"));
			isValid = false;
		} else if (firstName.length() > STRING_MAX_LENGTH) {
			request.setAttribute("firstNameMessage", rb.getString("signup.firstNameTooLong"));
			isValid = false;
		} else {
			request.setAttribute("firstName", firstName);
		}
		if (StringUtils.isEmpty(lastName)) {
			request.setAttribute("lastNameMessage", rb.getString("signup.lastNameTooShort"));
			isValid = false;
		} else if (lastName.length() > STRING_MAX_LENGTH) {
			request.setAttribute("lastNameMessage", rb.getString("signup.lastNameTooLong"));
			isValid = false;
		} else {
			request.setAttribute("lastName", lastName);
		}
		String loginMessage = "loginMessage";
		if (StringUtils.isEmpty(login) || login.length() < LOGIN_MIN_LENGTH) {
			request.setAttribute(loginMessage, rb.getString("signup.loginTooShort"));
			isValid = false;
		} else if (login.length() > LOGIN_MAX_LENGTH) {
			request.setAttribute(loginMessage, rb.getString("signup.loginTooLong"));
			isValid = false;
		} else if (login.contains(" ")) {
			request.setAttribute(loginMessage, rb.getString("signup.loginContainsSpace"));
			isValid = false;
		} else if (DBManager.getInstance().findUserByLoginToUpdate(login, user.getId()) != null) {
			request.setAttribute(loginMessage, rb.getString("signup.loginIsTaken"));
			isValid = false;
		} else {
			request.setAttribute("login", login);
		}
		String emailMessage = "emailMessage";
		if (StringUtils.isEmpty(email)
				|| !Pattern.compile(EMAIL_PATTERN).matcher(email).matches()) {
			request.setAttribute(emailMessage, rb.getString("signup.emailIsNotValid"));
			isValid = false;
		} else if (email.length() < EMAIL_MIN_LENGTH) {
			request.setAttribute(emailMessage, rb.getString("signup.emailTooShort"));
			isValid = false;
		} else if (email.length() > EMAIL_MAX_LENGTH) {
			request.setAttribute(emailMessage, rb.getString("signup.emailTooLong"));
			isValid = false;
		} else if (email.contains(" ")) {
			request.setAttribute(emailMessage, rb.getString("signup.emailContainsSpace"));
			isValid = false;
		} else if (DBManager.getInstance().findUserByEmailToUpdate(email, user.getId()) != null) {
			request.setAttribute(emailMessage, rb.getString("signup.emailIsTaken"));
			isValid = false;
		} else {
			request.setAttribute("email", email);
		}
		if (StringUtils.isEmpty(oldPassword1) || StringUtils.isEmpty(oldPassword2)) {
			request.setAttribute("oldPasswordMessage", rb.getString("updateProfile.oldPasswordIsEmpty"));
			isValid = false;
		} else if (!Password.hash(oldPassword1).equals(user.getPassword())
				|| !Password.hash(oldPassword2).equals(user.getPassword())) {
			request.setAttribute("oldPasswordMessage", rb.getString("updateProfile.oldPasswordIsNotCorrect"));
			isValid = false;
		}
		if (StringUtils.isEmpty(newPassword1)) {
		} else if (newPassword1.length() < PASSWORD_MIN_LENGTH) {
			request.setAttribute("passwordMessage", rb.getString("updateProfile.newPasswordTooShort"));
			isValid = false;
		} else if (newPassword1.length() > PASSWORD_MAX_LENGTH) {
			request.setAttribute("passwordMessage", rb.getString("updateProfile.newPasswordTooLong"));
			isValid = false;
		} else if (StringUtils.isEmpty(newPassword2)
				|| !newPassword1.equals(newPassword2)) {
			request.setAttribute("passwordMessage", rb.getString("updateProfile.newPasswordsAreNotEqual"));
			isValid = false;
		}
		return isValid;
	}

	private ResourceBundle getResourceBundle(HttpSession session) {
		ResourceBundle rb;
		String currentLocale = (String) session.getAttribute("currentLocale");
		if (currentLocale == null) {
			rb = ResourceBundle.getBundle("resources", Locale.getDefault());
		} else {
			rb = ResourceBundle.getBundle("resources", new Locale(currentLocale));
		}
		return rb;
	}
}