/*
$Id: SQLStoreAccess.java,v 1.1 2004/04/22 13:09:25 pelle Exp $
$Log: SQLStoreAccess.java,v $
Revision 1.1  2004/04/22 13:09:25  pelle
Added Deneb Shah's SQLSigner (deneb shah <deneb007@yahoo.com>)
Which stores private keys encrypted in a database and uses Hibernate.

*/

package org.neuclear.commons.crypto.signers.hibernate;


import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.SessionFactory;
import net.sf.hibernate.Transaction;
import net.sf.hibernate.cfg.Configuration;

import java.util.Iterator;
import java.util.List;

/**
 * @author deneb
 *         <p/>
 *         This Class acts as the gatekeeper for the SQLStore
 */
public class SQLStoreAccess {
    private SessionFactory factory;

    public SQLStoreAccess() {
        try {
            Configuration cfg = new Configuration().addClass(SQLStore.class);
            factory = cfg.buildSessionFactory();
        } catch (HibernateException he) {
            he.printStackTrace();
            //throw some exception
        }
    }


    /**
     * This method will
     * -- encrypt the private key with the passphrase
     * -- store the private key and the username
     */
    public void add(String username, byte[] privateKey, byte[] publicKey) {
        try {
            Session session = factory.openSession();
            Transaction txn = session.beginTransaction();
            SQLStore store = new SQLStore(username, privateKey, publicKey);
            session.save(store);
            txn.commit();
            session.close();

        } catch (HibernateException he) {
            he.printStackTrace();
            // TODO: handle exception
        }

    }

    /**
     * This method will
     * -- encrypt the private key with the passphrase
     * -- store the private key and the username
     */
    public void add(SQLStore store) {
        try {
            Session session = factory.openSession();
            Transaction txn = session.beginTransaction();
            //SQLStore store = new SQLStore(username, privateKey);
            session.save(store);
            txn.commit();
            session.close();

        } catch (HibernateException he) {
            he.printStackTrace();
            // TODO: handle exception
        }

    }

    /**
     * This method will
     * -- encrypt the private key with the passphrase
     * -- store the private key and the username
     */
    public void update(String username, String privateKey, String passphrase) {
        try {
            Session session = factory.openSession();
            Transaction txn = session.beginTransaction();
            //SQLStore store = new SQLStore(username, privateKey);
            //session.save(store);
            txn.commit();
            session.close();

        } catch (HibernateException he) {
            he.printStackTrace();
            // TODO: handle exception
        }

    }

    /**
     * This method will get the Encrypted Private Key from SQLStore based on the username
     * <p/>
     * -- encrypt the private key with the passphrase
     * -- store the private key and the username
     */
    public byte[] getEncryptedPrivateKey(String username) {

        try {
            SQLStore store = verifyAlias(username);
            byte[] arrPvtKey = store.getPrivateKey();
            if (arrPvtKey == null)
                throw new HibernateException("Private Key aint there");

            return arrPvtKey;

        } catch (HibernateException he) {
            he.printStackTrace();
            return null;
            // TODO: handle exception
        }

    }


    public SQLStore verifyAlias(String username) {
        try {
            Session session = factory.openSession();
            List t1List = session.createQuery("select new SQLStore(t.name,t.privatekey, t.publickey)"
                    + " from SQLStore t"
                    + " where t.name=" + username)
                    .setMaxResults(100)
                    .list();

            Iterator iter = t1List.iterator();
            SQLStore store = null;

            while (iter.hasNext()) {
                store = (SQLStore) iter.next();

            }
            return store;
        } catch (HibernateException he) {
            he.printStackTrace();
            return null;
        }
    }

    public Iterator getAllUserName() {
        try {
            Session session = factory.openSession();
            List t1List = session.createQuery("select name"
                    + " from SQLStore")
                    .list();

            return t1List.iterator();
        } catch (HibernateException he) {
            he.printStackTrace();
            return null;
        }
    }

    public static void log(String s) {
        System.out.println(s);
    }


}
