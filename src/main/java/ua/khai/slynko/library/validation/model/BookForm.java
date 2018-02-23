package ua.khai.slynko.library.validation.model;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import static ua.khai.slynko.library.constant.Constants.MAX_STRING_LENGTH;

public class BookForm {
    private String title;
    private String edition;
    private String author;
    private String publicationYear;
    private String instancesNumber;

    public BookForm(String title, String edition, String author, String publicationYear, String instancesNumber) {
        this.title = title;
        this.edition = edition;
        this.author = author;
        this.publicationYear = publicationYear;
        this.instancesNumber = instancesNumber;
    }

    public boolean validateAndPrefillRequestWithErrors(HttpServletRequest request) {
        boolean isValid = true;
        String currentLocale = (String) request.getSession().getAttribute("currentLocale");
        ResourceBundle rb;
        if (currentLocale == null) {
            rb = ResourceBundle.getBundle("resources", Locale.getDefault());
        } else {
            rb = ResourceBundle.getBundle("resources", new Locale(currentLocale));
        }
        if (StringUtils.isEmpty(title)) {
            request.setAttribute("titleMessage", rb.getString("modifyBook.titleIsEmpty"));
            isValid = false;
        } else if (title.length() > MAX_STRING_LENGTH) {
            request.setAttribute("titleMessage", rb.getString("modifyBook.titleIsTooLong"));
            isValid = false;
        } else {
            request.setAttribute("bookTitle", title);
        }
        if (StringUtils.isEmpty(author)) {
            request.setAttribute("authorMessage", rb.getString("modifyBook.authorIsEmpty"));
            isValid = false;
        } else if (author.length() > MAX_STRING_LENGTH) {
            request.setAttribute("authorMessage", rb.getString("modifyBook.authorIsTooLong"));
            isValid = false;
        } else {
            request.setAttribute("author", author);
        }
        if (StringUtils.isEmpty(edition)) {
            request.setAttribute("editionMessage", rb.getString("modifyBook.editionIsEmpty"));
            isValid = false;
        } else if (edition.length() > MAX_STRING_LENGTH) {
            request.setAttribute("editionMessage", rb.getString("modifyBook.editionIsTooLong"));
            isValid = false;
        } else {
            request.setAttribute("edition", edition);
        }
        if (!StringUtils.isNumeric(publicationYear)) {
            request.setAttribute("publicationYearMessage", rb.getString("modifyBook.notANumber"));
            isValid = false;
        } else if (Integer.parseInt(publicationYear) > Calendar.getInstance().get(Calendar.YEAR)) {
            request.setAttribute("publicationYearMessage", rb.getString("modifyBook.publicationYearIsNotValid"));
            isValid = false;
        } else {
            request.setAttribute("publicationYear", publicationYear);
        }
        if (!StringUtils.isNumeric(instancesNumber)) {
            request.setAttribute("instancesNumberMessage", rb.getString("modifyBook.notANumber"));
            isValid = false;
        } else {
            request.setAttribute("instancesNumber", instancesNumber);
        }
        return isValid;
    }
}
