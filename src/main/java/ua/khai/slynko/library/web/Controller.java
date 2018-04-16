package ua.khai.slynko.library.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.khai.slynko.library.constant.Constants;
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
		String commandName = request.getParameter("command");
		Command command = CommandContainer.get(commandName);
		String address = Constants.Path.PAGE_ERROR_PAGE;
		try {
			address = command.execute(request, response);
		} catch (AppException ex) {
			request.setAttribute("errorMessage", ex.getMessage());
		}
		if (request.getAttribute("sendRedirect") != null && (Boolean) request.getAttribute("sendRedirect")) {
			response.sendRedirect(response.encodeRedirectURL(address));
		} else {
			request.getRequestDispatcher(address).forward(request, response);
		}
	}
}