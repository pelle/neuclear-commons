package org.neuclear.commons.configuration;

import org.picocontainer.PicoContainer;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

/**
 * Created by IntelliJ IDEA.
 * User: pelleb
 * Date: Jan 2, 2004
 * Time: 8:20:14 AM
 * To change this template use Options | File Templates.
 */
public class MockConfiguredServlet extends HttpServlet {
    public void init(ServletConfig servletConfig) throws ServletException {

        pico=(PicoContainer) servletConfig.getServletContext().getAttribute("pico");
    }

    public PicoContainer getPico() {
        return pico;
    }

    private PicoContainer pico;
}
