package org.neuclear.commons.configuration;

import org.picocontainer.MutablePicoContainer;

/**
 * Created by IntelliJ IDEA.
 * User: pelleb
 * Date: Dec 31, 2003
 * Time: 10:10:32 AM
 * To change this template use Options | File Templates.
 */
public interface Configuration {
    public void configure(MutablePicoContainer pico) ;
}
