package ua.khai.slynko.library.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.khai.slynko.library.Path;
import ua.khai.slynko.library.exception.AppException;
import ua.khai.slynko.library.web.abstractCommand.Command;
import ua.khai.slynko.library.web.command.CommandContainer;

/**
 * Main servlet controller.
 * 
 * @author O.Slynko
 * 
 */
public class Controller extends HttpServlet {

	private static final long serialVersionUID = 2423353715955164816L;

	private static final Logger LOG = Logger.getLogger(Controller.class);

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		process(request, response);
	}

	/**
	 * Main method of this controller.
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	private void process(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		LOG.debug("Controller starts");
		// extract command name from the request
		String commandName = request.getParameter("command");
		LOG.trace("Request parameter: command --> " + commandName);

		// obtain command object by its name
		Command command = CommandContainer.get(commandName);
		LOG.trace("Obtained command --> " + command);

		// execute command and get forward address
		String address = Path.PAGE_ERROR_PAGE;
		try {
			address = command.execute(request, response);
		} catch (AppException ex) {
			request.setAttribute("errorMessage", ex.getMessage());
		}
		LOG.trace("Forward address --> " + address);

		LOG.debug("Controller finished, now go to forward address --> " + address);
		// go to forward
		if (request.getAttribute("sendRedirect") != null && (Boolean) request.getAttribute("sendRedirect")) {
			response.sendRedirect(response.encodeRedirectURL(address));
		} else {
			request.getRequestDispatcher(address).forward(request, response);
		}
	}

}