/*
 * Created on Apr 11, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
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

    /**
     * if the user's details are stored in the store then a store object will be returned
     * else null
     *
     * @param username
     * @return
     */
    public SQLStore verifyAlias(String username) {
        try {
            Session session = factory.openSession();
            List t1List = session.createQuery("select new SQLStore(t.alias,t.PrivateKey, t.PublicKey)"
                    + " from SQLStore t"
                    + " where t.alias like '" + username + "'")
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

    /**
     * gets the iterator list of all the usernames
     *
     * @return
     */
    public Iterator getAllUserName() {
        try {
            Session session = factory.openSession();
            List t1List = session.createQuery("select t.alias"
                    + " from SQLStore t")
                    .setMaxResults(100)
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

    public static void main(String[] arr) {
        SQLStoreAccess access = new SQLStoreAccess();
        //SQLStore store = access.verifyAlias("ccc");
        //log(" -- > " + store);
        Iterator iter = access.getAllUserName();
        log(" --> " + iter);
        //access.add("eee", getbytes(), getbytes());

    }

    private static byte[] getbytes() {
        byte[] arr = new byte[10];
        byte j = Byte.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (byte) j;
            j++;
        }
        return arr;
    }


}
