/* $Id: ServletTools.java,v 1.10 2004/04/21 23:10:14 pelle Exp $
 * $Log: ServletTools.java,v $
 * Revision 1.10  2004/04/21 23:10:14  pelle
 * Fixed mac look and feel
 *
 * Revision 1.9  2004/03/22 20:09:05  pelle
 * Added simple ledger for unit testing and in memory use
 *
 * Revision 1.8  2004/03/02 18:58:44  pelle
 * Further cleanups in neuclear-id. Moved everything under id.
 *
 * Revision 1.7  2004/02/19 15:29:10  pelle
 * Various cleanups and corrections
 *
 * Revision 1.6  2003/12/22 22:14:37  pelle
 * Last minute cleanups and documentation prior to release 0.8.1
 *
 * Revision 1.5  2003/12/16 15:04:43  pelle
 * Added SignedMessage contract for signing simple textual contracts.
 * Added NeuSender, updated SmtpSender and Sender to take plain email addresses (without the mailto:)
 * Added AbstractObjectCreationTest to make it quicker to write unit tests to verify
 * NamedObjectBuilder/SignedNamedObject Pairs.
 * Sample application has been expanded with a basic email application.
 * Updated docs for simple web app.
 * Added missing LGPL LICENSE.txt files to signer and simple app
 *
 * Revision 1.4  2003/12/15 23:32:40  pelle
 * added ServletTools.getInitParam() which first tries the ServletConfig, then the context config.
 * All the web.xml's have been updated to support this. Also various further generalizations have been done throughout
 * for getServiceid(), getTitle(), getSigner()
 *
 * Revision 1.3  2003/12/12 19:27:38  pelle
 * All the Cactus tests now for signing signers.
 * Added working AuthenticationFilterTest
 * Returned original functionality to DemoSigningServlet.
 * This is set up to use the test keys stored in neuclear-commons.
 * SigningServlet should now work for general use. It uses the default
 * keystore. Will add configurability later. It also uses the GUIDialogAgent.
 *
 * Revision 1.2  2003/11/21 04:43:41  pelle
 * EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
 * Otherwise You will Finaliate.
 * Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
 * This should hopefully make everything more stable (and secure).
 *
 * Revision 1.1  2003/11/11 21:17:48  pelle
 * Further vital reshuffling.
 * org.neudist.crypto.* and org.neudist.utils.* have been moved to respective areas under org.neuclear.commons
 * org.neuclear.signers.* as well as org.neuclear.passphraseagents have been moved under org.neuclear.commons.crypto as well.
 * Did a bit of work on the Canonicalizer and changed a few other minor bits.
 *
 * Revision 1.3  2003/11/09 03:27:09  pelle
 * More house keeping and shuffling about mainly pay
 *
 * Revision 1.2  2003/09/26 23:52:47  pelle
 * Changes mainly in receiver and related fun.
 * First real neuclear stuff in the payment package. Added TransferContract and AssetControllerReceiver.
 *
 * Revision 1.1  2003/01/18 18:12:30  pelle
 * First Independent commit of the Independent XML-Signature API for NeuDist.
 *
 * Revision 1.2  2002/09/25 19:20:15  pelle
 * Added various new schemas and updated most of the existing ones.
 * Added explanation interface for explaining the purpose of a
 * SignedNamedObject to a user. We may want to use XSL instead.
 * Also made the signing webapp look a bit nicer.
 *
 * Revision 1.1  2002/09/21 23:11:16  pelle
 * A bunch of clean ups. Got rid of as many hard coded URL's as I could.
 *
 */
package org.neuclear.commons.servlets;

import org.neuclear.commons.Utility;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

/**
 * @author pelleb
 * @version $Revision: 1.10 $
 */
public final class ServletTools {
    private ServletTools() {
    }

    public static String getAbsoluteURL(final HttpServletRequest request, final String item) {
        return request.getScheme() + "://" + request.getServerName() +
                ((request.getServerPort() == 80) ? "" : ":" + Integer.toString(request.getServerPort()))
                + request.getContextPath() + item;
    }

    public static void printHeader(final PrintWriter out, final HttpServletRequest request, final String title, final String sub) {
        out.print("<html><head><title>");
        out.print(title);
        out.println("</title>");
        out.println("<LINK rel=\"stylesheet\" type=\"text/css\" href=\"");
        out.println(getAbsoluteURL(request, "/styles.css"));
        out.println("\">");
        out.println("</head><body><div id=\"banner\">");
        out.println(title);
        out.println("</div>\n<div id=\"subtitle\">");
        out.println(sub);
        out.println("</div>\n");


    }

    public static void printHeader(final PrintWriter out, final HttpServletRequest request, final String title) {
        out.print("<html><head><title>");
        out.print(title);
        out.println("</title>");
        out.println("<style type=\"text/css\">");
        out.println("body, th, td, input, select, textarea, h2 small {\n font-family: Verdana, Helvetica, Arial, sans-serif;\n }\n code, pre {\n font-family: 'Andale Mono', Courier, monospace;\n font-size: small;\n background-color: lightgrey;\n}");
        out.println("</head><body bgcolor=\"#FFFFFF\"><div id=\"banner\"><table bgcolor=\"#0000ff\" width=\"100%\"><tr><td><h3 style=\"color: white\">");
        out.println(title);
        out.println("</h3></td></tr></table></div>");


    }

    public static String getInitParam(String name, ServletConfig config) {
        return Utility.denullString(config.getInitParameter(name), config.getServletContext().getInitParameter(name));
    }
}
