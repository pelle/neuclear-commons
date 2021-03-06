/*
 * $Id: SimpleSigner.java,v 1.11 2004/07/21 23:07:16 pelle Exp $
 * $Log: SimpleSigner.java,v $
 * Revision 1.11  2004/07/21 23:07:16  pelle
 * Updated the Signer interface with a new generateKey() method, which doesn't take any parameters.
 * It stores the generated key using the Base32 encoded SHA1 digest as it's alias.
 *
 * Revision 1.10  2004/01/19 17:53:14  pelle
 * Various clean ups
 *
 * Revision 1.9  2003/12/19 18:02:53  pelle
 * Revamped a lot of exception handling throughout the framework, it has been simplified in most places:
 * - For most cases the main exception to worry about now is InvalidNamedObjectException.
 * - Most lowerlevel exception that cant be handled meaningful are now wrapped in the LowLevelException, a
 *   runtime exception.
 * - Source and Store patterns each now have their own exceptions that generalizes the various physical
 *   exceptions that can happen in that area.
 *
 * Revision 1.8  2003/12/19 00:31:15  pelle
 * Lots of usability changes through out all the passphrase agents and end user tools.
 *
 * Revision 1.7  2003/12/18 17:40:07  pelle
 * You can now create keys that get stored with a X509 certificate in the keystore. These can be saved as well.
 * IdentityCreator has been modified to allow creation of keys.
 * Note The actual Creation of Certificates still have a problem that will be resolved later today.
 *
 * Revision 1.6  2003/12/16 23:16:40  pelle
 * Work done on the SigningServlet. The two phase web model is now only an option.
 * Allowing much quicker signing, using the GuiDialogueAgent.
 * The screen has also been cleaned up and displays the xml to be signed.
 * The GuiDialogueAgent now optionally remembers passphrases and has a checkbox to support this.
 * The PassPhraseAgent's now have a UserCancellationException, which allows the agent to tell the application if the user specifically
 * cancels the signing process.
 *
 * Revision 1.5  2003/12/10 23:55:45  pelle
 * Did some cleaning up in the builders
 * Fixed some stuff in IdentityCreator
 * New maven goal to create executable jarapp
 * We are close to 0.8 final of ID, 0.11 final of XMLSIG and 0.5 of commons.
 * Will release shortly.
 *
 * Revision 1.4  2003/11/21 04:43:41  pelle
 * EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
 * Otherwise You will Finaliate.
 * Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
 * This should hopefully make everything more stable (and secure).
 *
 * Revision 1.3  2003/11/19 23:32:50  pelle
 * Signers now can generatekeys via the generateKey() method.
 * Refactored the relationship between SignedNamedObject and NamedObjectBuilder a bit.
 * SignedNamedObject now contains the full xml which is returned with getEncoded()
 * This means that it is now possible to further receive on or process a SignedNamedObject, leaving
 * NamedObjectBuilder for its original purposes of purely generating new Contracts.
 * NamedObjectBuilder.sign() now returns a SignedNamedObject which is the prefered way of processing it.
 * Updated all major interfaces that used the old model to use the new model.
 *
 * Revision 1.2  2003/11/12 18:54:42  pelle
 * Updated SimpleSignerStoreTest to use a StoredPassPhraseAgent eliminating the popup during testing.
 * Created SigningBenchmark for running comparative performance benchmarks on various key algorithms.
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
 * Revision 1.5  2003/02/10 22:30:13  pelle
 * Got rid of even further dependencies. In Particular OSCore
 *
 * Revision 1.4  2003/02/09 00:15:55  pelle
 * Fixed things so they now compile with r_0.7 of XMLSig
 *
 * Revision 1.3  2002/10/06 00:39:26  pelle
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
import org.neuclear.commons.crypto.CryptoException;
import org.neuclear.commons.crypto.CryptoTools;
import org.neuclear.commons.crypto.passphraseagents.PassPhraseAgent;
import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.*;
import java.security.*;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple memory based implementation of Signer.
 * Currently it doesnt even use the passphrase. However it does do a SHA1 digest on the name first.
 */
public final class SimpleSigner implements Signer {

