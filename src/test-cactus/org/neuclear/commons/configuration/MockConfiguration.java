package org.neuclear.commons.configuration;

import org.picocontainer.MutablePicoContainer;

/**
 * Created by IntelliJ IDEA.
 * User: pelleb
 * Date: Jan 2, 2004
 * Time: 9:49:46 AM
 * To change this template use Options | File Templates.
 */
public class MockConfiguration implements Configuration{
    public void configure(MutablePicoContainer pico) {
        System.out.println("Loading MockCactus Configuration");
        pico.registerComponentImplementation(WidgetInterface.class,WidgetComponent.class);
    }
}
