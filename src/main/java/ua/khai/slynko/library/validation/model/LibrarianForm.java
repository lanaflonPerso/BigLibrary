package ua.khai.slynko.library.validation.model;

import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.exception.DBException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import static ua.khai.slynko.library.constant.Constants.EMAIL_MAX_LENGTH;
import static ua.khai.slynko.library.constant.Constants.EMAIL_MIN_LENGTH;
import static ua.khai.slynko.library.constant.Constants.EMAIL_PATTERN;
import static ua.khai.slynko.library.constant.Constants.LOGIN_MAX_LENGTH;
import static ua.khai.slynko.library.constant.Constants.LOGIN_MIN_LENGTH;
import static ua.khai.slynko.library.constant.Constants.PASSWORD_MAX_LENGTH;
import static ua.khai.slynko.library.constant.Constants.PASSWORD_MIN_LENGTH;
import static ua.khai.slynko.library.constant.Constants.STRING_MAX_LENGTH;
import static ua.khai.slynko.library.constant.Constants.STRING_MIN_LENGTH;

public class LibrarianForm {
    private String firstName;
    private String lastName;
    private String email;
    private String login;
    private String password;
    private String passwordConfirm;

    public LibrarianForm(String firstName, String lastName, String email, String login, String password, String passwordConfirm) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.login = login;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
    }

    public boolean validateAndPrefillRequestWithErrors(HttpServletRequest request) throws DBException {
        boolean isValid = true;
        Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
        HttpSession session = request.getSession();
        String currentLocale = (String) session.getAttribute("currentLocale");
        ResourceBundle rb;
        if (currentLocale == null) {
            rb = ResourceBundle.getBundle("resources", Locale.getDefault());
        } else {
            rb = ResourceBundle.getBundle("resources", new Locale(currentLocale));
        }

        String capthaAnswer = request.getParameter("capthaAnswer");

        if (login == null && email == null && firstName == null && lastName == null) {
            request.setAttribute("fillInMessage", rb.getString("signup.fieldsAreNotFilled"));
            isValid = false;
            return isValid;
        }
        if (firstName == null || firstName.length() < STRING_MIN_LENGTH) {
            request.setAttribute("firstNameMessage", rb.getString("signup.firstNameTooShort"));
            isValid = false;
        } else if (firstName.length() > STRING_MAX_LENGTH) {
            request.setAttribute("firstNameMessage", rb.getString("signup.firstNameTooLong"));
            isValid = false;
        } else {
            request.setAttribute("firstName", firstName);
        }
        if (lastName == null || lastName.length() < STRING_MIN_LENGTH) {
            request.setAttribute("lastNameMessage", rb.getString("signup.lastNameTooShort"));
            isValid = false;
        } else if (lastName.length() > STRING_MAX_LENGTH) {
            request.setAttribute("lastNameMessage", rb.getString("signup.lastNameTooLong"));
            isValid = false;
        } else {
            request.setAttribute("lastName", lastName);
        }
        String loginMessage = "loginMessage";
        if (login == null || login.length() < LOGIN_MIN_LENGTH) {
            request.setAttribute(loginMessage, rb.getString("signup.loginTooShort"));
            isValid = false;
        } else if (login.length() > LOGIN_MAX_LENGTH) {
            request.setAttribute(loginMessage, rb.getString("signup.loginTooLong"));
            isValid = false;
        } else if (login.contains(" ")) {
            request.setAttribute(loginMessage, rb.getString("signup.loginContainsSpace"));
            isValid = false;
        } else if (DBManager.getInstance().findUserByLogin(login) != null) {
            request.setAttribute(loginMessage, rb.getString("signup.loginIsTaken"));
            isValid = false;
        } else {
            request.setAttribute("login", login);
        }
        String emailMessage = "emailMessage";
        if (email == null || !emailPattern.matcher(email).matches()) {
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
        } else if (DBManager.getInstance().findUserByEmail(email) != null) {
            request.setAttribute(emailMessage, rb.getString("signup.emailIsTaken"));
            isValid = false;
        } else {
            request.setAttribute("email", email);
        }
        if (password == null || password.length() < PASSWORD_MIN_LENGTH) {
            request.setAttribute("passwordMessage", rb.getString("signup.passwordTooShort"));
            isValid = false;
        } else if (password.length() > PASSWORD_MAX_LENGTH) {
            request.setAttribute("passwordMessage", rb.getString("signup.passwordTooLong"));
            isValid = false;
        } else if (passwordConfirm == null || !password.equals(passwordConfirm)) {
            request.setAttribute("passwordMessage", rb.getString("signup.passwordsAreNotEqual"));
            isValid = false;
        }
        return isValid;
    }
}
