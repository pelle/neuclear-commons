package org.neuclear.commons.configuration;

import org.picocontainer.lifecycle.LifecyclePicoAdapter;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.extras.DefaultLifecyclePicoAdapter;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.neuclear.commons.Utility;
import org.neuclear.commons.sql.ConnectionSource;
import org.neuclear.commons.sql.DefaultConnectionSource;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;

/**
 *
 */
public class ServletContextContainer implements ServletContextListener {
    public final void contextInitialized(ServletContextEvent servletContextEvent) {
        final ServletContext ctx = servletContextEvent.getServletContext();
        final String configname = ctx.getInitParameter("configurator");
        Configuration conf=null;
        if (!Utility.isEmpty(configname)) {
            try {
                conf=(Configuration) Class.forName(configname).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();  //To change body of catch statement use Options | File Templates.
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use Options | File Templates.
            } catch (ClassNotFoundException e) {
                e.printStackTrace();  //To change body of catch statement use Options | File Templates.
            }
        }
        if (conf==null)
            conf=getDefaultConfiguration();
        pico=new ConfigurableContainer(conf);
        lifecycle=new DefaultLifecyclePicoAdapter(pico);
        lifecycle.start();

        ctx.setAttribute("pico",pico);
    }

    public final void contextDestroyed(ServletContextEvent servletContextEvent) {
        lifecycle.stop();
    }

    public Configuration getDefaultConfiguration(){
        return new DefaultConfiguration();
    }
    private LifecyclePicoAdapter lifecycle;
    private PicoContainer pico;


}
