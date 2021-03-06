/*
 * $Id: CryptoTools.java,v 1.24 2004/08/26 01:06:36 pelle Exp $
 * $Log: CryptoTools.java,v $
 * Revision 1.24  2004/08/26 01:06:36  pelle
 * Fixed a bug in CryptoTools with regards to seeding the srng
 *
 * Revision 1.23  2004/05/21 19:24:05  pelle
 * Changed name of Neuclear Personal Signer to NeuClear Personal Trader
 * More changes from Personality to Account
 * Moved hibernates.properties out from the jar file and to the test directory and where ever it gets used, to avoid conflicts between multiple files.
 *
 * Revision 1.22  2004/04/19 18:45:26  pelle
 * Changed default signer store to use correct path seperators.
 *
 * Revision 1.21  2004/04/14 00:10:52  pelle
 * Added a MessageLabel for handling errors, validation and info
 * Save works well now.
 * It's pretty much there I think.
 *
 * Revision 1.20  2004/04/09 20:02:54  pelle
 * Added PrivateKey wrapping and unwrapping to CryptoTools with the methods:
 * byte [] wrapKey(char passphrase[], PrivateKey key)
 * and
 * PrivateKey unWrapKey(char passphrase[],byte wrapped[],String algorithm)
 * PrivateKey unWrapRSAKey(char passphrase[],byte wrapped[])
 *
 * Revision 1.19  2004/03/31 18:48:25  pelle
 * Added various Streams for simplified crypto operations.
 *
 * Revision 1.18  2004/03/19 22:21:24  pelle
 * Changes in the XMLSignature class, which is now Abstract there are currently 3 implementations for:
 * - Enveloped
 * - DataObjects - (Enveloping)
 * - Any for interop testing mainly.
 *
 * Revision 1.17  2004/03/18 21:31:26  pelle
 * Some fixups in SignedInfo
 *
 * Revision 1.16  2004/03/08 23:50:34  pelle
 * More improvements on the XMLSignature. Now uses the Transforms properly, References properly.
 * All the major elements have been refactored to be cleaner and more correct.
 *
 * Revision 1.15  2004/03/05 23:43:06  pelle
 * New Channels package with nio based channels for various crypto related tasks such as digests, signing, verifying and encoding.
 * DigestsChannel, SigningChannel and VerifyingChannel are complete, but not tested.
 * AbstractEncodingChannel will be used for a Base64/Base32 Channel as well as possibly an xml canonicalization channel in the xmlsig library.
 *
 * Revision 1.14  2004/02/19 15:29:10  pelle
 * Various cleanups and corrections
 *
 * Revision 1.13  2004/02/18 00:13:41  pelle
 * Many, many clean ups. I've readded Targets in a new method.
 * Gotten rid of NamedObjectBuilder and revamped Identity and Resolvers
 *
 * Revision 1.12  2004/01/18 21:20:20  pelle
 * Created Base32 encoder that now fully complies with Tyler's spec.
 *
 * Revision 1.11  2004/01/16 23:41:59  pelle
 * Added Base32 class. The Base32 encoding used wasnt following the standards.
 * Added user creatable Identity for Public Keys
 *
 * Revision 1.10  2004/01/09 16:34:32  pelle
 * changed use of base36 encoding to base32 to ensure compatibility with other schemes.
 *
 * Revision 1.9  2003/12/19 00:31:16  pelle
 * Lots of usability changes through out all the passphrase agents and end user tools.
 *
 * Revision 1.8  2003/12/18 17:40:07  pelle
 * You can now create keys that get stored with a X509 certificate in the keystore. These can be saved as well.
 * IdentityCreator has been modified to allow creation of keys.
 * Note The actual Creation of Certificates still have a problem that will be resolved later today.
 *
 * Revision 1.7  2003/12/10 23:55:45  pelle
 * Did some cleaning up in the builders
 * Fixed some stuff in IdentityCreator
 * New maven goal to create executable jarapp
 * We are close to 0.8 final of ID, 0.11 final of XMLSIG and 0.5 of commons.
 * Will release shortly.
 *
 * Revision 1.6  2003/12/06 00:16:35  pelle
 * Updated various areas in NSTools.
 * Updated URI Validation in particular to support new expanded format
 * Updated createUniqueID and friends to be a lot more unique and more efficient.
 * In CryptoTools updated getRandom() to finally use a SecureRandom.
 * Changed CryptoTools.getFormatURLSafe to getBase36 because that is what it really is.
 *
 * Revision 1.5  2003/11/21 04:43:41  pelle
 * EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
 * Otherwise You will Finaliate.
 * Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
 * This should hopefully make everything more stable (and secure).
 *
 * Revision 1.4  2003/11/20 23:41:36  pelle
 * Getting all the tests to work in id
 * Removing usage of BC in CryptoTools as it was causing issues.
 * First version of EntityLedger that will use OFB's EntityEngine. This will allow us to support a vast amount databases without
 * writing SQL. (Yipee)
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
 * Revision 1.2  2003/11/18 23:34:55  pelle
 * Payment Web Application is getting there.
 *
 * Revision 1.1  2003/11/11 21:17:48  pelle
 * Further vital reshuffling.
 * org.neudist.crypto.* and org.neudist.utils.* have been moved to respective areas under org.neuclear.commons
 * org.neuclear.signers.* as well as org.neuclear.passphraseagents have been moved under org.neuclear.commons.crypto as well.
 * Did a bit of work on the Canonicalizer and changed a few other minor bits.
 *
 * Revision 1.13  2003/11/09 03:27:01  pelle
 * More house keeping and shuffling about mainly pay
 *
 * Revision 1.12  2003/10/29 21:15:53  pelle
 * Refactored the whole signing process. Now we have an interface called Signer which is the old SignerStore.
 * To use it you pass a byte array and an alias. The sign method then returns the signature.
 * If a Signer needs a passphrase it uses a PassPhraseAgent to present a dialogue box, read it from a command line etc.
 * This new Signer pattern allows us to use secure signing hardware such as N-Cipher in the future for server applications as well
 * as SmartCards for end user applications.
 *
 * Revision 1.11  2003/10/28 23:44:15  pelle
 * The GuiDialogAgent now works. It simply presents itself as a simple modal dialog box asking for a passphrase.
 * The two Signer implementations both use it for the passphrase.
 *
 * Revision 1.10  2003/10/21 22:30:32  pelle
 * Renamed NeudistException to NeuClearException and moved it to org.neuclear.commons where it makes more sense.
 * Unhooked the XMLException in the xmlsig library from NeuClearException to make all of its exceptions an independent hierarchy.
 * Obviously had to perform many changes throughout the code to support these changes.
 *
 * Revision 1.9  2003/09/29 23:44:44  pelle
 * Trying to tweak Canonicalizer to function better.
 * Apparently the built in Sun JCE doesnt like the Keysizes of NSROOT
 * So now CryptoTools forces the use of BouncyCastle
 *
 * Revision 1.8  2003/09/26 23:52:38  pelle
 * Changes mainly in receiver and related fun.
 * First real neuclear stuff in the payment package. Added TransferContract and AssetControllerReceiver.
 *
 * Revision 1.7  2003/02/22 23:18:54  pelle
 * Additional fixes to the encoding problem.
 *
 * Revision 1.6  2003/02/22 16:54:10  pelle
 * Major structural changes in the whole processing framework.
 * Verification now supports Enveloping and detached signatures.
 * The reference element is a lot more important at the moment and handles much of the logic.
 * Replaced homegrown Base64 with Blackdowns.
 * Still experiencing problems with decoding foreign signatures. I reall dont understand it. I'm going to have
 * to reread the specs a lot more and study other implementations sourcecode.
 *
 * Revision 1.5  2003/02/21 22:48:10  pelle
 * New Test Infrastructure
 * Added test keys in src/testdata/keys
 * Modified tools to handle these keys
 *
 * Revision 1.4  2003/02/20 13:26:41  pelle
 * Adding all of the modification from Rams?s Morales ramses@computer.org to support DSASHA1 Signatures
 * Thanks Rams?s good work.
 * So this means there is now support for:
 * - DSA KeyInfo blocks
 * - DSA Key Generation within CryptoTools
 * - Signing using DSASHA1
 *
 * Revision 1.3  2003/02/18 00:03:03  pelle
 * Moved the Signer classes from neuclearframework into neuclear-xmlsig
 *
 * Revision 1.2  2003/02/11 14:47:02  pelle
 * Added benchmarking code.
 * DigestValue is now a required part.
 * If you pass a keypair when you sign, you get the PublicKey included as a KeyInfo block within the signature.
 *
 * Revision 1.1  2003/02/08 20:55:02  pelle
 * Some documentation changes.
 * Major reorganization of code. The code is slowly being cleaned up in such a way that we can
 * get rid of the org.neuclear.utils package and split out the org.neuclear.xml.soap package.
 * Got rid of tons of unnecessary dependencies.
 *
 * Revision 1.2  2003/01/21 03:14:11  pelle
 * Mainly clean ups through out and further documentation.
 *
 * Revision 1.1  2003/01/18 18:12:29  pelle
 * First Independent commit of the Independent XML-Signature API for NeuDist.
 *
 * Revision 1.5  2003/01/16 22:20:03  pelle
 * First Draft of new generalised Ledger Interface.
 * Currently we have a Book and Transaction class.
 * We also need a Ledger class and a Ledger Factory.
 *
 * Revision 1.4  2002/12/17 21:41:01  pelle
 * First part of refactoring of SignedNamedObject and SignedObject Interface/Class parings.
 *
 * Revision 1.3  2002/12/17 20:34:43  pelle
 * Lots of changes to core functionality.
 * First of all I've refactored most of the Resolving and verification code. I have a few more things to do
 * on it before I'm happy.
 * There is now a NSResolver class, which handles all the namespace resolution. I took most of the functionality
 * for this out of SignedNamedObject.
 * Then there is the veriifer, which verifies a given SignedNamedObject using the NSResolver.
 * This has simplified the SignedNamedObject classes drastically, leaving them as mainly data objects, which is what they
 * should be.
 * I have also gone around and tightened up security on many different classes, making clases and/or methods final where appropriate.
 * NSCache now operates using http://www.waterken.com's fantastic ADT collections library.
 * Something important has been added, which is a SignRequest named object. This signed object, embeds an unsigned
 * named object for signing by an end users' signing service.
 * Now were almost ready to start seriously implementing AssetIssuers and Transfers, which will be the most important
 * part of the framework.
 *
 * Revision 1.2  2002/09/23 15:09:18  pelle
 * Got the SimpleSigner working properly.
 * I couldn't get SealedObjects working with BouncyCastle's Symmetric keys.
 * Don't know what I was doing, so I reimplemented it. Encrypting
 * and decrypting it my self.
 *
 * Revision 1.1.1.1  2002/09/18 10:55:55  pelle
 * First release in new CVS structure.
 * Also first public release.
 * This implemnts simple named objects.
 * - Identity Objects
 * - NSAuth Objects
 *
 * Storage systems
 * - In Memory Storage
 * - Clear text file based storage
 * - Encrypted File Storage (with SHA256 digested filenames)
 * - CachedStorage
 * - SoapStorage
 *
 * Simple SOAP client/server
 * - Simple Single method call SOAP client, for arbitrary dom4j based requests
 * - Simple Abstract SOAP Servlet for implementing http based SOAP Servers
 *
 * Simple XML-Signature Implementation
 * - Based on dom4j
 * - SHA-RSA only
 * - Very simple (likely imperfect) highspeed canonicalizer
 * - Zero support for X509 (We dont like that anyway)
 * - Super Simple
 *
 *
 * Revision 1.4  2002/06/17 20:48:33  pelle
 * The NS functionality should now work. FileStore is working properly.
 * The example .ns objects in the neuspace folder have been updated with the
 * latest version of the format.
 * "neuspace/root.ns" should now be considered the universal parent of the
 * neuclear system.
 * Still more to go, but we're getting there. I will now focus on a quick
 * Web interface. After which Contracts will be added.
 *
 * Revision 1.3  2002/06/13 19:04:07  pelle
 * A start to a web interface into the architecture.
 * We're getting a bit further now with functionality.
 *
 * Revision 1.2  2002/06/05 23:42:05  pelle
 * The Throw clauses of several method definitions were getting out of hand, so I have
 * added a new wrapper exception NeuClearException, to keep things clean in the ledger.
 * This is used as a catchall wrapper for all Exceptions in the underlying API's such as IOExceptions,
 * XML Exceptions etc.
 * You can catch any Exception and rethrow it using Utility.rethrowException(e) as a quick way of handling
 * exceptions.
 * Otherwise the Store framework and the NameSpaces are really comming along quite well. I added a CachedStore
 * which wraps around any other Store and caches the access to the store.
 *
 * Revision 1.1.1.1  2002/05/29 10:02:23  pelle
 * Lets try one more time. This is the first rev of the next gen of Neudist
 *
 *
 */
