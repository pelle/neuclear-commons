package org.neuclear.commons.sql;

import org.neuclear.commons.NeuClearException;

import javax.naming.NamingException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * User: pelleb
 * Date: Aug 6, 2003
 * Time: 3:38:50 PM
 */
public final class DefaultXAConnectionSource implements ConnectionSource {
    public DefaultXAConnectionSource() throws SQLException, NeuClearException, IOException, NamingException {
        final Properties props = SQLTools.loadProperties();
        cs = new XAConnectionSource(
                props.getProperty("jndi.name"),
                props.getProperty("jdbc.class"),
                props.getProperty("jdbc.url"),
                props.getProperty("jdbc.username"),
                props.getProperty("jdbc.password")
        );
    }

    public Connection getConnection() throws SQLException {
        return cs.getConnection();
    }

    private final ConnectionSource cs;

}
