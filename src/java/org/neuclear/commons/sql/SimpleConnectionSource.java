package org.neuclear.commons.sql;

import org.enhydra.jdbc.standard.StandardXADataSource;
import org.neuclear.commons.NeuClearException;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.sql.XADataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;

/**
 * User: pelleb
 * Date: Aug 6, 2003
 * Time: 3:38:50 PM
 */
public final class SimpleConnectionSource implements ConnectionSource {
    public SimpleConnectionSource(final String driver, final String url, final String user, final String password) throws SQLException, NeuClearException, NamingException {
        try {
            Class.forName(driver).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        this.username=user;
        this.url=url;
        this.password=password;
    }


    public Connection getConnection() throws SQLException, IOException {
        return DriverManager.getConnection(
                url,
                username,
                password
        );
    }

    private final String url;
    private final String username;
    private final String password;

}
