package com.example.university;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Context listener. Initializes i18n and CommandManager for future use.
 */
public class ContextListener implements ServletContextListener {

    private static final Logger LOG = LogManager.getLogger(ContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        LOG.trace("Servlet context initialization starts");
        ServletContext servletContext = event.getServletContext();
        initI18N(servletContext);
        initCommandManager();
        LOG.trace("Servlet context initialization finished");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        LOG.trace("Servlet context destruction.");
    }

    /**
     * Initializes i18n subsystem.
     *
     * @param servletContext - gets init parameters from descriptor.
     */
    private void initI18N(ServletContext servletContext) {
        LOG.debug("I18N subsystem initialization started");
        String localesValue = servletContext.getInitParameter("locales");
        if (localesValue == null || localesValue.isEmpty()) {
            LOG.warn("'locales' init parameter is empty, the default encoding will be used");
        } else {
            List<String> locales = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(localesValue);
            while (st.hasMoreTokens()) {
                String localeName = st.nextToken();
                locales.add(localeName);
            }
            LOG.debug("Application attribute set: 'locales' = {}", locales);
            servletContext.setAttribute("locales", locales);
        }
        LOG.debug("I18N subsystem initialization finished");
    }

    /**
     * Initializes CommandManager.
     */
    private void initCommandManager() {
        try {
            Class.forName("com.example.university.commands.CommandManager");
        } catch (ClassNotFoundException ex) {
            throw new IllegalStateException(
                    "Cannot initialize Command Manager", ex);
        }
    }

}