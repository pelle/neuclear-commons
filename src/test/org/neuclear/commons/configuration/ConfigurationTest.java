package org.neuclear.commons.configuration;

import junit.framework.TestCase;
import org.neuclear.commons.sql.ConnectionSource;

/**
 * User: pelleb
 * Date: Aug 18, 2003
 * Time: 11:15:41 AM
 */
public final class ConfigurationTest extends TestCase {
    public ConfigurationTest(final String string) {
        super(string);
    }

    public final void testGetComponent() throws ConfigurationException {
        assertNotNull(Configuration.getComponent(ConnectionSource.class, "neuclear-commons"));
    }
}
