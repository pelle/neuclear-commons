/*
 * $Id: SimpleSigner.java,v 1.1 2003/11/11 21:17:47 pelle Exp $
 * $Log: SimpleSigner.java,v $
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

import org.neuclear.commons.NeuClearException;
import org.neuclear.commons.crypto.passphraseagents.PassPhraseAgent;
import org.neuclear.commons.crypto.CryptoException;
import org.neuclear.commons.crypto.CryptoTools;
import org.neuclear.commons.crypto.signers.InvalidPassphraseException;
import org.neuclear.commons.crypto.signers.NonExistingSignerException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import java.io.*;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple memory based implementation of Signer.
 * Currently it doesnt even use the passphrase. However it does do a SHA1 digest on the name first.
 */
public class SimpleSigner implements Signer {

    public SimpleSigner(String file, PassPhraseAgent agent) throws NeuClearException, GeneralSecurityException {
        this.agent = agent;
        try {
            signerFile = new File(file);
            if (signerFile.exists()) {
                System.out.println("NEUDIST: Loading KeyStore");
                FileInputStream in = new FileInputStream(signerFile);
                ObjectInputStream s = new ObjectInputStream(in);
                ks = (HashMap) s.readObject();
            } else
                ks = new HashMap();

            kf = KeyFactory.getInstance("RSA", "BC");
        } catch (IOException e) {
            throw new NeuClearException(e);
        } catch (ClassNotFoundException e) {
            throw new NeuClearException(e);
        }
    }

    private PrivateKey getKey(String name, char passphrase[]) throws CryptoException, NonExistingSignerException {
        System.out.println("NEUDIST: UnSealing key " + name + " ...");
        byte encrypted[] = (byte[]) ks.get(getDigestedName(name));
        if (encrypted == null)
            throw new NonExistingSignerException("Signer " + name + "doesnt exist in this Store");
        ByteArrayInputStream bis = new ByteArrayInputStream(encrypted);
        byte keyBytes[] = new byte[0];
        try {
            Cipher c = CryptoTools.makePBECipher(Cipher.DECRYPT_MODE, passphrase);
            CipherInputStream cin = new CipherInputStream(bis, c);
            DataInputStream din = new DataInputStream(cin);
            //byte keyBytes[]=new byte[c.getOutputSize(encrypted.length)];
            if (din.readInt() != 11870)  //This is just a quick check to see if the passphrase worked
                throw new InvalidPassphraseException("Passphrase Didnt Match");

            int i = din.readInt();
            // Sanity Check
            if (i > 5000)
                throw new InvalidPassphraseException("Returned key is too big");
            keyBytes = new byte[i];
            din.readFully(keyBytes, 0, keyBytes.length);
            din.close();
            KeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            return kf.generatePrivate(spec);
        } catch (GeneralSecurityException e) {
            throw new InvalidPassphraseException(e.getLocalizedMessage());
        } catch (IOException e) {
            throw new CryptoException(e);
        }
    }

    /**
     * Adds the given key to the store.
     * 
     * @param name       The name to store it as
     * @param passphrase The passphrase to encrypt the key
     * @param key        The PrivateKey itself.
     */

    public void addKey(String name, char passphrase[], PrivateKey key) throws GeneralSecurityException, IOException {
        System.out.println("NEUDIST: Sealing key: " + name + " in format " + key.getFormat());
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        DataOutputStream dOut = new DataOutputStream(bOut);
        Cipher c = CryptoTools.makePBECipher(Cipher.ENCRYPT_MODE, passphrase);
        CipherOutputStream cOut = new CipherOutputStream(dOut, c);
        dOut = new DataOutputStream(cOut);
        dOut.writeInt(11870);//This is just a quick check to see if the passphrase worked
        byte keyBytes[] = key.getEncoded(); //I'm assuming this is PKCS8, If not tough dooda
        dOut.writeInt(keyBytes.length);
        dOut.write(keyBytes);
        dOut.close();
        byte encrypted[] = bOut.toByteArray();
        ks.put(getDigestedName(name), encrypted);
    }

    public boolean canSignFor(String name) throws CryptoException {
        return ks.containsKey(getDigestedName(name));
    }

    /**
     * Checks the key type of the given alias
     * @param name
     * @return KEY_NONE,KEY_RSA,KEY_DSA
     * @throws CryptoException
     */
    public int getKeyType(String name) throws CryptoException {
        return (canSignFor(name))?KEY_RSA:KEY_NONE; // We always use RSA here
    }

    static final protected String getDigestedName(String name) {
        return new String(CryptoTools.digest(name.getBytes()));
    }

    public void save() throws IOException {
        if (signerFile.getParent() != null)
            signerFile.getParentFile().mkdirs();

        FileOutputStream f = new FileOutputStream(signerFile);
        ObjectOutput s = new ObjectOutputStream(f);
        s.writeObject(ks);
        s.flush();

    }

    /**
     * Signs the data with the privatekey of the given name
     * 
     * @param name Alias of private key to be used within KeyStore
     * @param data Data to be signed
     * @return The signature
     * @throws InvalidPassphraseException if the passphrase doesn't match
     */

    public byte[] sign(String name, byte data[]) throws CryptoException {

        return CryptoTools.sign(getKey(name, agent.getPassPhrase(name)), data);
    }

    private KeyFactory kf;
    private Map ks;

    private final File signerFile;
    private final PassPhraseAgent agent;
}
