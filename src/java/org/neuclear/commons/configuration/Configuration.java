package org.neuclear.commons.configuration;

import org.picocontainer.PicoContainer;

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
        return null;
    }

    private static PicoContainer pico;
    //private static final String CONFIG_FILE_NAME = "neuclear-conf.xml";

}
