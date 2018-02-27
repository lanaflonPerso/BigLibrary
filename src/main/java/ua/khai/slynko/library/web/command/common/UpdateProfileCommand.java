package ua.khai.slynko.library.web.command.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.security.Password;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * Update profile command.
 * 
 * @author O.Slynko
 * 
 */
public class UpdateProfileCommand extends Command {

	private static final Logger LOG = Logger.getLogger(UpdateProfileCommand.class);

	private static final String REQUEST_FIRST_NAME = "firstName";
	private static final String REQUEST_LAST_NAME = "lastName";
	private static final String REQUEST_LOGIN = "login";
	private static final String REQUEST_EMAIL = "email";
	private static final String REQUEST_OLD_PASSWORD_1 = "oldPassword1";
	private static final String REQUEST_OLD_PASSWORD_2 = "oldPassword2";
	private static final String REQUEST_NEW_PASSWORD_1 = "newPassword1";
	private static final String REQUEST_NEW_PASSWORD_2 = "newPassword2";
	private static final String REQUEST_USER = "user";

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, AppException {
		String address;
		if (!inputDataIsValid(request)) {
			address = Path.PAGE_UPDATE_PROFILE;
		} else {
			HttpSession session = request.getSession();
			String oldPassword = request.getParameter(REQUEST_OLD_PASSWORD_1);
			String newPassword = request.getParameter(REQUEST_NEW_PASSWORD_1);
			User user = (User) session.getAttribute(REQUEST_USER);
			user.setLogin(request.getParameter(REQUEST_LOGIN));
			try {
				if (StringUtils.isEmpty(newPassword)) {
					user.setPassword(Password.hash(oldPassword));
				} else {
					user.setPassword(Password.hash(newPassword));
				}
			} catch (NoSuchAlgorithmException e) {
				throw new AppException("NoSuchAlgorithm", e);
			}
			user.setFirstName(request.getParameter(REQUEST_FIRST_NAME));
			user.setLastName(request.getParameter(REQUEST_LAST_NAME));
			user.setEmail(request.getParameter(REQUEST_EMAIL));
			DBManager.getInstance().updateUser(user);
			session.setAttribute(REQUEST_USER, user);
			request.setAttribute("sendRedirect", true);
			address = Path.PAGE_HOME_REDERECT;
			session.setAttribute("redirectPage", Path.COMMAND_SETTINGS);
			session.setAttribute("profileUpdateIsSuccessful", true);
		}
		return address;
	}

