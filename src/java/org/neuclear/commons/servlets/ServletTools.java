/* $Id: ServletTools.java,v 1.1 2003/11/11 21:17:48 pelle Exp $
 * $Log: ServletTools.java,v $
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

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;

/**
 * @author pelleb
 * @version $Revision: 1.1 $
 **/
public class ServletTools {

    public static String getAbsoluteURL(HttpServletRequest request, String item) {
        return request.getScheme() + "://" + request.getServerName() +
                ((request.getServerPort() == 80) ? "" : ":" + Integer.toString(request.getServerPort()))
                + request.getContextPath() + item;
    }

    public static void printHeader(PrintWriter out, HttpServletRequest request, String title) {
        out.println("<html><head><title>NeuDist Local Signing Service</title>");
        //out.println("<LINK rel=\"STYLESHEET\" type=\"text/css\" href=\"style/neuclear.css\">");
        out.println("<style type=\"text/css\">");
        out.println("body, th, td, input, select, textarea, h2 small {\n font-family: Verdana, Helvetica, Arial, sans-serif;\n }\n code, pre {\n font-family: 'Andale Mono', Courier, monospace;\n font-size: small;\n background-color: lightgrey;\n}");
        out.println("</style></head><body bgcolor=\"#FFFFFF\"><div id=\"banner\"><table bgcolor=\"#026A32\" width=\"100%\"><tr><td><h3 style=\"color: white\">");
        out.println(title);
        out.println("</h3></td><td align=\"right\"><img src=\"images/logo.gif\"></td></tr></table></div>");


    }
}
