/*
 * $Id: Utility.java,v 1.1 2003/11/11 21:17:52 pelle Exp $
 * $Log: Utility.java,v $
 * Revision 1.1  2003/11/11 21:17:52  pelle
 * Further vital reshuffling.
 * org.neudist.crypto.* and org.neudist.utils.* have been moved to respective areas under org.neuclear.commons
 * org.neuclear.signers.* as well as org.neuclear.passphraseagents have been moved under org.neuclear.commons.crypto as well.
 * Did a bit of work on the Canonicalizer and changed a few other minor bits.
 *
 * Revision 1.6  2003/11/09 03:27:09  pelle
 * More house keeping and shuffling about mainly pay
 *
 * Revision 1.5  2003/10/21 22:30:33  pelle
 * Renamed NeudistException to NeuClearException and moved it to org.neuclear.commons where it makes more sense.
 * Unhooked the XMLException in the xmlsig library from NeuClearException to make all of its exceptions an independent hierarchy.
 * Obviously had to perform many changes throughout the code to support these changes.
 *
 * Revision 1.4  2003/09/26 23:52:47  pelle
 * Changes mainly in receiver and related fun.
 * First real neuclear stuff in the payment package. Added TransferContract and AssetControllerReceiver.
 *
 * Revision 1.3  2003/02/11 14:47:02  pelle
 * Added benchmarking code.
 * DigestValue is now a required part.
 * If you pass a keypair when you sign, you get the PublicKey included as a KeyInfo block within the signature.
 *
 * Revision 1.2  2003/02/08 20:55:05  pelle
 * Some documentation changes.
 * Major reorganization of code. The code is slowly being cleaned up in such a way that we can
 * get rid of the org.neuclear.utils package and split out the org.neuclear.xml.soap package.
 * Got rid of tons of unnecessary dependencies.
 *
 * Revision 1.1  2003/01/18 18:12:30  pelle
 * First Independent commit of the Independent XML-Signature API for NeuDist.
 *
 * Revision 1.5  2003/01/16 22:20:03  pelle
 * First Draft of new generalised Ledger Interface.
 * Currently we have a Book and Transaction class.
 * We also need a Ledger class and a Ledger Factory.
 *
 * Revision 1.4  2002/09/25 19:20:15  pelle
 * Added various new schemas and updated most of the existing ones.
 * Added explanation interface for explaining the purpose of a
 * SignedNamedObject to a user. We may want to use XSL instead.
 * Also made the signing webapp look a bit nicer.
 *
 * Revision 1.3  2002/09/21 23:11:16  pelle
 * A bunch of clean ups. Got rid of as many hard coded URL's as I could.
 *
 * Revision 1.2  2002/09/20 01:15:18  pelle
 * Added prototype webapplication under src/java
 * SOAPServlet appears to work
 * Any webservices taking named objects should subclass from ReceiverServlet
 * SigningServlet is not completely working right now, but
 * will be the main prototype of a web based signer.
 *
 * Other new features are GenericNamedObject for simple instantiation of
 * arbitrary named objects.
 *
 * Revision 1.1.1.1  2002/09/18 10:55:58  pelle
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
 * Revision 1.5  2002/06/18 03:04:11  pelle
 * Just added all the necessary jars.
 * Fixed a few things in the framework and
 * started a GUI Application to manage Neu's.
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
 * Revision 1.3  2002/06/13 19:04:08  pelle
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
package org.neuclear.commons;

import org.neuclear.commons.NeuClearException;

import java.io.PrintStream;

public class Utility {
    public static boolean isEmpty(Object obj) {
        return (obj == null || obj.toString().equals(""));
    }

    public static String denullString(String string) {
        return denullString(string, "");
    }

    public static String denullString(String string, String def) {
        if (string == null)
            return def;
        return string;
    }

    // Im sick of writing the same crap over and over again
    public static void handleException(Throwable e, PrintStream out) {
        out.println("Exception in :" + e.getClass().toString());
        out.println(e.getLocalizedMessage());
        e.printStackTrace(out);
    }

    public static void rethrowException(Throwable e) throws NeuClearException {
        throw new NeuClearException(e);
    }

    public static void handleException(Throwable e) {
        handleException(e, System.out);
    }

    public static boolean isTrue(String clause, boolean defaultVal) {
        if (isEmpty(clause))
            return defaultVal;
        clause = clause.toLowerCase();
        return (clause.equals("yes") || clause.equals("y") || clause.equals("1") || clause.equals("true"));
    }

    public static int getIntValue(String strValue, int defValue) {
        if (strValue == null)
            return defValue;

        try {
            return Integer.parseInt(strValue);
        } catch (NumberFormatException e) {
            return defValue;
        }

    }

    public static int getIntValue(String strValue) {
        return getIntValue(strValue, 0);
    }

    public static int getEnumVal(String[] enum, String key) {
        return getEnumVal(enum, key, -1);
    }

    public static int getEnumVal(String[] enum, String key, int def) {
        if (enum == null || key == null)
            return def;
        for (int i = 0; i < enum.length; i++)
            if (key.equals(enum[i]))
                return i;
        return def;
    }

/*
    public static String normalizeNeuURL(String url) throws MalformedURLException {
        boolean clearLastChar=url.endsWith("/");
        if (url.startsWith("/")) {
            if (clearLastChar)
                return "neu:/"+url.substring(0,url.length()-2);
            return "neu:/"+url;
        }
        if (url.startsWith("neu://")) {
            if (clearLastChar)
                return url.substring(0,url.length()-2);
            return url;
        }
        throw new MalformedURLException("NEU URL: "+url+" was malformed");

    }
*/

    /*   public static String deURLize(String url) {
           int i=url.indexOf("://");
           if (i>0)
               return url.substring(i+2);
           if (url.length()>0&&url.substring(0,1).equals("/"))
               return url;
           return "/"+url;
       }
   */
}
