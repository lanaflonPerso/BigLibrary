package ua.khai.slynko.library.web.command.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.khai.slynko.library.constant.Constants;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * No command.
 * 
 * @author O.Slynko
 * 
 */
public class NoCommand extends Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("errorMessage", "No such command");
		return Constants.Path.PAGE_ERROR_PAGE;
	}
}