    public SimpleSigner(final String file, final PassPhraseAgent agent) {
        this.agent = agent;
        try {
            signerFile = new File(file);
            if (signerFile.exists()) {
                System.out.println("NeuClear: Loading KeyStore");
                final FileInputStream in = new FileInputStream(signerFile);
                final ObjectInputStream s = new ObjectInputStream(in);
                ks = (HashMap) s.readObject();
            } else
                ks = new HashMap();
            kf = KeyFactory.getInstance("RSA");
            kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024, SecureRandom.getInstance("SHA1PRNG"));

        } catch (IOException e) {
            throw new LowLevelException(e);
        } catch (ClassNotFoundException e) {
            throw new LowLevelException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new LowLevelException(e);
        }
    }

    private PrivateKey getKey(final String name, final char[] passphrase) throws InvalidPassphraseException, NonExistingSignerException {
        System.out.println("NEUDIST: UnSealing key " + name + " ...");
        final byte[] encrypted = (byte[]) ks.get(getDigestedName(name));
        if (encrypted == null)
            throw new NonExistingSignerException("Signer " + name + "doesnt exist in this Store");
        final ByteArrayInputStream bis = new ByteArrayInputStream(encrypted);
        byte keyBytes[] = new byte[0];
        try {
            final Cipher c = CryptoTools.makePBECipher(Cipher.DECRYPT_MODE, passphrase);
            final CipherInputStream cin = new CipherInputStream(bis, c);
            final DataInputStream din = new DataInputStream(cin);
            //byte keyBytes[]=new byte[c.getOutputSize(encrypted.length)];
            if (din.readInt() != 11870)  //This is just a quick check to see if the passphrase worked
                throw new InvalidPassphraseException("Passphrase Didnt Match");

            final int i = din.readInt();
            // Sanity Check
            if (i > 5000)
                throw new InvalidPassphraseException("Returned key is too big");
            keyBytes = new byte[i];
            din.readFully(keyBytes, 0, keyBytes.length);
            din.close();
            final KeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            return kf.generatePrivate(spec);
        } catch (GeneralSecurityException e) {
            throw new InvalidPassphraseException(e.getLocalizedMessage());
        } catch (IOException e) {
            throw new LowLevelException(e);
        }
    }

    /**
     * Adds the given key to the store. Uses the PassPhrase
     * agent to ask for PassPhrase.
     *
     * @param name The name to store it as
     * @param key  The PrivateKey itself.
     */

    public final void addKey(final String name, final PrivateKey key) throws UserCancellationException {
        addKey(name, agent.getPassPhrase(name), key);
    }

    /**
     * Adds the given key to the store.
     *
     * @param name       The name to store it as
     * @param passphrase The passphrase to encrypt the key
     * @param key        The PrivateKey itself.
     */

    public final void addKey(final String name, final char[] passphrase, final PrivateKey key) {
        System.out.println("NeuClear: Sealing key: " + name + " in format " + key.getFormat());
        try {
            final ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            DataOutputStream dOut = new DataOutputStream(bOut);
            final Cipher c = CryptoTools.makePBECipher(Cipher.ENCRYPT_MODE, passphrase);
            final CipherOutputStream cOut = new CipherOutputStream(dOut, c);
            dOut = new DataOutputStream(cOut);
            dOut.writeInt(11870);//This is just a quick check to see if the passphrase worked
            final byte[] keyBytes = key.getEncoded(); //I'm assuming this is PKCS8, If not tough dooda
            dOut.writeInt(keyBytes.length);
            dOut.write(keyBytes);
            dOut.close();
            final byte[] encrypted = bOut.toByteArray();
            ks.put(getDigestedName(name), encrypted);
        } catch (GeneralSecurityException e) {
            throw new LowLevelException(e);
        } catch (IOException e) {
            throw new LowLevelException(e);
        }
    }

    public final boolean canSignFor(final String name) {
        return ks.containsKey(getDigestedName(name));
    }

    /**
     * Checks the key type of the given alias
     *
     * @param name
     * @return KEY_NONE,KEY_RSA,KEY_DSA
     */
    public final int getKeyType(final String name) {
        return (canSignFor(name)) ? KEY_RSA : KEY_NONE; // We always use RSA here
    }

    static final protected String getDigestedName(final String name) {
        return new String(CryptoTools.digest(name.getBytes()));
    }

    public final void save() {
        if (signerFile.getParent() != null)
            signerFile.getParentFile().mkdirs();

        try {
            final FileOutputStream f = new FileOutputStream(signerFile);
            final ObjectOutput s = new ObjectOutputStream(f);
            s.writeObject(ks);
            s.flush();
        } catch (IOException e) {
            throw new LowLevelException(e);
        }

    }

    /**
     * Signs the data with the privatekey of the given name
     *
     * @param name Alias of private key to be used within KeyStore
     * @param data Data to be signed
     * @return The signature
     * @throws UserCancellationException
     */
    public final byte[] sign(final String name, final byte[] data) throws UserCancellationException {
        return sign(name, data, false);
    }

    private final byte[] sign(final String name, final byte[] data, boolean incorrect) throws UserCancellationException {
        try {
            return CryptoTools.sign(getKey(name, agent.getPassPhrase(name, incorrect)), data);
        } catch (CryptoException e) {
            return sign(name, data, true);
        }
    }

    public final PublicKey generateKey(final String alias) throws UserCancellationException {
        final KeyPair kp = kpg.generateKeyPair();
        addKey(alias, agent.getPassPhrase(alias), kp.getPrivate());
        return kp.getPublic();
    }

    public PublicKey generateKey() throws UserCancellationException {
        final KeyPair kp = kpg.generateKeyPair();
        String alias = CryptoTools.encodeBase32(CryptoTools.digest(kp.getPublic().getEncoded()));
        addKey(alias, agent.getPassPhrase(alias), kp.getPrivate());
        return kp.getPublic();
    }

    private KeyFactory kf;
    private Map ks;

    private final File signerFile;
    private final PassPhraseAgent agent;
    private final KeyPairGenerator kpg;
}
