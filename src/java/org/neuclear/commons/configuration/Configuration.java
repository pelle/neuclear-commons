package org.neuclear.commons.configuration;

import org.nanocontainer.DomRegistrationNanoContainer;
import org.nanocontainer.InputSourceRegistrationNanoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoInitializationException;
import org.picocontainer.PicoRegistrationException;
import org.xml.sax.InputSource;

import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStream;

/**
 * User: pelleb
 * Date: Aug 13, 2003
 * Time: 11:34:51 AM
 */
public final class Configuration {

    private static synchronized PicoContainer getContainer(final String context) throws ConfigurationException {
        if (pico == null)
            pico = buildContainer(context);
        return pico;
    }

    public static Object getComponent(final Object type, final String context) throws ConfigurationException {
        return getContainer(context).getComponent(type);
    }

    private static PicoContainer buildContainer(final String context) throws ConfigurationException {
        try {
            final InputSourceRegistrationNanoContainer nc = new DomRegistrationNanoContainer.Default();
            final InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(context + ".xml");
            nc.registerComponents(new InputSource(in));
            nc.instantiateComponents();
            return nc;
        } catch (ParserConfigurationException e) {
            throw new ConfigurationException(e);
        } catch (PicoRegistrationException e) {
            throw new ConfigurationException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ConfigurationException(e);
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
            throw new ConfigurationException(e);
        } catch (PicoInitializationException e) {
            e.printStackTrace();
            throw new ConfigurationException(e);
        }
    }

    private static PicoContainer pico;
    //private static final String CONFIG_FILE_NAME = "neuclear-conf.xml";

}
