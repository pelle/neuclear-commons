package org.neuclear.commons.sql;

import org.neuclear.commons.sql.ConnectionSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

/**
 * 
 * User: pelleb
 * Date: Aug 6, 2003
 * Time: 4:15:06 PM
 */
public abstract class ThreadMappedConnectionSource implements ConnectionSource {
    protected Map threadmap;

    public ThreadMappedConnectionSource() {
        threadmap = new HashMap();
    }

    /**
     * gets a thread mapped SQL Connection from the database driver
     * @return
     * @throws java.sql.SQLException
     */
    public Connection getConnection() throws SQLException, IOException {
        Connection con=(Connection)threadmap.get(Thread.currentThread());

        if (!(con!=null&&!con.isClosed())) {
            con= createConnection();
            threadmap.put(Thread.currentThread(),con);
        }
        return con;
    }

    public abstract Connection createConnection() throws SQLException, IOException;
}
