package ua.khai.slynko.Library.web.listener;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import ua.khai.slynko.Library.exception.Messages;

/**
 * Context listener.
 * 
 * @author O.Slynko
 * 
 */
public class ContextListener implements ServletContextListener {

	private static final Logger LOG = Logger.getLogger(ContextListener.class);

	public void contextDestroyed(ServletContextEvent event) {
		log("Servlet context destruction starts");

		log("Servlet context destruction finished");
	}

	public void contextInitialized(ServletContextEvent event) {
		log("Servlet context initialization starts");

		ServletContext servletContext = event.getServletContext();
		initLog4J(servletContext);
		initCommandContainer();
		
		ServletContext context = event.getServletContext();
		String localesFileName = context.getInitParameter("locales");

		// obtain real path on server
		String localesFileRealPath = context.getRealPath(localesFileName);

		// locale descriptions
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
					LOG.error(Messages.ERR_CANNOT_CLOSE_FIS, ex);
				}
			}
		}

		// save descriptions to servlet context
		context.setAttribute("locales", locales);
		locales.list(System.out);
		Locale.setDefault(new Locale("en_GB"));
		log("Servlet context initialization finished");
	}

	/**
	 * Initializes log4j framework.
	 * 
	 * @param servletContext
	 */
	private void initLog4J(ServletContext servletContext) {
		log("Log4J initialization started");
		try {
			PropertyConfigurator.configure(servletContext.getRealPath("WEB-INF/log4j.properties"));
			LOG.debug("Log4j has been initialized");
		} catch (Exception ex) {
			log("Cannot configure Log4j");
			LOG.error(ex.getMessage());
		}
		log("Log4J initialization finished");
	}

	/**
	 * Initializes CommandContainer.
	 * 
	 * @param servletContext
	 */
	private void initCommandContainer() {

		// initialize commands container
		// just load class to JVM
		try {
			Class.forName("ua.khai.slynko.Library.web.command.CommandContainer");
		} catch (ClassNotFoundException ex) {
			throw new IllegalStateException("Cannot initialize Command Container");
		}
	}

	private void log(String msg) {
		System.out.println("[ContextListener] " + msg);
	}
}