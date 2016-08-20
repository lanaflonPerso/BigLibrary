package ua.khai.slynko.Library.web.command;

import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import ua.khai.slynko.Library.web.abstractCommand.Command;
import ua.khai.slynko.Library.web.command.admin.AddBookCommand;
import ua.khai.slynko.Library.web.command.admin.AddLibrarianCommand;
import ua.khai.slynko.Library.web.command.admin.BlockUnblockUserCommand;
import ua.khai.slynko.Library.web.command.admin.ListAdminCatalogCommand;
import ua.khai.slynko.Library.web.command.admin.ListLibrariansCommand;
import ua.khai.slynko.Library.web.command.admin.ListReadersCommand;
import ua.khai.slynko.Library.web.command.admin.UpdateBookCommand;
import ua.khai.slynko.Library.web.command.common.LogoutCommand;
import ua.khai.slynko.Library.web.command.common.NoCommand;
import ua.khai.slynko.Library.web.command.common.UpdateProfileCommand;
import ua.khai.slynko.Library.web.command.common.ViewSettingsCommand;
import ua.khai.slynko.Library.web.command.librarian.ConfirmRequestCommand;
import ua.khai.slynko.Library.web.command.librarian.ListReadersRequestsCommand;
import ua.khai.slynko.Library.web.command.outOfControl.ForgotPasswordCommand;
import ua.khai.slynko.Library.web.command.outOfControl.ListCatalogCommand;
import ua.khai.slynko.Library.web.command.outOfControl.LoginCommand;
import ua.khai.slynko.Library.web.command.outOfControl.SignUpCommand;
import ua.khai.slynko.Library.web.command.reader.ListPersonalAreaCommand;

/**
 * Holder for all commands.<br/>
 * 
 * @author O.Slynko
 * 
 */
public class CommandContainer {

	private static final Logger LOG = Logger.getLogger(CommandContainer.class);

	private static Map<String, Command> commands = new TreeMap<String, Command>();

	static {
		// common commands
		commands.put("login", new LoginCommand());
		commands.put("signUp", new SignUpCommand());
		commands.put("forgotPassword", new ForgotPasswordCommand());
		commands.put("logout", new LogoutCommand());
		commands.put("updateProfile", new UpdateProfileCommand());
		commands.put("viewSettings", new ViewSettingsCommand());
		commands.put("noCommand", new NoCommand());

		// reader commands
		commands.put("listCatalog", new ListCatalogCommand());
		commands.put("listPersonalArea", new ListPersonalAreaCommand());

		// admin commands
		commands.put("listAdminCatalog", new ListAdminCatalogCommand());
		commands.put("listLibrarians", new ListLibrariansCommand());
		commands.put("updateBook", new UpdateBookCommand());
		commands.put("addBook", new AddBookCommand());
		commands.put("listReaders", new ListReadersCommand());
		commands.put("blockUnblockUser", new BlockUnblockUserCommand());
		commands.put("addLibrarian", new AddLibrarianCommand());

		// librarian commands
		commands.put("listReadersRequests", new ListReadersRequestsCommand());
		commands.put("confirmRequestCommand", new ConfirmRequestCommand());

		LOG.debug("Command container was successfully initialized");
		LOG.trace("Number of commands --> " + commands.size());
	}

	/**
	 * Returns command object with the given name.
	 * 
	 * @param commandName
	 *            Name of the command.
	 * @return Command object.
	 */
	public static Command get(String commandName) {
		if (commandName == null || !commands.containsKey(commandName)) {
			LOG.trace("Command not found, name --> " + commandName);
			return commands.get("noCommand");
		}

		return commands.get(commandName);
	}

}