package org.neuclear.commons.crypto;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.X509V3CertificateGenerator;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.interfaces.ECPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.neuclear.commons.time.TimeTools;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.Certificate;
import java.security.interfaces.DSAPrivateKey;
import java.security.interfaces.DSAPublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

// TODO Implement some code to automatically BC Provider if not installed

public final class CryptoTools {
    private CryptoTools() {
    }


    /**
     * Call this method at the beginning of an executable. To ensure that BouncyCastle gets installed properly.
     */
    public static void ensureProvider() {
        if (Security.getProvider("BC") == null) {
            System.err.println("Adding BouncyCastleProvider");
            Security.addProvider(new BouncyCastleProvider());
            System.err.println("Added BouncyCastleProvider");

        }
    }

    public static KeyPair getKeyPair(final KeyStore ks, final String s, final char[] password) throws CryptoException {
        try {
            final Certificate cert = ks.getCertificate(s);
            final PrivateKey priv = (PrivateKey) ks.getKey(s, password);
            if (cert == null || priv == null)
                throw new CryptoException("They KeyStore Doesn't Contain an entry for: " + s);
            return new KeyPair(cert.getPublicKey(), priv);
        } catch (KeyStoreException e) {
            rethrowException(e);
        } catch (NoSuchAlgorithmException e) {
            rethrowException(e);
        } catch (UnrecoverableKeyException e) {
            rethrowException(e);
        }
        return null;
    }


