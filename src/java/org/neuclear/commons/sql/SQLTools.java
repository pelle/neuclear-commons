package org.neuclear.commons.sql;

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
       Properties props=loadProperties();
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


    private static Properties loadProperties() throws IOException {
        Properties props=new Properties();
        File propsFile= new File(
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

    public final static Timestamp toTimestamp(java.util.Date date) {
        if (date==null)
            return null;
        return new Timestamp(date.getTime());
    }
}
