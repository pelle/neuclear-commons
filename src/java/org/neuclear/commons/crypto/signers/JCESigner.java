/*
 * $Id: JCESigner.java,v 1.23 2004/04/13 17:32:07 pelle Exp $
 * $Log: JCESigner.java,v $
 * Revision 1.23  2004/04/13 17:32:07  pelle
 * Now has save dialog
 * Remembers passphrases
 *
 * Revision 1.22  2004/04/12 23:50:08  pelle
 * implemented the queue and improved the DefaultSigner
 *
 * Revision 1.21  2004/04/09 22:56:45  pelle
 * SwingAgent now manages key creation as well through the NewAliasDialog.
 * Many small uservalidation features have also been added.
 *
 * Revision 1.20  2004/04/09 18:40:45  pelle
 * BrowsableSigner now inherits Signer and PublicKeySource, which means implementations only need to implement BrowsableSigner now.
 * Added NewAliasDialog, which isnt yet complete.
 *
 * Revision 1.19  2004/04/07 17:22:10  pelle
 * Added support for the new improved interactive signing model. A new Agent is also available with SwingAgent.
 * The XMLSig classes have also been updated to support this.
 *
 * Revision 1.18  2004/03/22 20:09:05  pelle
 * Added simple ledger for unit testing and in memory use
 *
 * Revision 1.17  2004/01/20 17:38:58  pelle
 * Further updates to unit tests
 *
 * Revision 1.16  2004/01/19 17:53:14  pelle
 * Various clean ups
 *
 * Revision 1.15  2003/12/22 22:14:37  pelle
 * Last minute cleanups and documentation prior to release 0.8.1
 *
 * Revision 1.14  2003/12/19 18:02:53  pelle
 * Revamped a lot of exception handling throughout the framework, it has been simplified in most places:
 * - For most cases the main exception to worry about now is InvalidNamedObjectException.
 * - Most lowerlevel exception that cant be handled meaningful are now wrapped in the LowLevelException, a
 *   runtime exception.
 * - Source and Store patterns each now have their own exceptions that generalizes the various physical
 *   exceptions that can happen in that area.
 *
 * Revision 1.13  2003/12/19 00:31:15  pelle
 * Lots of usability changes through out all the passphrase agents and end user tools.
 *
 * Revision 1.12  2003/12/18 17:40:07  pelle
 * You can now create keys that get stored with a X509 certificate in the keystore. These can be saved as well.
 * IdentityCreator has been modified to allow creation of keys.
 * Note The actual Creation of Certificates still have a problem that will be resolved later today.
 *
 * Revision 1.11  2003/12/16 21:09:22  pelle
 * The Sample Web App is semi stable for now.
 *
 * Revision 1.10  2003/12/14 20:52:54  pelle
 * Added ServletPassPhraseAgent which uses ThreadLocal to transfer the passphrase to the signer.
 * Added ServletSignerFactory, which builds Signers for use within servlets based on parameters in the Servlets
 * Init parameters in web.xml
 * Updated SQLContext to use ThreadLocal
 * Added jakarta cactus unit tests to neuclear-commons to test the 2 new features above.
 * Added use of the new features in neuclear-commons to the servilets within neuclear-id and added
 * configuration parameters in web.xml
 *
 * Revision 1.9  2003/12/10 23:55:45  pelle
 * Did some cleaning up in the builders
 * Fixed some stuff in IdentityCreator
 * New maven goal to create executable jarapp
 * We are close to 0.8 final of ID, 0.11 final of XMLSIG and 0.5 of commons.
 * Will release shortly.
 *
 * Revision 1.8  2003/11/22 00:22:52  pelle
 * All unit tests in commons, id and xmlsec now work.
 * AssetController now successfully processes payments in the unit test.
 * Payment Web App has working form that creates a TransferRequest presents it to the signer
 * and forwards it to AssetControlServlet. (Which throws an XML Parser Exception) I think the XMLReaderServlet is bust.
 *
 * Revision 1.7  2003/11/21 04:43:41  pelle
 * EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
 * Otherwise You will Finaliate.
 * Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
 * This should hopefully make everything more stable (and secure).
 *
 * Revision 1.6  2003/11/19 23:32:50  pelle
 * Signers now can generatekeys via the generateKey() method.
 * Refactored the relationship between SignedNamedObject and NamedObjectBuilder a bit.
 * SignedNamedObject now contains the full xml which is returned with getEncoded()
 * This means that it is now possible to further receive on or process a SignedNamedObject, leaving
 * NamedObjectBuilder for its original purposes of purely generating new Contracts.
 * NamedObjectBuilder.sign() now returns a SignedNamedObject which is the prefered way of processing it.
 * Updated all major interfaces that used the old model to use the new model.
 *
 * Revision 1.5  2003/11/18 15:07:18  pelle
 * Changes to JCE Implementation
 * Working on getting all tests working including store tests
 *
 * Revision 1.4  2003/11/18 00:01:02  pelle
 * The simple signing web application for logging in and out is now working.
 * There had been an issue in the canonicalizer when dealing with the embedded object of the SignatureRequest object.
 *
 * Revision 1.3  2003/11/13 23:26:17  pelle
 * The signing service and web authentication application is now almost working.
 *
 * Revision 1.2  2003/11/12 23:47:50  pelle
 * Much work done in creating good test environment.
 * PaymentReceiverTest works, but needs a abit more work in its environment to succeed testing.
 *
 * Revision 1.1  2003/11/11 21:17:47  pelle
 * Further vital reshuffling.
 * org.neudist.crypto.* and org.neudist.utils.* have been moved to respective areas under org.neuclear.commons
 * org.neuclear.signers.* as well as org.neuclear.passphraseagents have been moved under org.neuclear.commons.crypto as well.
 * Did a bit of work on the Canonicalizer and changed a few other minor bits.
 *
 * Revision 1.3  2003/11/08 20:27:06  pelle
 * Updated the Signer interface to return a key type to be used for XML SignatureInfo. Thus we now support DSA sigs yet again.
 *
 * Revision 1.2  2003/10/29 23:17:53  pelle
 * Updated some javadocs
 * Added a neuclear specific maven repository at:
 * http://neuclear.org/maven/ and updated the properties files to reflect that.
 *
 * Revision 1.1  2003/10/29 21:16:28  pelle
 * Refactored the whole signing process. Now we have an interface called Signer which is the old SignerStore.
 * To use it you pass a byte array and an alias. The sign method then returns the signature.
 * If a Signer needs a passphrase it uses a PassPhraseAgent to present a dialogue box, read it from a command line etc.
 * This new Signer pattern allows us to use secure signing hardware such as N-Cipher in the future for server applications as well
 * as SmartCards for end user applications.
 *
 * Revision 1.4  2003/10/28 23:44:03  pelle
 * The GuiDialogAgent now works. It simply presents itself as a simple modal dialog box asking for a passphrase.
 * The two Signer implementations both use it for the passphrase.
 *
 * Revision 1.3  2003/10/21 22:29:59  pelle
 * Renamed NeudistException to NeuClearException and moved it to org.neuclear.commons where it makes more sense.
 * Unhooked the XMLException in the xmlsig library from NeuClearException to make all of its exceptions an independent hierarchy.
 * Obviously had to perform many changes throughout the code to support these changes.
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
 * Revision 1.2  2003/02/09 00:15:55  pelle
 * Fixed things so they now compile with r_0.7 of XMLSig
 *
 * Revision 1.1  2002/10/06 00:39:26  pelle
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
 * Revision 1.2  2002/09/23 15:09:11  pelle
 * Got the SimpleSigner working properly.
 * I couldn't get SealedObjects working with BouncyCastle's Symmetric keys.
 * Don't know what I was doing, so I reimplemented it. Encrypting
 * and decrypting it my self.
 *
 * Revision 1.1  2002/09/21 23:11:16  pelle
 * A bunch of clean ups. Got rid of as many hard coded URL's as I could.
 *
 * User: pelleb
 * Date: Sep 20, 2002
 * Time: 12:37:32 PM
 */