    public static String formatKeyAsHex(final Key key) {
        return formatByteArrayAsHex(key.getEncoded());
    }

    public static PublicKey getPublicKeyFromHex(final String hex) throws CryptoException {
        try {
            final byte[] barray = convertHexToByteArray(hex);
            final X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(barray);
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(pubKeySpec);
        } catch (NoSuchAlgorithmException e) {
            rethrowException(e);
        } catch (InvalidKeySpecException e) {
            rethrowException(e);
        }

        return null;
    }

    public static PublicKey getPublicKeyFromBase64(final String b64) throws CryptoException {
        try {
            final byte[] barray = Base64.decode(b64);
            final X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(barray);
            final KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(pubKeySpec);
        } catch (NoSuchAlgorithmException e) {
            rethrowException(e);
        } catch (InvalidKeySpecException e) {
            rethrowException(e);
        }

        return null;
    }

    public static String formatByteArrayAsHex(final byte[] barray) {
        final byte[] hexarray = new byte[2 * barray.length];
        int h = 0;
        for (int i = 0; i < barray.length; i++) {
            h = 2 * i;
            final byte src = barray[i];
            hexarray[h] = HEX_TABLE[(src & 0xF0) >> 4];
            hexarray[h + 1] = HEX_TABLE[src & 0x0F];

        }
        return new String(hexarray);

    }

