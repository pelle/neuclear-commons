package org.neuclear.commons.sql;

import org.enhydra.jdbc.standard.StandardXADataSource;
import org.neuclear.commons.NeuClearException;

import javax.sql.XADataSource;
import javax.sql.XAConnection;
import javax.naming.NamingException;
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
public final class XAConnectionSource implements ConnectionSource {
    public XAConnectionSource(final String driver, final String url, final String user, final String password) throws SQLException, NeuClearException, NamingException{
        SQLTools.loadDefaultContext();
        try {
             Class.forName(driver).newInstance();
         } catch (InstantiationException e) {
             e.printStackTrace();  //To change body of catch statement use Options | File Templates.
         } catch (IllegalAccessException e) {
             e.printStackTrace();  //To change body of catch statement use Options | File Templates.
         } catch (ClassNotFoundException e) {
             e.printStackTrace();  //To change body of catch statement use Options | File Templates.
         }
        this.user=user;
        this.password=password;
        xads = new StandardXADataSource();
        ((StandardXADataSource) xads).setDriverName(driver);
        ((StandardXADataSource) xads).setUrl(url);
        ((StandardXADataSource) xads).setTransactionManager(SQLTools.getTransactionManager());

    }


    public Connection getConnection() throws SQLException, IOException {
        return xads.getXAConnection(user,password).getConnection();
    }

    private final String user;
    private final String password;
    private final XADataSource xads;
//    private XAConnection xaConnection;


}
