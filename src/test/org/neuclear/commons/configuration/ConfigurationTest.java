package org.neuclear.commons.configuration;

import junit.framework.TestCase;
import org.picocontainer.PicoContainer;
import org.picocontainer.MutablePicoContainer;
import org.neuclear.commons.crypto.passphraseagents.PassPhraseAgent;

/**
 * User: pelleb
 * Date: Aug 18, 2003
 * Time: 11:15:41 AM
 */
public final class ConfigurationTest extends TestCase {
    public ConfigurationTest(final String string) {
        super(string);
    }


    public final void testConfigureAndGet()  {
        PicoContainer pico=new ConfigurableContainer(new Configuration(){
            public void configure(MutablePicoContainer pico) {
                pico.registerComponentImplementation(MockInterface.class,MockComponent.class);
                pico.registerComponentImplementation(MockDependency.class);
            }

        });
        assertNotNull(pico);
        MockInterface obj=(MockInterface) pico.getComponentInstance(MockInterface.class);
        assertNotNull(obj);
        assertTrue(obj instanceof MockComponent);
        assertTrue(obj.alwaysTrue());
    }
}