    public static byte[] convertHexToByteArray(final String hex) {
        final byte[] hexarray = hex.getBytes();
        final byte[] bytearray = new byte[(hexarray.length / 2)];
        for (int i = 0; i < hexarray.length; i += 2) {
            final byte result;
            byte high = hexarray[i];
            byte low = hexarray[i + 1];
            high = mapHexChar((char) high);
            low = mapHexChar((char) low);
            result = (byte) ((high << 4) | low);
            bytearray[(i == 0 ? 0 : (i / 2))] = result;
        }
        return bytearray;
    }


    //Quick Hack. Not very efficient I know.
    public static byte[] pad(final byte[] value, final Cipher c) {
        final int blockSize = c.getBlockSize();
        return pad(value, blockSize);
    }

    public static byte[] pad(final byte[] value, final int blockSize) {
        final int mod = value.length % blockSize;
        final int diff = blockSize - mod;
        final byte[] output = new byte[value.length + diff];
        System.arraycopy(value, 0, output, 0, value.length);
        for (int i = value.length; i < output.length; i++)
            output[i] = (byte) 0;
        return output;
    }

    public static byte[] encrypt(final byte[] key, final String value) throws CryptoException {
        return encrypt(key, value.getBytes());
    }

    public static byte[] encrypt(final String key, final byte[] value) throws CryptoException {
        return encrypt(key.getBytes(), value);
    }

    public static byte[] encrypt(final String key, final String value) throws CryptoException {
        return encrypt(key.getBytes(), value.getBytes());
    }

    public static byte[] encrypt(final byte[] key, final byte[] value) throws CryptoException {
        return cipherProcess(key, value, true);
    }

    public static byte[] decrypt(final String key, final String value) throws CryptoException {
        return decrypt(key.getBytes(), value.getBytes());
    }

    public static byte[] decrypt(final byte[] key, final byte[] value) throws CryptoException {
        return cipherProcess(key, value, false);
    }

