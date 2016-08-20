package ua.khai.slynko.Library.web.command.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.khai.slynko.Library.Path;
import ua.khai.slynko.Library.web.abstractCommand.Command;

/**
 * View settings command.
 * 
 * @author O.Slynko
 * 
 */
public class ViewSettingsCommand extends Command {

	private static final long serialVersionUID = -3071536593627692473L;

	private static final Logger LOG = Logger.getLogger(ViewSettingsCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		LOG.debug("Command starts");
		LOG.debug("Command finished");
		return Path.PAGE_SETTINGS;
	}

}