package org.neuclear.commons.crypto.signers;

import org.neuclear.commons.LowLevelException;
import org.neuclear.commons.Utility;
import org.neuclear.commons.crypto.CryptoException;
import org.neuclear.commons.crypto.CryptoTools;
import org.neuclear.commons.crypto.passphraseagents.AlwaysTheSamePassphraseAgent;
import org.neuclear.commons.crypto.passphraseagents.InteractiveAgent;
import org.neuclear.commons.crypto.passphraseagents.PassPhraseAgent;
import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;

import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Wrapper around JCE KeyStore
 */
public class JCESigner implements BrowsableSigner {

    /**
     * Constructs a JCESigner with the agent providing the keystore passphrase.
     *
     * @param filename
     * @param type
     * @param provider
     * @param agent
     * @throws InvalidPassphraseException If the given passphrase is incorrect
     * @throws UserCancellationException  If the user choses to cancel the process in the passphrase agent, this should cancel the loading process
     */
    public JCESigner(final String filename, final String type, final String provider, final PassPhraseAgent agent) throws UserCancellationException, InvalidPassphraseException {
        this(filename, createInputStream(filename), type, provider, agent);
        this.filename = filename;
    }

    /**
     * Constructs a JCESigner providing a initial passphrase in the parameters.
     *
     * @param filename
     * @param type
     * @param provider
     * @param agent
     * @param initialpassphrase
     * @throws InvalidPassphraseException If the given passphrase is incorrect
     */
    public JCESigner(final String filename, final String type, final String provider, final PassPhraseAgent agent, final char[] initialpassphrase) throws InvalidPassphraseException {
        this(filename, createInputStream(filename), type, provider, agent, initialpassphrase);
        this.filename = filename;

    }

