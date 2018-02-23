package ua.khai.slynko.library.exception;

/**
 * An exception that provides information on a database access error.
 * 
 * @author O.Slynko
 * 
 */
public class DBException extends AppException {

	private static final long serialVersionUID = -3550446897536410392L;

	public DBException() {
		super();
	}

	public DBException(String message) {
		super(message);
	}

	public DBException(String message, Throwable cause) {
		super(message, cause);
	}

	public static boolean fire(String message) throws DBException {
		boolean flag = true;
		if (flag) {
			throw new DBException(message);
		}
		return true;
	}
}
