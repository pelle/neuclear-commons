/*
 * Created on Apr 11, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.neuclear.commons.crypto.signers.hibernate;

/**
 * Keeps a Cache of PrivateKeys.
 * The Alias name or username would retrieve the private key
 * At the moment used
 * -- username [50 chars long string]
 * -- private key [blob]
 * -- public key [blob] [since key length can be 1024 and in some databases can have a max length for varchar]
 * <p/>
 * TODO
 * -- cache for store
 * <p/>
 * Question ?
 * -- do we need to store certificates instead of public key
 * -- 2 blob columns should not be a limitation in any database
 * -- do we need to store the algorithm so that the db can contain a wrapped key using any algorithm
 */
public class SQLStore {
    private String alias; // username
    private byte[] privateKey; // private key in raw form
    // passphrase not stored so that no one can decrypt the private key
    private byte[] publicKey;

    public SQLStore() {
    }

    public SQLStore(String alias) {
        this.alias = alias;
    }

    public SQLStore(String alias, byte[] arrPvtKey, byte[] arrPubKey) {
        this.alias = alias;
        privateKey = arrPvtKey;
        publicKey = arrPubKey;
    }

    /**
     * @return
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param string
     */
    public void setAlias(String strAlias) {
        alias = strAlias;
    }

    /**
     * This is the getter method from the database
     *
     * @return
     */
    public byte[] getPrivateKey() {
        return privateKey;
    }

    /**
     * @param string
     */
    void setPrivateKey(byte[] arrPvtKey) {
        privateKey = arrPvtKey;
    }

    /**
     * @return
     */
    public byte[] getPublicKey() {
        return publicKey;
    }

    /**
     * @param string
     */
    private void setPublicKey(byte[] arrPubKey) {
        publicKey = arrPubKey;
    }

    public String toString() {
        return getAlias();
    }

}
