package org.neuclear.commons.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 * 
 * User: pelleb
 * Date: Aug 6, 2003
 * Time: 3:38:50 PM
 */
public class SimpleConnectionSource extends ThreadMappedConnectionSource {
    public SimpleConnectionSource(String driver, String url, String user, String password) throws SQLException{
        try {
             Class.forName(driver).newInstance();
         } catch (InstantiationException e) {
             e.printStackTrace();  //To change body of catch statement use Options | File Templates.
         } catch (IllegalAccessException e) {
             e.printStackTrace();  //To change body of catch statement use Options | File Templates.
         } catch (ClassNotFoundException e) {
             e.printStackTrace();  //To change body of catch statement use Options | File Templates.
         }
        this.url=url;
        this.user=user;
        this.password=password;
    }

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(
                         url,
                         user,
                         password
                    );
    }

    private String url;
    private String user;
    private String password;
    private Connection con;

}
