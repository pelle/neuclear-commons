/*
 * $Id: Signer.java,v 1.4 2003/12/18 17:40:07 pelle Exp $
 * $Log: Signer.java,v $
 * Revision 1.4  2003/12/18 17:40:07  pelle
 * You can now create keys that get stored with a X509 certificate in the keystore. These can be saved as well.
 * IdentityCreator has been modified to allow creation of keys.
 * Note The actual Creation of Certificates still have a problem that will be resolved later today.
 *
 * Revision 1.3  2003/12/10 23:55:45  pelle
 * Did some cleaning up in the builders
 * Fixed some stuff in IdentityCreator
 * New maven goal to create executable jarapp
 * We are close to 0.8 final of ID, 0.11 final of XMLSIG and 0.5 of commons.
 * Will release shortly.
 *
 * Revision 1.2  2003/11/19 23:32:50  pelle
 * Signers now can generatekeys via the generateKey() method.
 * Refactored the relationship between SignedNamedObject and NamedObjectBuilder a bit.
 * SignedNamedObject now contains the full xml which is returned with getEncoded()
 * This means that it is now possible to further receive on or process a SignedNamedObject, leaving
 * NamedObjectBuilder for its original purposes of purely generating new Contracts.
 * NamedObjectBuilder.sign() now returns a SignedNamedObject which is the prefered way of processing it.
 * Updated all major interfaces that used the old model to use the new model.
 *
 * Revision 1.1  2003/11/11 21:17:47  pelle
 * Further vital reshuffling.
 * org.neudist.crypto.* and org.neudist.utils.* have been moved to respective areas under org.neuclear.commons
 * org.neuclear.signers.* as well as org.neuclear.passphraseagents have been moved under org.neuclear.commons.crypto as well.
 * Did a bit of work on the Canonicalizer and changed a few other minor bits.
 *
 * Revision 1.3  2003/11/08 20:26:52  pelle
 * Updated the Signer interface to return a key type to be used for XML SignatureInfo. Thus we now support DSA sigs yet again.
 *
 * Revision 1.2  2003/10/29 23:17:10  pelle
 * Updated some javadocs
 * Added a neuclear specific maven repository at:
 * http://neuclear.org/maven/ and updated the properties files to reflect that.
 *
 * Revision 1.1  2003/10/29 21:15:53  pelle
 * Refactored the whole signing process. Now we have an interface called Signer which is the old SignerStore.
 * To use it you pass a byte array and an alias. The sign method then returns the signature.
 * If a Signer needs a passphrase it uses a PassPhraseAgent to present a dialogue box, read it from a command line etc.
 * This new Signer pattern allows us to use secure signing hardware such as N-Cipher in the future for server applications as well
 * as SmartCards for end user applications.
 *
 * Revision 1.3  2003/10/28 23:44:03  pelle
 * The GuiDialogAgent now works. It simply presents itself as a simple modal dialog box asking for a passphrase.
 * The two Signer implementations both use it for the passphrase.
 *
 * Revision 1.2  2003/02/20 13:26:41  pelle
 * Adding all of the modification from Rams?s Morales ramses@computer.org to support DSASHA1 Signatures
 * Thanks Rams?s good work.
 * So this means there is now support for:
 * - DSA KeyInfo blocks
 * - DSA Key Generation within CryptoTools
 * - Signing using DSASHA1
 *
 * Revision 1.1  2003/02/18 00:03:32  pelle
 * Moved the Signer classes from neuclearframework into neuclear-xmlsig
 *
 * Revision 1.4  2002/10/06 00:39:26  pelle
 * I have now expanded support for different types of Signers.
 * There is now a JCESigner which uses a JCE KeyStore for signing.
 * I have refactored the SigningServlet a bit, eliminating most of the demo code.
 * This has been moved into DemoSigningServlet.
 * I have expanded the CommandLineSigner, so it now also has an option for specifying a default signing service.
 * The default web application now contains two signers.
 * - The Demo one is still at /Signer
 * - There is a new one at /personal/Signer this uses the testkeys.ks for
 * signing anything under neu://test
 * Note neu://test now has a default interactive signer running on localhost.
 * So to play with this you must install the webapp on your own local machine.
 *
 * Revision 1.3  2002/09/23 15:09:11  pelle
 * Got the SimpleSigner working properly.
 * I couldn't get SealedObjects working with BouncyCastle's Symmetric keys.
 * Don't know what I was doing, so I reimplemented it. Encrypting
 * and decrypting it my self.
 *
 * Revision 1.2  2002/09/21 23:11:16  pelle
 * A bunch of clean ups. Got rid of as many hard coded URL's as I could.
 *
 * User: pelleb
 * Date: Sep 20, 2002
 * Time: 12:35:14 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package org.neuclear.commons.crypto.signers;

import org.neuclear.commons.crypto.CryptoException;

import java.security.PublicKey;
import java.security.cert.Certificate;


/**
 * The Signer follows the following model:
 * <pre>                                  (Optional)
 *                               +---------------+
 *                               |PassPhraseAgent|
 *                               +-------+-------+
 *                                       |PassPhrase
 *     +-------------------+         +---+----+
 *     |Signing Application|--Data--&gt;| Signer |
 *     +--------+----------+         +---+----+
 *              +-&lt;-----Signature--------+</pre>
 */
public interface Signer {
    /**
     * Signs the data with the privatekey of the given name
     * 
     * @param name Alias of private key to be used within KeyStore
     * @param data Data to be signed
     * @return The signature
     * @throws org.neuclear.commons.crypto.CryptoException
     *          
     */

    public byte[] sign(String name, byte data[]) throws CryptoException;

//    public void addKey(String name, char passphrase[], PrivateKey key) throws GeneralSecurityException, IOException ;

    /**
     * Returns true if the Signer contains a signer for the given name
     * 
     * @param name 
     * @return true if signer is contained
     * @throws CryptoException 
     */
    public boolean canSignFor(String name) throws CryptoException;


    /**
     * Checks the key type of the given alias
     * 
     * @param name 
     * @return KEY_NONE,KEY_RSA,KEY_DSA
     * @throws CryptoException 
     */
    public int getKeyType(String name) throws CryptoException;

    /**
     * Creates a new KeyPair, stores the PrivateKey using the given alias
     * and returns the PublicKey.
     *
     * @param alias
     * @return Generated PublicKey
     * @throws CryptoException
     */
    public PublicKey generateKey(String alias) throws CryptoException;

    final public static int KEY_NONE = 0;
    final public static int KEY_RSA = 1;
    final public static int KEY_DSA = 2;
    final public static int KEY_OTHER = -1;

    void save() throws CryptoException;

}
