package ua.khai.slynko.library.web.command.outOfControl;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.Role;
import ua.khai.slynko.library.db.entity.User;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.mail.MailHelper;
import ua.khai.slynko.library.security.Password;
import ua.khai.slynko.library.web.abstractCommand.Command;

import static ua.khai.slynko.library.constant.Constants.EMAIL_PATTERN;

/**
 * Sign up command.
 * 
 * @author O.Slynko
 * 
 */
public class SignUpCommand extends Command {

	private static final Logger LOG = Logger.getLogger(SignUpCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws AppException {

		if (request.getSession().getAttribute("userRole") != null) {
			throw new AppException("You do not have permission to access the requested resource");
		}

		String address;
		if (!inputDataIsValid(request)) {
			address = Path.PAGE_SIGN_UP;
		} else {
			User user = new User();
			user.setLogin(request.getParameter("login"));
			user.setPassword(Password.hash(request.getParameter("password1")));
			user.setFirstName(request.getParameter("firstName"));
			user.setLastName(request.getParameter("lastName"));
			user.setEmail(request.getParameter("email"));
			user.setRoleId(Role.READER.getValue());

			DBManager.getInstance().createReader(user);
			request.setAttribute("sendRedirect", true);

			address = Path.PAGE_LOGIN_REDERECT;
			request.getSession().setAttribute("signUpSuccessful", true);

			String email = user.getEmail();
			String subject = "Library registration";
			String message = "Registration is successful!" + " Your username is: " + user.getLogin() + " , password: "
					+ request.getParameter("password1");
			try {
				MailHelper.sendMail(email, subject, message);
			} catch (Exception ex) {
				LOG.trace("Mail has not been sent");
			}

		}
		return address;
	}

	private boolean inputDataIsValid(HttpServletRequest request) throws AppException {
		boolean isValid = true;

		DBManager dbManager = DBManager.getInstance();

		String firstName = request.getParameter("firstName");
		String lastName = request.getParameter("lastName");
		String login = request.getParameter("login");
		String email = request.getParameter("email");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		HttpSession session = request.getSession();

		String currentLocale = (String) session.getAttribute("currentLocale");
		ResourceBundle rb;
		if (currentLocale == null) {
			rb = ResourceBundle.getBundle("resources", Locale.getDefault());
		} else {
			rb = ResourceBundle.getBundle("resources", new Locale(currentLocale));
		}

		LOG.trace("Validation starts");

		if (StringUtils.isEmpty(firstName) && StringUtils.isEmpty(lastName)
				&& StringUtils.isEmpty(login) && StringUtils.isEmpty(email)) {
			request.setAttribute("fillInMessage", rb.getString("signup.fieldsAreNotFilled"));
			return false;
		}
		if (StringUtils.isEmpty(firstName)) {
			request.setAttribute("firstNameMessage", rb.getString("signup.firstNameTooShort"));
			isValid = false;
		} else if (firstName.length() > 20) {
			request.setAttribute("firstNameMessage", rb.getString("signup.firstNameTooLong"));
			isValid = false;
		} else {
			request.setAttribute("firstName", firstName);
		}
		if (StringUtils.isEmpty(lastName)) {
			request.setAttribute("lastNameMessage", rb.getString("signup.lastNameTooShort"));
			isValid = false;
		} else if (lastName.length() > 20) {
			request.setAttribute("lastNameMessage", rb.getString("signup.lastNameTooLong"));
			isValid = false;
		} else {
			request.setAttribute("lastName", lastName);
		}
		String loginMessage = "loginMessage";
		if (StringUtils.isEmpty(login) || login.length() < 3) {
			request.setAttribute(loginMessage, rb.getString("signup.loginTooShort"));
			isValid = false;
		} else if (login.length() > 20) {
			request.setAttribute(loginMessage, rb.getString("signup.loginTooLong"));
			isValid = false;
		} else if (login.contains(" ")) {
			request.setAttribute(loginMessage, rb.getString("signup.loginContainsSpace"));
			isValid = false;
		} else if (dbManager.findUserByLogin(login) != null) {
			request.setAttribute(loginMessage, rb.getString("signup.loginIsTaken"));
			isValid = false;
		} else {
			request.setAttribute("login", login);
		}
		String emailMessage = "emailMessage";
		if (StringUtils.isEmpty(email) || !Pattern.compile(EMAIL_PATTERN).matcher(email).matches()) {
			request.setAttribute(emailMessage, rb.getString("signup.emailIsNotValid"));
			isValid = false;
		} else if (email.length() < 5) {
			request.setAttribute(emailMessage, rb.getString("signup.emailTooShort"));
			isValid = false;
		} else if (email.length() > 30) {
			request.setAttribute(emailMessage, rb.getString("signup.emailTooLong"));
			isValid = false;
		} else if (email.contains(" ")) {
			request.setAttribute(emailMessage, rb.getString("signup.emailContainsSpace"));
			isValid = false;
		} else if (dbManager.findUserByEmail(email) != null) {
			request.setAttribute(emailMessage, rb.getString("signup.emailIsTaken"));
			isValid = false;
		} else {
			request.setAttribute("email", email);
		}
		if (StringUtils.isEmpty(password1) || password1.length() < 5) {
			request.setAttribute("passwordMessage", rb.getString("signup.passwordTooShort"));
			isValid = false;
		} else if (password1.length() > 20) {
			request.setAttribute("passwordMessage", rb.getString("signup.passwordTooLong"));
			isValid = false;
		} else if (StringUtils.isEmpty(password2) || !password1.equals(password2)) {
			request.setAttribute("passwordMessage", rb.getString("signup.passwordsAreNotEqual"));
			isValid = false;
		}

		return isValid;
	}
}