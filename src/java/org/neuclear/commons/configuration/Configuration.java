package org.neuclear.commons.configuration;

import org.nanocontainer.DomRegistrationNanoContainer;
import org.nanocontainer.InputSourceRegistrationNanoContainer;
import org.picocontainer.PicoContainer;
import org.picocontainer.PicoInitializationException;
import org.picocontainer.PicoRegistrationException;
import org.xml.sax.InputSource;

import javax.xml.parsers.ParserConfigurationException;

/**
 * User: pelleb
 * Date: Aug 13, 2003
 * Time: 11:34:51 AM
 */
public class Configuration {

    public static synchronized PicoContainer getContainer(Class context) throws ConfigurationException {
        if (pico == null)
            pico = buildContainer(context);
        return pico;
    }

    private static PicoContainer buildContainer(Class context) throws ConfigurationException {
        try {
            InputSourceRegistrationNanoContainer nc = new DomRegistrationNanoContainer.Default();
            nc.registerComponents(new InputSource(context.getResourceAsStream("neuclear-conf.xml")));
            return nc;
        } catch (ParserConfigurationException e) {
            throw new ConfigurationException(e);
        } catch (PicoRegistrationException e) {
            throw new ConfigurationException(e);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ConfigurationException(e);
        } catch (PicoInitializationException e) {
            throw new ConfigurationException(e);
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
            throw new ConfigurationException(e);
        }
    }

    private static PicoContainer pico;

}