    private static byte[] cipherProcess(final byte[] key, final byte[] value, final boolean doencrypt) throws CryptoException {
        try {
            final BlockCipher engine = new AESEngine();
            final BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(engine));
            cipher.init(doencrypt, new KeyParameter(digest256(key)));

            final byte[] cipherText = new byte[cipher.getOutputSize(value.length)];

            final int outputLen = cipher.processBytes(value, 0, value.length, cipherText, 0);
            cipher.doFinal(cipherText, outputLen);
            return cipherText;
        } catch (InvalidCipherTextException e) {
            rethrowException(e);
        }
        return new byte[0];
    }

    public static Cipher getCipher(final byte[] key, final boolean doencrypt) throws CryptoException {
        try {
            final Cipher cipher = Cipher.getInstance("AES");
            final KeySpec keyspec = new SecretKeySpec(key, "AES");
            final SecretKeyFactory kf = SecretKeyFactory.getInstance("AES");
            final Key skey = kf.generateSecret(keyspec);
            cipher.init(doencrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, skey);
            return cipher;
        } catch (NoSuchAlgorithmException e) {
            rethrowException(e);
        } catch (NoSuchPaddingException e) {
            rethrowException(e);
        } catch (InvalidKeySpecException e) {
            rethrowException(e);
        } catch (InvalidKeyException e) {
            rethrowException(e);
        }
        return null;
    }

    public static byte[] sign(final KeyPair kp, final byte[] value) throws CryptoException {
        return sign(kp.getPrivate(), value);
    }

    public static byte[] sign(final PrivateKey key, final byte[] value) throws CryptoException {
        try {
            final Signature sig = getSignatureCipher(key);
            sig.update(value); // put plain text of lock data into signature.
            byte[] raw = sig.sign();
            if (key instanceof DSAPrivateKey) {
                raw = convertASN1toXMLDSIG(raw);
            }
            return raw;
        } catch (GeneralSecurityException e) {
            rethrowException(e);
        } catch (IOException e) {
            rethrowException(e);
        }
        return new byte[0];
    }

    public static Signature getSignatureCipher(final PrivateKey key) throws NoSuchAlgorithmException, InvalidKeyException {
        Signature sig = null;
        if (key instanceof RSAPrivateKey)
            sig = Signature.getInstance("SHA1withRSA"); // Set up signature object.
        else if (key instanceof DSAPrivateKey)
            sig = Signature.getInstance("SHA1withDSA");
        else if (key instanceof ECPrivateKey)
            sig = Signature.getInstance("SHA1withECDSA");

        sig.initSign(key); // Initialize with my private signing key.
        return sig;
    }

    public static Signature getSignatureCipher(final PublicKey key) throws NoSuchAlgorithmException, InvalidKeyException {
        Signature sig = null;
        if (key instanceof RSAPublicKey)
            sig = Signature.getInstance("SHA1withRSA"); // Set up signature object.
        else if (key instanceof DSAPublicKey)
            sig = Signature.getInstance("SHA1withDSA");
        else if (key instanceof ECPublicKey)
            sig = Signature.getInstance("SHA1withECDSA");

        sig.initVerify(key); // Initialize with my public key.
        return sig;
    }

    public static boolean verify(final PublicKey pk, final byte[] value, byte sigvalue[]) throws CryptoException {
        try {
            Signature sig = null;
            if (pk instanceof DSAPublicKey) {
                sig = Signature.getInstance("SHA1withDSA"); // Set up signature object.
                sigvalue = convertXMLDSIGtoASN1(sigvalue);
            } else if (pk instanceof RSAPublicKey) {
                sig = Signature.getInstance("SHA1withRSA");
            }
            sig.initVerify(pk); // Initialize with my private signing key.
            sig.update(value);
            return sig.verify(sigvalue);
        } catch (GeneralSecurityException e) {
//            e.printStackTrace();
            rethrowException(e);
        } catch (IOException e) {
//            e.printStackTrace();  //To change body of catch statement use Options | File Templates.
            rethrowException(e);
        }
        return false;
    }

    public static byte[] digest(final InputStream is) throws IOException {
        Digest digest = new org.bouncycastle.crypto.digests.SHA1Digest();
        byte buf[] = new byte[digest.getDigestSize()];
        int length = 0;
        while ((length = is.read(buf)) >= 0)
            digest.update(buf, 0, length);
        digest.doFinal(buf, 0);
        is.close();
        return buf;
    }

    public static byte[] digest(final byte[] value) {
        final Digest dig = new org.bouncycastle.crypto.digests.SHA1Digest();
        return digest(dig, value);
    }

    public static byte[] digest(final String value) {
        return digest(value.getBytes());
    }

    public static byte[] digest256(final byte[] value) {
        final Digest dig = new SHA256Digest();
        return digest(dig, value);
    }

    public static byte[] digest512(final byte[] value) {
        final Digest dig = new SHA512Digest();
        return digest(dig, value);
    }

    private static byte[] digest(final Digest dig, final byte[] value) {
        final byte[] output = new byte[dig.getDigestSize()];
        dig.update(value, 0, value.length);
        dig.doFinal(output, 0);
        return output;
    }

    public static boolean equalByteArrays(final byte[] one, final byte[] two) {
        if ((one == null && two != null) || (one != null && two == null))
            return false;
        if (one == null && two == null)
            return true;
        if (one.length != two.length)
            return false;
        for (int i = 0; i < one.length; i++) {
            if (one[i] != two[i])
                return false;
        }
        return true;
    }

    /**
     * Unpadded Base32 Encoding as defined in:
     * <a href="http://www.waterken.com/dev/Enc/base32/">http://www.waterken.com/dev/Enc/base32/</a>
     *
     * @param val
     * @return
     */
    public static String encodeBase32(final byte[] val) {
        return Base32.encode(val);
    }

    public static String createRandomID() {
        return createRandomID(RAND_BIT_LENGTH);

    }

    public static String createRandomID(int length) {
        final BigInteger big = new BigInteger(length, getRandomInstance());
        return big.toString(32);
    }

    private static synchronized SecureRandom getRandomInstance() {
        if (randSource == null)
            try {
                randSource = SecureRandom.getInstance("SHA1PRNG");
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        return randSource;
    }

    private static byte mapHexChar(char hex) {
        hex = Character.toUpperCase(hex);
        switch (hex) {
            case 'A':
                return (byte) 10;
            case 'B':
                return (byte) 11;
            case 'C':
                return (byte) 12;
            case 'D':
                return (byte) 13;
            case 'E':
                return (byte) 14;
            case 'F':
                return (byte) 15;
        }
        if (Character.isDigit(hex))
            return (byte) Character.getNumericValue(hex);
        else
            return (byte) 0;
    }

    /**
     * Adapted from BouncyCastle's JDKKeyStore class
     *
     * @param algorithm
     * @param mode
     * @param password
     * @param salt
     * @param iterationCount
     * @param provider
     * @return
     * @throws java.security.GeneralSecurityException
     *
     */
    public static Cipher makePBECipher(final String algorithm,
                                       final int mode,
                                       final char[] password,
                                       final byte[] salt,
                                       final int iterationCount,
                                       final String provider) throws GeneralSecurityException {
        final PBEKeySpec pbeSpec = new PBEKeySpec(password);
        final SecretKeyFactory keyFact = SecretKeyFactory.getInstance(algorithm);
        final PBEParameterSpec defParams = new PBEParameterSpec(salt, iterationCount);

        final Cipher cipher = Cipher.getInstance(algorithm);

        cipher.init(mode, keyFact.generateSecret(pbeSpec), defParams);

        return cipher;
    }

    /**
     * Adapted from BouncyCastle's JDKKeyStore class.<p>
     * This one is setup with some meaningful JCE and Algorithm Defaults
     *
     * @param mode
     * @param password
     * @param salt
     * @param iterationCount
     * @return
     * @throws java.security.GeneralSecurityException
     *
     */
    public static Cipher makePBECipher(final int mode,
                                       final char[] password,
                                       final byte[] salt,
                                       final int iterationCount) throws GeneralSecurityException {
        return makePBECipher(DEFAULT_PBE_ALGORITHM, mode, password, salt, iterationCount, DEFAULT_JCE_PROVIDER);
    }


    /**
     * Adapted from BouncyCastle's JDKKeyStore class.<p>
     * This one is setup with some meaningful JCE and Algorithm Defaults as well as general simple defaults
     *
     * @param mode
     * @param password
     * @return
     * @throws java.security.GeneralSecurityException
     *
     */
    public static Cipher makePBECipher(final int mode,
                                       final char[] password) throws GeneralSecurityException {
        return makePBECipher(DEFAULT_PBE_ALGORITHM, mode, password, DEFAULT_SALT, DEFAULT_ITERATION_COUNT, DEFAULT_JCE_PROVIDER);
    }

    public static byte[] wrapKey(final char password[], final PrivateKey key) throws CryptoException {
        try {
            Cipher cipher = makePBECipher(Cipher.WRAP_MODE, password);
            return cipher.wrap(key);
        } catch (BadPaddingException e) {
            throw new CryptoException(e);
        } catch (GeneralSecurityException e) {
            throw new CryptoException(e);
        }
    }

    public static PrivateKey unWrapKey(final char password[], final byte data[], final String algorithm) throws CryptoException {
        try {
            Cipher cipher = makePBECipher(Cipher.UNWRAP_MODE, password);
            return (PrivateKey) cipher.unwrap(data, algorithm, Cipher.PRIVATE_KEY);
        } catch (BadPaddingException e) {
            throw new CryptoException(e);
        } catch (GeneralSecurityException e) {
            throw new CryptoException(e);
        }
    }

    public static PrivateKey unWrapRSAKey(final char password[], final byte data[]) throws CryptoException {
        return unWrapKey(password, data, "RSA");
    }

    public static PublicKey createPK(final String mod, final String exp) throws CryptoException {
        try {
            final KeyFactory rsaFactory = KeyFactory.getInstance("RSA");
            final RSAPublicKeySpec rsaKeyspec = new RSAPublicKeySpec(new BigInteger(Base64.decode(mod)), new BigInteger(Base64.decode(exp)));
            return rsaFactory.generatePublic(rsaKeyspec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(System.err);
            throw new CryptoException(e);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace(System.err);
            throw new CryptoException(e);
        }
    }

    public static KeyPair createKeyPair() throws NoSuchAlgorithmException {
        return getKeyPairGenerator().generateKeyPair();
    }

    public static KeyPair createTinyRSAKeyPair() throws NoSuchAlgorithmException {
        return getTinyRSAKeyPairGenerator().generateKeyPair();
    }

    public static KeyPair createTinyDSAKeyPair() throws NoSuchAlgorithmException {
        return getTinyDSAKeyPairGenerator().generateKeyPair();
    }

    public static KeyPair createKeyPair(final String algorithm)
            throws NoSuchAlgorithmException {
        return getKeyPairGenerator(algorithm).generateKeyPair();
    }

    public static KeyPairGenerator getKeyPairGenerator() throws NoSuchAlgorithmException {
        if (kg == null) {
            kg = KeyPairGenerator.getInstance("RSA");

            kg.initialize(1024, getRandomInstance());
        }
        return kg;

    }

    public static KeyPairGenerator getTinyRSAKeyPairGenerator() throws NoSuchAlgorithmException {
        if (tkg == null) {
            tkg = KeyPairGenerator.getInstance("RSA");

            tkg.initialize(512, getRandomInstance());
        }
        return tkg;

    }

    public static KeyPairGenerator getTinyDSAKeyPairGenerator() throws NoSuchAlgorithmException {
        if (tdkg == null) {
            tdkg = KeyPairGenerator.getInstance("DSA");

            tdkg.initialize(512, getRandomInstance());
        }
        return tdkg;

    }

    public static KeyPairGenerator getKeyPairGenerator(final String algorithm)
            throws NoSuchAlgorithmException {
        if (!algorithm.equals(RSA) && !algorithm.equals(DSA))
            throw new NoSuchAlgorithmException(algorithm + " is not supported");

        if (null == dkg) {
            dkg = KeyPairGenerator.getInstance(algorithm);

            dkg.initialize(1024, getRandomInstance());
        }
        return dkg;
    }

    public static void rethrowException(final Throwable e) throws CryptoException {
        throw new CryptoException(e);
    }

    /**
     * Converts an ASN.1 DSA value to a XML Signature DSA Value.
     * <p/>
     * The JAVA JCE DSA Signature algorithm creates ASN.1 encoded (r,s) value
     * pairs; the XML Signature requires the core BigInteger values.
     *
     * @see <A HREF="http://www.w3.org/TR/xmldsig-core/#dsa-sha1">6.4.1 DSA</A>
     */
    public static byte[] convertASN1toXMLDSIG(final byte[] asn1Bytes) throws IOException {

        final byte rLength = asn1Bytes[3];
        int i;

        for (i = rLength; (i > 0) && (asn1Bytes[(4 + rLength) - i] == 0); i--) ;

        final byte sLength = asn1Bytes[5 + rLength];
        int j;

        for (j = sLength; (j > 0) && (asn1Bytes[(6 + rLength + sLength) - j] == 0); j--) ;

        if ((asn1Bytes[0] != 48) || (asn1Bytes[1] != asn1Bytes.length - 2)
                || (asn1Bytes[2] != 2) || (i > 20) || (asn1Bytes[4 + rLength] != 2)
                || (j > 20)) {

            throw new IOException("Invalid ASN.1 format of DSA signature");
        } else {
            final byte[] xmldsigBytes = new byte[40];

            System.arraycopy(asn1Bytes, (4 + rLength) - i, xmldsigBytes, 20 - i, i);
            System.arraycopy(asn1Bytes, (6 + rLength + sLength) - j, xmldsigBytes, 40 - j, j);

            return xmldsigBytes;
        }
    }

    /**
     * Converts a XML Signature DSA Value to an ASN.1 DSA value.
     * <p/>
     * The JAVA JCE DSA Signature algorithm creates ASN.1 encoded (r,s) value
     * pairs; the XML Signature requires the core BigInteger values.
     *
     * @see <A HREF="http://www.w3.org/TR/xmldsig-core/#dsa-sha1">6.4.1 DSA</A>
     */
    public static byte[] convertXMLDSIGtoASN1(final byte[] xmldsigBytes) throws IOException {

        if (xmldsigBytes.length != 40) {
            throw new IOException("Invalid XMLDSIG format of DSA signature");
        }

        int i;
        for (i = 20; (i > 0) && (xmldsigBytes[20 - i] == 0); i--) ;

        int j = i;
        if (xmldsigBytes[20 - i] < 0) {
            j += 1;
        }

        int k;
        for (k = 20; (k > 0) && (xmldsigBytes[40 - k] == 0); k--) ;

        int l = k;
        if (xmldsigBytes[40 - k] < 0) {
            l += 1;
        }

        final byte[] asn1Bytes = new byte[6 + j + l];

        asn1Bytes[0] = 48;
        asn1Bytes[1] = (byte) (4 + j + l);
        asn1Bytes[2] = 2;
        asn1Bytes[3] = (byte) j;

        System.arraycopy(xmldsigBytes, 20 - i, asn1Bytes, (4 + j) - i, i);

        asn1Bytes[4 + j] = 2;
        asn1Bytes[5 + j] = (byte) l;

        System.arraycopy(xmldsigBytes, 40 - k, asn1Bytes, (6 + j + l) - k, k);

        return asn1Bytes;
    }

    public final static Certificate createCertificate(String name, KeyPair kp) throws SignatureException, InvalidKeyException {
        X509V3CertificateGenerator gen = new X509V3CertificateGenerator();
//        Vector code=new Vector(1);
//        code.add(0,"CN");
//        Vector names=new Vector(1);
//        names.add(0,name);
//        final X509Name x509Name = new X509Name(code,names);
//        gen.setIssuerDN(x509Name);
        final X509Principal x509 = new X509Principal("CN=" + name + ", OU=NEU, O=NEU, L=NEU, ST=NEU, C=PA");
        gen.setSubjectDN(x509);
        gen.setIssuerDN(x509);
        gen.setPublicKey(kp.getPublic());
        gen.setNotBefore(new Date());
        gen.setNotAfter(TimeTools.get2020());
        gen.setSignatureAlgorithm("SHA1withRSA");
        gen.setSerialNumber(new BigInteger(digest(kp.getPublic().getEncoded())));
        return gen.generateX509Certificate(kp.getPrivate());
    }

    {
        ensureProvider();
    }

    private static final String RSA = "RSA", DSA = "DSA";
    private static KeyPairGenerator kg;
    private static KeyPairGenerator dkg;
    private static KeyPairGenerator tkg;
    private static KeyPairGenerator tdkg;
    public static final String DEFAULT_PBE_ALGORITHM = "PBEWithMD5AndDES";
    public static final String DEFAULT_JCE_PROVIDER = "BC";
    private static final byte DEFAULT_SALT[] = "LiquidNi".getBytes();
    private static final int DEFAULT_ITERATION_COUNT = 2048;
    private static final byte[] HEX_TABLE = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static SecureRandom randSource;

    public final static String DEFAULT_KEYSTORE = System.getProperty("user.home") + File.separatorChar + ".neuclear" + File.separatorChar + "accounts.jks";
    public static final int RAND_BIT_LENGTH = 128;
    private static final long YPLUS20 = 20 * 365 * 24 * 60 * 60;
}
