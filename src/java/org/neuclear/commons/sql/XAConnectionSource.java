package org.neuclear.commons.sql;

import org.enhydra.jdbc.standard.StandardXADataSource;
import org.neuclear.commons.NeuClearException;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import javax.sql.XADataSource;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * User: pelleb
 * Date: Aug 6, 2003
 * Time: 3:38:50 PM
 */
public class XAConnectionSource implements ConnectionSource {
    public XAConnectionSource(final String name, final String driver, final String url, final String user, final String password) throws SQLException, NeuClearException, NamingException {

        Context ctx = SQLTools.loadDefaultContext();
        SQLTools.getTransactionManager();
        try {
            DataSource ds = (DataSource) ctx.lookup("java:comp/env/" + name);
            if (ds!=null){
                xads=(XADataSource) ds;
                return;
            }
        } catch (Exception e) {
            ;//ignore and create new datasource
        }
        try {
            Class.forName(driver).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        xads = new StandardXADataSource();
        ((StandardXADataSource) xads).setDriverName(driver);
        ((StandardXADataSource) xads).setUrl(url);
        ((StandardXADataSource) xads).setTransactionManager(SQLTools.getTransactionManager());
        ((StandardXADataSource) xads).setUser(user);
        ((StandardXADataSource) xads).setPassword(password);


        ctx.rebind(name, xads);

    }


    public Connection getConnection() throws SQLException {
        return xads.getXAConnection().getConnection();
    }

    private XADataSource xads;
//    private XAConnection xaConnection;


}
