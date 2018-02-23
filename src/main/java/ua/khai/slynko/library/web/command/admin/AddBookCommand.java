package ua.khai.slynko.library.web.command.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.db.DBManager;
import ua.khai.slynko.library.db.entity.CatalogItem;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.validation.model.BookForm;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * Login command.
 *
 * @author O.Slynko
 */
public class AddBookCommand extends Command {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, AppException {
        if (!isInputDataValid(request)) {
            return Path.PAGE_ADD_BOOK;
        } else {
            DBManager.getInstance().createCatalogItem(buildCatalogItem(request));
            request.getSession().setAttribute("bookAddIsSuccessful", true);
            request.setAttribute("sendRedirect", true);
            return Path.PAGE_HOME_REDERECT;
        }
    }

    private boolean isInputDataValid(HttpServletRequest request) throws IOException, ServletException, AppException {
        return buildAddBookForm(request)
                .validateAndPrefillRequestWithErrors(request);
    }

    private BookForm buildAddBookForm(HttpServletRequest request) {
        return new BookForm(
                request.getParameter("bookTitle"), request.getParameter("edition"),
                request.getParameter("author"), request.getParameter("publicationYear"),
                request.getParameter("instancesNumber"));
    }

    private CatalogItem buildCatalogItem(HttpServletRequest request) {
        CatalogItem catalogItem = new CatalogItem();
        catalogItem.setTitle(request.getParameter("bookTitle"));
        catalogItem.setAuthor(request.getParameter("author"));
        catalogItem.setEdition(request.getParameter("edition"));
        catalogItem.setPublicationYear(Integer.parseInt(request.getParameter("publicationYear")));
        catalogItem.setInstancesNumber(Integer.parseInt(request.getParameter("instancesNumber")));
        return catalogItem;
    }
}