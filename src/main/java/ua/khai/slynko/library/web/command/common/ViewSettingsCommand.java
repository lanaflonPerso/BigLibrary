package ua.khai.slynko.library.web.command.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.khai.slynko.library.constant.Constants;
import ua.khai.slynko.library.web.abstractCommand.Command;

/**
 * View settings command.
 * 
 * @author O.Slynko
 * 
 */
public class ViewSettingsCommand extends Command {

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		return Constants.Path.PAGE_SETTINGS;
	}

}