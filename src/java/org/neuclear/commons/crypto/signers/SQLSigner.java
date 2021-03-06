/*
 *  The NeuClear Project and it's libraries are
 *  (c) 2002-2004 Antilles Software Ventures SA
 *  For more information see: http://neuclear.org
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
/*
$Id: SQLSigner.java,v 1.3 2004/07/21 23:07:16 pelle Exp $
$Log: SQLSigner.java,v $
Revision 1.3  2004/07/21 23:07:16  pelle
Updated the Signer interface with a new generateKey() method, which doesn't take any parameters.
It stores the generated key using the Base32 encoded SHA1 digest as it's alias.

Revision 1.2  2004/05/13 23:55:32  pelle
Updated Deneb Shahs SQLSigner
Also added his unit tests

Revision 1.1  2004/04/22 13:09:25  pelle
Added Deneb Shah's SQLSigner (deneb shah <deneb007@yahoo.com>)
Which stores private keys encrypted in a database and uses Hibernate.
*/
package org.neuclear.commons.crypto.signers;

import org.neuclear.commons.LowLevelException;
import org.neuclear.commons.crypto.CryptoException;
import org.neuclear.commons.crypto.CryptoTools;
import org.neuclear.commons.crypto.passphraseagents.AlwaysTheSamePassphraseAgent;
import org.neuclear.commons.crypto.passphraseagents.InteractiveAgent;
import org.neuclear.commons.crypto.passphraseagents.PassPhraseAgent;
import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;
import org.neuclear.commons.crypto.signers.hibernate.SQLStore;
import org.neuclear.commons.crypto.signers.hibernate.SQLStoreAccess;

import java.security.*;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Iterator;

