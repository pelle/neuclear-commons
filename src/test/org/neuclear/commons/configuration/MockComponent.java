package org.neuclear.commons.configuration;

/**
 * Created by IntelliJ IDEA.
 * User: pelleb
 * Date: Dec 31, 2003
 * Time: 11:31:56 AM
 * To change this template use Options | File Templates.
 */
public class MockComponent implements MockInterface {
    public MockComponent(MockDependency dep) {
        this.dep = dep;
    }

    public boolean alwaysTrue() {
        return true;
    }
    private final MockDependency dep;
}
