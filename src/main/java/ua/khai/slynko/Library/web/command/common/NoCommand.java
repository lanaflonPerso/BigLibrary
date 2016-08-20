package ua.khai.slynko.Library.web.command.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.khai.slynko.Library.Path;
import ua.khai.slynko.Library.web.abstractCommand.Command;

/**
 * No command.
 * 
 * @author O.Slynko
 * 
 */
public class NoCommand extends Command {

	private static final long serialVersionUID = -2785976616686657267L;

	private static final Logger LOG = Logger.getLogger(NoCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		LOG.debug("Command starts");

		String errorMessage = "No such command";
		request.setAttribute("errorMessage", errorMessage);
		LOG.error("Set the request attribute: errorMessage --> " + errorMessage);

		LOG.debug("Command finished");
		return Path.PAGE_ERROR_PAGE;
	}

}