/**
 * @author DESHAH
 *         <p/>
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SQLSigner implements BrowsableSigner {
    SQLStoreAccess access;
    PassPhraseAgent agent;

    /* (non-Javadoc)
     * @see org.neuclear.commons.crypto.signers.BrowsableSigner#iterator()
     *
     * this will
     * -- encrypt the private key with the CryptoTool
     * -- create a SQLStore to store it
     * -- use SQLAccess to store the key
     */
    public SQLSigner(String passphrase) {
        access = new SQLStoreAccess();
        agent = new AlwaysTheSamePassphraseAgent(passphrase);
    }

    public SQLSigner() {
        this("neuclear");
    }

    /*
     * This method should return an iterator list of all username
     *
     * Question ?
     * -- why Iterator List ? why not any other dataStructure like String[]
     *
     * @see org.neuclear.commons.crypto.signers.BrowsableSigner#iterator()
     */
    public Iterator iterator() throws KeyStoreException {
        return access.getAllUserName();
    }

    /* (non-Javadoc)
     * @see org.neuclear.commons.crypto.signers.BrowsableSigner#sign(byte[], org.neuclear.commons.crypto.signers.SetPublicKeyCallBack)
     *
     * dont know what to do with this method yet
     */
    public byte[] sign(byte[] data, SetPublicKeyCallBack callback)
            throws UserCancellationException {
        return ((InteractiveAgent) agent).sign(this, data, callback);
    }

    /* (non-Javadoc)
     * @see org.neuclear.commons.crypto.signers.BrowsableSigner#sign(java.lang.String, char[], byte[], org.neuclear.commons.crypto.signers.SetPublicKeyCallBack)
     *
     * This should sign the data with the privateKey
     * and return the data
     * -- get key from SQLStore using SQLStoreAccess for a given alias
     * -- sign and return the data using CryptoTool
     *
     */
    public byte[] sign(String alias,
                       char[] passphrase,
                       byte[] data,
                       SetPublicKeyCallBack callback)
            throws InvalidPassphraseException {
        try {
            PrivateKey prvKey = getKey(alias, passphrase);
            byte[] bytes = CryptoTools.sign(prvKey, data);
            if (callback != null)
                callback.setPublicKey(getPublicKey(alias));
            return bytes;
        } catch (UnrecoverableKeyException e) {
            throw new InvalidPassphraseException(alias);
        } catch (NoSuchAlgorithmException e) {
            throw new LowLevelException(e);
        } catch (KeyStoreException e) {
            // Could try to reload it here but I wont for now
            throw new LowLevelException(e);
        } catch (CryptoException e) {
            throw new LowLevelException(e);
        }

    }

    /**
     * This method will get the private key from the SQLStore
     * -- Use SQLStore to get the private key
     * -- unwrap the private key using CryptoTool with the passphrase
     * -- return the decrypted privateKey
     *
     * @param name
     * @param passphrase
     * @return
     * @throws UnrecoverableKeyException
     * @throws NonExistingSignerException
     * @throws NoSuchAlgorithmException
     * @throws KeyStoreException
     */
    private PrivateKey getKey(final String name, final char[] passphrase) throws UnrecoverableKeyException, NonExistingSignerException, NoSuchAlgorithmException, KeyStoreException {
        try {
            SQLStoreAccess access = new SQLStoreAccess();
            final byte[] arrWrappedPvtKey = access.getEncryptedPrivateKey(name);

            if (arrWrappedPvtKey == null)
                throw new NonExistingSignerException("No keys for: " + name);

            final PrivateKey key = CryptoTools.unWrapRSAKey(passphrase, arrWrappedPvtKey);

            if (key == null)
                throw new NonExistingSignerException("No keys for: " + name);

            return key;
        } catch (ClassCastException e) {
            throw new NonExistingSignerException("Incorrect Key type found");
        } catch (CryptoException e) {
            throw new NonExistingSignerException("Incorrect Key type found");
        }


    }

    /*
     * This method will generate a keyp pair and store it in the DB
     * -- use Crypto Tools to generate a keypair
     * -- get public key
     * -- get Private key
     * -- store it in the table using SQLStore
     *
     * (non-Javadoc)
     * @see org.neuclear.commons.crypto.signers.BrowsableSigner#createKeyPair(java.lang.String, char[])
     */
    public void createKeyPair(String alias, char[] passphrase)
            throws CryptoException {
        try {

            // for now it is RSA, later make it generic
            KeyPair kp = CryptoTools.createTinyRSAKeyPair();
            PrivateKey pvtKey = kp.getPrivate();
            PublicKey pubKey = kp.getPublic();

            byte[] encodedPublicKey = pubKey.getEncoded();
            byte[] wrappedPrivateKey = CryptoTools.wrapKey(passphrase, pvtKey);

            SQLStore store = new SQLStore(alias, wrappedPrivateKey, encodedPublicKey);

            SQLStoreAccess access = new SQLStoreAccess();
            access.add(store);

        } catch (Exception e) {
            throw new CryptoException("Aint Create Key -- under age");
        }

    }

    /* (non-Javadoc)
     * @see org.neuclear.commons.crypto.signers.Signer#sign(java.lang.String, byte[])
     */
    public byte[] sign(String name, byte[] data)
            throws UserCancellationException, NonExistingSignerException {
        return sign(name, data, false);
    }

    public final byte[] sign(final String name, final byte[] data, boolean incorrect) throws UserCancellationException, NonExistingSignerException {
        try {
            final char[] pass = getPassPhrase(name, incorrect);
            return CryptoTools.sign(getKey(name, pass), data);
        } catch (UnrecoverableKeyException e) {
            System.err.println("Incorrect Passphrase Attemt on: " + name);
            return sign(name, data, true);
        } catch (NoSuchAlgorithmException e) {
            throw new LowLevelException(e);
        } catch (KeyStoreException e) {
            // Could try to reload it here but I wont for now
            throw new LowLevelException(e);
        } catch (CryptoException e) {
            throw new LowLevelException(e);
        }
    }

    private char[] getPassPhrase(final String name, boolean incorrect) throws UserCancellationException {
        if (incorrect && (agent instanceof InteractiveAgent))
            ((InteractiveAgent) agent).getPassPhrase(name, true);
        return agent.getPassPhrase(name);
    }

    /*
     * This method will return true if the username is present in the db
     *
     * (non-Javadoc)
     * @see org.neuclear.commons.crypto.signers.Signer#canSignFor(java.lang.String)
     */
    public boolean canSignFor(String name) {
        SQLStoreAccess access = new SQLStoreAccess();
        SQLStore store = access.verifyAlias(name);
        if (store == null) {
            return false;
        }

        return true;
    }

    /* (non-Javadoc)
     * @see org.neuclear.commons.crypto.signers.Signer#getKeyType(java.lang.String)
     */
    public int getKeyType(String name) {

        try {
            if (canSignFor(name)) {
                final PublicKey pk = getPublicKey(name);
                if (pk instanceof RSAPublicKey)
                    return KEY_RSA;
                if (pk instanceof DSAPublicKey)
                    return KEY_DSA;
                return KEY_OTHER;
            }
        } catch (NonExistingSignerException e) {
            return KEY_NONE;
        }

        return KEY_NONE;
    }

    /* (non-Javadoc)
     * @see org.neuclear.commons.crypto.signers.Signer#generateKey(java.lang.String)
     */
    public PublicKey generateKey(String alias) throws UserCancellationException {
        try {
            //	for now it is RSA, later make it generic
            KeyPair kp = CryptoTools.createTinyRSAKeyPair();
            PrivateKey pvtKey = kp.getPrivate();
            PublicKey pubKey = kp.getPublic();

            byte[] encodedPublicKey = pubKey.getEncoded();
            byte[] wrappedPrivateKey = CryptoTools.wrapKey(agent.getPassPhrase(alias), pvtKey);

            SQLStore store = new SQLStore(alias, wrappedPrivateKey, encodedPublicKey);

            SQLStoreAccess access = new SQLStoreAccess();
            access.add(store);

            return pubKey;
        } catch (CryptoException e) {
            throw new UserCancellationException("[SQLSigner] [generateKey] -- " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new UserCancellationException("[SQLSigner] [generateKey] -- " + e.getMessage());
        }
    }

    public PublicKey generateKey() throws UserCancellationException {
        try {
            //	for now it is RSA, later make it generic
            KeyPair kp = CryptoTools.createTinyRSAKeyPair();
            PrivateKey pvtKey = kp.getPrivate();
            PublicKey pubKey = kp.getPublic();

            byte[] encodedPublicKey = pubKey.getEncoded();
            String alias = CryptoTools.encodeBase32(CryptoTools.digest(encodedPublicKey));

            byte[] wrappedPrivateKey = CryptoTools.wrapKey(agent.getPassPhrase(alias), pvtKey);

            SQLStore store = new SQLStore(alias, wrappedPrivateKey, encodedPublicKey);

            SQLStoreAccess access = new SQLStoreAccess();
            access.add(store);

            return pubKey;
        } catch (CryptoException e) {
            throw new UserCancellationException("[SQLSigner] [generateKey] -- " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            throw new UserCancellationException("[SQLSigner] [generateKey] -- " + e.getMessage());
        }
    }

    /* (non-Javadoc)
     * @see org.neuclear.commons.crypto.signers.Signer#save()
     */
    public void save() throws UserCancellationException {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see org.neuclear.commons.crypto.signers.PublicKeySource#getPublicKey(java.lang.String)
     */
    public PublicKey getPublicKey(String name)
            throws NonExistingSignerException {
        try {
            SQLStore store = access.verifyAlias(name);
            byte[] arrPubKey = store.getPublicKey();
            return CryptoTools.getPublicKeyFromBase64(new String(arrPubKey));
        } catch (CryptoException e) {
            throw new NonExistingSignerException("[SQLSigner] [getPublicKey] -- " + e.getMessage());
        }
    }

    public static void main(String[] args) {
    }
}
