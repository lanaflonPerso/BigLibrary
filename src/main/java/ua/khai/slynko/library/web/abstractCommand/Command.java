package ua.khai.slynko.library.web.abstractCommand;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ua.khai.slynko.library.exception.AppException;

/**
 * Main interface for the Command pattern implementation.
 * 
 * @author O.Slynko
 * 
 */
public abstract class Command implements Serializable {
	/**
	 * Execution method for command.
	 * 
	 * @return Address to go once the command is executed.
	 */
	public abstract String execute(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, AppException;

	@Override
	public final String toString() {
		return getClass().getSimpleName();
	}
}