	private boolean inputDataIsValid(HttpServletRequest request) throws AppException {
		boolean isValid = true;
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(REQUEST_USER);
		DBManager dbManager = DBManager.getInstance();
		Map<String, String> valuesMap = initMap(request);
		ResourceBundle rb = getResourceBundle(session);
		Pattern emailPattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9\\.-]*\\@[a-zA-Z]+\\.[a-zA-Z]+$");
		if (valuesMap.get(REQUEST_FIRST_NAME) == null && valuesMap.get(REQUEST_LAST_NAME) == null
				&& valuesMap.get(REQUEST_LOGIN) == null && valuesMap.get(REQUEST_EMAIL) == null) {
			request.setAttribute("fillInMessage", rb.getString("signup.fieldsAreNotFilled"));
			isValid = false;
			return isValid;
		}
		if (valuesMap.get(REQUEST_FIRST_NAME) == null || valuesMap.get(REQUEST_FIRST_NAME).length() < 1) {
			request.setAttribute("firstNameMessage", rb.getString("signup.firstNameTooShort"));
			isValid = false;
		} else if (valuesMap.get(REQUEST_FIRST_NAME).length() > 20) {
			request.setAttribute("firstNameMessage", rb.getString("signup.firstNameTooLong"));
			isValid = false;
		} else {
			request.setAttribute(REQUEST_FIRST_NAME, valuesMap.get(REQUEST_FIRST_NAME));
		}
		if (valuesMap.get(REQUEST_LAST_NAME) == null || valuesMap.get(REQUEST_LAST_NAME).length() < 1) {
			request.setAttribute("lastNameMessage", rb.getString("signup.lastNameTooShort"));
			isValid = false;
		} else if (valuesMap.get(REQUEST_LAST_NAME).length() > 20) {
			request.setAttribute("lastNameMessage", rb.getString("signup.lastNameTooLong"));
			isValid = false;
		} else {
			request.setAttribute(REQUEST_LAST_NAME, valuesMap.get(REQUEST_LAST_NAME));
		}
		String loginMessage = "loginMessage";
		if (valuesMap.get(REQUEST_LOGIN) == null || valuesMap.get(REQUEST_LOGIN).length() < 3) {
			request.setAttribute(loginMessage, rb.getString("signup.loginTooShort"));
			isValid = false;
		} else if (valuesMap.get(REQUEST_LOGIN).length() > 20) {
			request.setAttribute(loginMessage, rb.getString("signup.loginTooLong"));
			isValid = false;
		} else if (valuesMap.get(REQUEST_LOGIN).contains(" ")) {
			request.setAttribute(loginMessage, rb.getString("signup.loginContainsSpace"));
			isValid = false;
		} else if (dbManager.findUserByLoginToUpdate(valuesMap.get(REQUEST_LOGIN), user.getId()) != null) {
			request.setAttribute(loginMessage, rb.getString("signup.loginIsTaken"));
			isValid = false;
		} else {
			request.setAttribute(REQUEST_LOGIN, valuesMap.get(REQUEST_LOGIN));
		}
		String emailMessage = "emailMessage";
		if (valuesMap.get(REQUEST_EMAIL) == null || !emailPattern.matcher(valuesMap.get(REQUEST_EMAIL)).matches()) {
			request.setAttribute(emailMessage, rb.getString("signup.emailIsNotValid"));
			isValid = false;
		} else if (valuesMap.get(REQUEST_EMAIL).length() < 5) {
			request.setAttribute(emailMessage, rb.getString("signup.emailTooShort"));
			isValid = false;
		} else if (valuesMap.get(REQUEST_EMAIL).length() > 30) {
			request.setAttribute(emailMessage, rb.getString("signup.emailTooLong"));
			isValid = false;
		} else if (valuesMap.get(REQUEST_EMAIL).contains(" ")) {
			request.setAttribute(emailMessage, rb.getString("signup.emailContainsSpace"));
			isValid = false;
		} else if (dbManager.findUserByEmailToUpdate(valuesMap.get(REQUEST_EMAIL), user.getId()) != null) {
			request.setAttribute(emailMessage, rb.getString("signup.emailIsTaken"));
			isValid = false;
		} else {
			request.setAttribute(REQUEST_EMAIL, valuesMap.get(REQUEST_EMAIL));
		}
		if (valuesMap.get("oldPassword1Hash") == null || valuesMap.get("oldPassword2Hash") == null) {
			request.setAttribute("oldPasswordMessage", rb.getString("updateProfile.oldPasswordIsEmpty"));
			isValid = false;
		} else if (!valuesMap.get("oldPassword1Hash").equals(valuesMap.get("currentPassword"))
				|| !valuesMap.get("oldPassword2Hash").equals(valuesMap.get("currentPassword"))) {
			request.setAttribute("oldPasswordMessage", rb.getString("updateProfile.oldPasswordIsNotCorrect"));
			isValid = false;
		}
		if (valuesMap.get(REQUEST_NEW_PASSWORD_1) == null || valuesMap.get(REQUEST_NEW_PASSWORD_1).length() == 0) {
		} else if (valuesMap.get(REQUEST_NEW_PASSWORD_1).length() < 5) {
			request.setAttribute("passwordMessage", rb.getString("updateProfile.newPasswordTooShort"));
			isValid = false;
		} else if (valuesMap.get(REQUEST_NEW_PASSWORD_1).length() > 20) {
			request.setAttribute("passwordMessage", rb.getString("updateProfile.newPasswordTooLong"));
			isValid = false;
		} else if (valuesMap.get(REQUEST_NEW_PASSWORD_2) == null
				|| !valuesMap.get(REQUEST_NEW_PASSWORD_1).equals(valuesMap.get(REQUEST_NEW_PASSWORD_2))) {
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

	/**
	 * Initialize map from request
	 * 
	 * @param request
	 * @return Map<String, String> with request values
	 */
	private Map<String, String> initMap(ServletRequest request) {
		Map<String, String> hashMap = new HashMap<>();
		HttpSession session = ((HttpServletRequest) request).getSession();
		User user = (User) session.getAttribute(REQUEST_USER);
		hashMap.put(REQUEST_FIRST_NAME, request.getParameter(REQUEST_FIRST_NAME));
		hashMap.put(REQUEST_LAST_NAME, request.getParameter(REQUEST_LAST_NAME));
		hashMap.put(REQUEST_LOGIN, request.getParameter(REQUEST_LOGIN));
		hashMap.put(REQUEST_EMAIL, request.getParameter(REQUEST_EMAIL));
		hashMap.put(REQUEST_NEW_PASSWORD_1, request.getParameter(REQUEST_NEW_PASSWORD_1));
		hashMap.put(REQUEST_NEW_PASSWORD_2, request.getParameter(REQUEST_NEW_PASSWORD_2));
		String oldPassword1 = request.getParameter(REQUEST_OLD_PASSWORD_1);
		String oldPassword2 = request.getParameter(REQUEST_OLD_PASSWORD_2);
		if (oldPassword1 != null && oldPassword2 != null) {
			try {
				hashMap.put("oldPassword1Hash", Password.hash(oldPassword1));
				hashMap.put("oldPassword2Hash", Password.hash(oldPassword2));
			} catch (NoSuchAlgorithmException | UnsupportedEncodingException e1) {
				LOG.error(e1.getMessage(), e1);
			}
		}
		hashMap.put("currentPassword", user.getPassword());
		return hashMap;
	}
}