    /**
     * The purpose of this method is to either return an InputStream or Null. The reason being that the Keystore accepts null
     * to create a new KeyStore in memory.
     *
     * @param filename
     * @return
     */
    private static InputStream createInputStream(final String filename) {
        if (Utility.isEmpty(filename))
            return null;
        final File file = new File(filename);
        if (!file.exists())
            return null;
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.err.println(e.getLocalizedMessage());
            throw new LowLevelException(e);
        }
    }

    /**
     * Constructs a JCESigner using the agent to provide the initial passphrase
     *
     * @param name
     * @param in
     * @param type
     * @param provider
     * @param agent
     * @throws InvalidPassphraseException If the given passphrase is incorrect
     * @throws UserCancellationException  If the user choses to cancel the process in the passphrase agent, this should cancel the loading process
     */
    protected JCESigner(final String name, final InputStream in, final String type, final String provider, final PassPhraseAgent agent) throws UserCancellationException, InvalidPassphraseException {
        this(loadKeyStore(provider, type, in, agent, name), agent);
    }

    /**
     * Constructs a JCESigner using the provided Initial passphrase to load the keystore
     *
     * @param name
     * @param in
     * @param type
     * @param provider
     * @param agent
     * @param initpassphrase
     * @throws InvalidPassphraseException If the given passphrase is incorrect
     */
    protected JCESigner(final String name, final InputStream in, final String type, final String provider, final PassPhraseAgent agent, final char[] initpassphrase) throws InvalidPassphraseException {
        this(loadKeyStore(provider, type, in, initpassphrase), agent);
    }

    private static KeyStore loadKeyStore(final String provider, final String type, final InputStream in, final char[] passphrase) throws InvalidPassphraseException {
        try {
            return loadKeyStore(provider, type, in, new AlwaysTheSamePassphraseAgent(passphrase), "keystore");
        } catch (UserCancellationException e) {
            throw new LowLevelException(e);
        }
    }

    private static KeyStore loadKeyStore(final String provider, final String type, final InputStream in, final PassPhraseAgent agent, final String name) throws InvalidPassphraseException, UserCancellationException {
//       System.out.println("Loading JCESigner using passphrase: "+new String(passphrase));
        try {
            KeyStore ki = null;
            if (provider == null)
                ki = KeyStore.getInstance(type);
            else
                ki = KeyStore.getInstance(type, provider);
            if (in != null)
                ki.load(in, agent.getPassPhrase("Keystore password for: " + name));
            else
                ki.load(null, null);
            return ki;
        } catch (KeyStoreException e) {
            throw new LowLevelException(e);
        } catch (NoSuchProviderException e) {
            throw new LowLevelException(e);
        } catch (IOException e) {
            System.err.println("Incorrect Passphrase");
            throw new InvalidPassphraseException("entered passphrase was invalid");
        } catch (NoSuchAlgorithmException e) {
            throw new LowLevelException(e);
        } catch (CertificateException e) {
            throw new LowLevelException(e);
        }
    }

    /**
     * Creates a signer based on a fully loaded keystore
     *
     * @param ks
     * @param agent
     */
    public JCESigner(final KeyStore ks, final PassPhraseAgent agent) {
        this.agent = agent;
        this.ks = ks;
        cache = new KeyCache(ks);
        try {
            kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024, SecureRandom.getInstance("SHA1PRNG"));
        } catch (NoSuchAlgorithmException e) {
            throw new LowLevelException(e);
        }

    }

    private PrivateKey getKey(final String name, final char[] passphrase) throws UnrecoverableKeyException, NonExistingSignerException, NoSuchAlgorithmException, KeyStoreException {
        try {
            final PrivateKey key = (PrivateKey) cache.getKey(name, passphrase);
            if (key == null)
                throw new NonExistingSignerException("No keys for: " + name);
            return key;
        } catch (ClassCastException e) {
            throw new NonExistingSignerException("Incorrect Key type found");
        }

    }

    public final byte[] sign(final String name, final byte[] data) throws NonExistingSignerException, UserCancellationException {
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

    public final boolean canSignFor(final String name) {
        try {
            return ks.containsAlias(name);
        } catch (KeyStoreException e) {
            throw new LowLevelException(e);
        }
    }

    public final int getKeyType(final String name) {
        try {
            if (ks.isKeyEntry(name)) {
                final PublicKey pk = getPublicKey(name);
                if (pk instanceof RSAPublicKey)
                    return KEY_RSA;
                if (pk instanceof DSAPublicKey)
                    return KEY_DSA;
                return KEY_OTHER;
            }
        } catch (KeyStoreException e) {
            throw new LowLevelException(e);
        } catch (NonExistingSignerException e) {
            return KEY_NONE;
        }
        return KEY_NONE;  //To change body of implemented methods use Options | File Templates.
    }


    public final PublicKey generateKey(final String alias) throws UserCancellationException {
        try {
            final KeyPair kp = kpg.generateKeyPair();
            ks.setKeyEntry(alias, kp.getPrivate(), agent.getPassPhrase(alias), new Certificate[]{CryptoTools.createCertificate(alias, kp)});
            return kp.getPublic();
        } catch (KeyStoreException e) {
            throw new LowLevelException(e);
        } catch (SignatureException e) {
            throw new LowLevelException(e);
        } catch (InvalidKeyException e) {
            throw new LowLevelException(e);
        }
    }

    public final PublicKey getPublicKey(final String name) throws NonExistingSignerException {
        try {
            final Certificate certificate = ks.getCertificate(name);
            if (certificate == null)
                throw new NonExistingSignerException(name);

            PublicKey pub = certificate.getPublicKey();
            if (pub == null)
                throw new NonExistingSignerException(name);
            return pub;

        } catch (KeyStoreException e) {
            throw new LowLevelException(e);
        }
    }

    public byte[] sign(byte data[], SetPublicKeyCallBack callback) throws UserCancellationException {
        return ((InteractiveAgent) agent).sign(this, data, callback);
    }

    public byte[] sign(String name, char pass[], byte data[], SetPublicKeyCallBack callback) throws InvalidPassphraseException {
        try {
            final byte[] bytes = CryptoTools.sign(getKey(name, pass), data);
            if (callback != null)
                callback.setPublicKey(getPublicKey(name));
            return bytes;
        } catch (UnrecoverableKeyException e) {
            throw new InvalidPassphraseException(name);
        } catch (NoSuchAlgorithmException e) {
            throw new LowLevelException(e);
        } catch (KeyStoreException e) {
            // Could try to reload it here but I wont for now
            throw new LowLevelException(e);
        } catch (CryptoException e) {
            throw new LowLevelException(e);
        }
    }

    public void createKeyPair(String alias, char passphrase[]) throws CryptoException {
        try {
            final KeyPair kp = kpg.generateKeyPair();
            ks.setKeyEntry(alias, kp.getPrivate(), passphrase, new Certificate[]{CryptoTools.createCertificate(alias, kp)});
//            if (!Utility.isEmpty(filename)) save();
        } catch (KeyStoreException e) {
            throw new LowLevelException(e);
        } catch (SignatureException e) {
            throw new LowLevelException(e);
        } catch (InvalidKeyException e) {
            throw new LowLevelException(e);
        }
    }

    public void save() {
        try {
            save(filename);
        } catch (FileNotFoundException e) {
            throw new LowLevelException(e);
        }
    }

    public synchronized final void save(String filename) throws FileNotFoundException {
        if (Utility.isEmpty(filename))
            throw new FileNotFoundException("no keystore filename");
        try {
            File ksfile = new File(filename);
            ksfile.getParentFile().mkdirs();
            System.out.println(Thread.currentThread());
            ks.store(new FileOutputStream(ksfile), agent.getPassPhrase(filename));
        } catch (Exception e) {
            throw new LowLevelException(e);
        }
    }

    public Iterator iterator() throws KeyStoreException {
        final Enumeration enum = ks.aliases();
        return new Iterator() {
            public void remove() {

            }

            public boolean hasNext() {
                return enum.hasMoreElements();
            }

            public Object next() {
                return enum.nextElement();
            }

        };
    }

    private final KeyStore ks;
    private final KeyCache cache;
    private final PassPhraseAgent agent;

    private final KeyPairGenerator kpg;
    private String filename;
}
