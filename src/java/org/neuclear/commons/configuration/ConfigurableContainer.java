package org.neuclear.commons.configuration;

import org.picocontainer.*;
import org.picocontainer.defaults.DefaultPicoContainer;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: pelleb
 * Date: Dec 31, 2003
 * Time: 11:19:52 AM
 * To change this template use Options | File Templates.
 */
public class ConfigurableContainer implements PicoContainer {
    public ConfigurableContainer(Configuration config) {
        this.pico = new DefaultPicoContainer();
        if (config!=null)
            config.configure(pico);
    }

    private final MutablePicoContainer pico;

    public Object getComponentInstance(Object object) throws PicoException {
        return pico.getComponentInstance(object);
    }

    public List getComponentInstances() throws PicoException {
        return pico.getComponentInstances();
    }

    public boolean hasComponent(Object object) {
        return pico.hasComponent(object);
    }

    public Object getComponentMulticaster(boolean b, boolean b1) throws PicoException {
        return pico.getComponentMulticaster(b,b1);
    }

    public Object getComponentMulticaster() throws PicoException {
        return pico.getComponentMulticaster();
    }

    public Collection getComponentKeys() {
        return pico.getComponentKeys();
    }

    public Collection getChildContainers() {
        return pico.getChildContainers();
    }

    public List getParentContainers() {
        return pico.getParentContainers();
    }

    public ComponentAdapter findComponentAdapter(Object object) throws PicoIntrospectionException {
        return pico.findComponentAdapter(object);
    }
}
