package ua.khai.slynko.library.web.listener;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import ua.khai.slynko.library.constant.Constants;

/**
 * Context listener.
 * 
 * @author O.Slynko
 * 
 */
public class ContextListener implements ServletContextListener {

	private static final Logger LOG = Logger.getLogger(ContextListener.class);

	public void contextDestroyed(ServletContextEvent event) {
		// do nothing
	}

	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		initLog4J(servletContext);
		initCommandContainer();
		
		ServletContext context = event.getServletContext();
		String localesFileName = context.getInitParameter("locales");

		String localesFileRealPath = context.getRealPath(localesFileName);

		Properties locales = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(localesFileRealPath);
			locales.load(fis);
		} catch (IOException e) {
			LOG.error(e.getMessage());
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException ex) {
					LOG.error(Constants.Messages.ERR_CANNOT_CLOSE_FIS, ex);
				}
			}
		}

		context.setAttribute("locales", locales);
		locales.list(System.out);
		Locale.setDefault(new Locale("en_GB"));
	}

	/**
	 * Initializes log4j framework.
	 * 
	 * @param servletContext
	 */
	private void initLog4J(ServletContext servletContext) {
		try {
			PropertyConfigurator.configure(servletContext.getRealPath("WEB-INF/log4j.properties"));
			LOG.debug("Log4j has been initialized");
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
		}
	}

	/**
	 * Initializes CommandContainer.
	 * 
	 * @param servletContext
	 */
	private void initCommandContainer() {
		try {
			Class.forName("ua.khai.slynko.library.web.command.CommandContainer");
		} catch (ClassNotFoundException ex) {
			throw new IllegalStateException("Cannot initialize Command Container");
		}
	}
}