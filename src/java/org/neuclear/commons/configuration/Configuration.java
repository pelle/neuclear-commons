package org.neuclear.commons.configuration;

import org.picocontainer.PicoContainer;
import org.picocontainer.PicoRegistrationException;
import org.picocontainer.PicoInitializationException;
import org.nanocontainer.InputSourceRegistrationNanoContainer;
import org.nanocontainer.DomRegistrationNanoContainer;
import org.xml.sax.InputSource;

import javax.xml.parsers.ParserConfigurationException;

/**
 * 
 * User: pelleb
 * Date: Aug 13, 2003
 * Time: 11:34:51 AM
 */
public class Configuration {

    public static synchronized PicoContainer getContainer() throws ConfigurationException {
          if (pico==null)
            pico=buildContainer();
          return pico;
    }

    private static PicoContainer buildContainer()throws ConfigurationException{
        try {
            InputSourceRegistrationNanoContainer nc = new DomRegistrationNanoContainer.Default();
            nc.registerComponents(new InputSource(Configuration.class.getClassLoader().getResourceAsStream("neuclear-conf.xml")));
            nc.instantiateComponents();
            return nc;
        } catch (ParserConfigurationException e) {
            throw new ConfigurationException(e);
        } catch (PicoRegistrationException e) {
            throw new ConfigurationException(e);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException(e);
        } catch (PicoInitializationException e) {
            throw new ConfigurationException(e);
        }
    }
    private static PicoContainer pico;

}
