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
        if (config != null)
            config.configure(pico);
    }

    public ComponentAdapter registerComponentImplementation(Object o, Class aClass) throws PicoRegistrationException {
        return pico.registerComponentImplementation(o, aClass);
    }

    public ComponentAdapter registerComponentImplementation(Object o, Class aClass, Parameter[] parameters) throws PicoRegistrationException {
        return pico.registerComponentImplementation(o, aClass, parameters);
    }

    public ComponentAdapter registerComponentImplementation(Class aClass) throws PicoRegistrationException {
        return pico.registerComponentImplementation(aClass);
    }

    public ComponentAdapter registerComponentInstance(Object o) throws PicoRegistrationException {
        return pico.registerComponentInstance(o);
    }

    public ComponentAdapter registerComponentInstance(Object o, Object o1) throws PicoRegistrationException {
        return pico.registerComponentInstance(o, o1);
    }

    public ComponentAdapter registerComponent(ComponentAdapter componentAdapter) throws PicoRegistrationException {
        return pico.registerComponent(componentAdapter);
    }

    public ComponentAdapter unregisterComponent(Object o) {
        return pico.unregisterComponent(o);
    }

    public ComponentAdapter unregisterComponentByInstance(Object o) {
        return pico.unregisterComponentByInstance(o);
    }

    public void setParent(PicoContainer picoContainer) {
        pico.setParent(picoContainer);
    }

    public Object getComponentInstance(Object o) {
        return pico.getComponentInstance(o);
    }

    public Object getComponentInstanceOfType(Class aClass) {
        return pico.getComponentInstanceOfType(aClass);
    }

    public List getComponentInstances() {
        return pico.getComponentInstances();
    }

    public PicoContainer getParent() {
        return pico.getParent();
    }

    public ComponentAdapter getComponentAdapter(Object o) {
        return pico.getComponentAdapter(o);
    }

    public ComponentAdapter getComponentAdapterOfType(Class aClass) {
        return pico.getComponentAdapterOfType(aClass);
    }

    public Collection getComponentAdapters() {
        return pico.getComponentAdapters();
    }

    public List getComponentAdaptersOfType(Class aClass) {
        return pico.getComponentAdaptersOfType(aClass);
    }

    public void verify() throws PicoVerificationException {
        pico.verify();
    }

    public void addOrderedComponentAdapter(ComponentAdapter componentAdapter) {
        pico.addOrderedComponentAdapter(componentAdapter);
    }

    public void start() {
        pico.start();
    }

    public void stop() {
        pico.stop();
    }

    public void dispose() {
        pico.dispose();
    }

    private final MutablePicoContainer pico;

}
