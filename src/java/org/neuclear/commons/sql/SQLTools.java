package org.neuclear.commons.sql;

import org.neuclear.commons.NeuClearException;
import org.objectweb.jotm.Jotm;
import org.objectweb.carol.util.configuration.CarolConfiguration;
import org.objectweb.carol.util.configuration.RMIConfigurationException;

import javax.transaction.UserTransaction;
import javax.transaction.TransactionManager;
import javax.transaction.SystemException;
import javax.naming.InitialContext;
import javax.naming.Context;
import javax.naming.NamingException;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.util.Properties;
import java.util.Date;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: pelleb
 * Date: Jul 16, 2003
 * Time: 1:47:21 PM
 * To change this template use Options | File Templates.
 */
public final class SQLTools {
    private static final String PROPS_FILE = "neuclear-ledger.properties";

    public final static java.sql.Connection getConnection() throws SQLException, IOException {
       final Properties props=loadProperties();
       try {
            Class.forName(props.getProperty("jdbc.class")).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
        }
        return DriverManager.getConnection(
                props.getProperty("jdbc.url"),
                props.getProperty("jdbc.username"),
                props.getProperty("jdbc.password")
        );

    }


    static Properties loadProperties() throws IOException {
        final Properties props=new Properties();
        final File propsFile= new File(
                System.getProperty("user.home")+
                System.getProperty("file.separator")+
                PROPS_FILE) ;
        if (!propsFile.exists()) {
            System.out.println("getting props from system classpath");
            props.load(ClassLoader.getSystemResourceAsStream(PROPS_FILE));
        } else {
            System.out.println("getting props from home directory");
            props.load(new FileInputStream(propsFile));
        }
        return props;
    }

    public final static Timestamp toTimestamp(final java.util.Date date) {
        if (date==null)
            return null;
        return new Timestamp(date.getTime());
    }
    public final static UserTransaction getUserTransaction() throws NamingException,SystemException {
            Context ctx = new InitialContext();
        UserTransaction ut = null;
        try {
            ut = (UserTransaction)ctx.lookup(USERXACT);
        } catch (NamingException e) {
            getTransactionManager().getTransaction();
            return jotm.getUserTransaction();

        }
        return ut;

    }

    public final static synchronized TransactionManager getTransactionManager() throws NamingException {
        if (jotm==null){
            Context ctx = new InitialContext();
            jotm=new Jotm(true,false);
            ctx.rebind(USERXACT,jotm.getUserTransaction());
        }
        return jotm.getTransactionManager();

    }
    final static void loadDefaultContext(){
        try{
            System.setProperty("java.naming.factory.initial","org.objectweb.carol.jndi.spi.MultiOrbInitialContextFactory");
            Context ctx=new InitialContext();
        } catch (Exception ex){

            try {
                CarolConfiguration.init();
                getTransactionManager();
            } catch (RMIConfigurationException e) {
                throw new RuntimeException(e);
            } catch (NamingException e) {
                throw new RuntimeException(e) ;

            }
        }
    }


    private static Jotm jotm;
    public static final String USERXACT = "java:comp/UserTransaction";

}
