package org.neuclear.commons.crypto.passphraseagents;

import org.apache.cactus.ServletTestCase;
import org.apache.cactus.WebRequest;
import org.neuclear.commons.NeuClearException;
import org.neuclear.commons.crypto.signers.Signer;
import org.neuclear.commons.crypto.signers.ServletSignerFactory;
import org.neuclear.commons.crypto.signers.TestCaseSigner;

import javax.servlet.ServletException;
import java.security.GeneralSecurityException;
import java.io.IOException;


/**
 * Created by IntelliJ IDEA.
 * User: pelleb
 * Date: Dec 14, 2003
 * Time: 2:04:24 PM
 * To change this template use Options | File Templates.
 */
public class ServletPassPhraseAgentTest extends ServletTestCase {
    public void beginGetPassphrase(WebRequest theRequest) throws GeneralSecurityException, NeuClearException {
        theRequest.setURL("http://users.neuclear.org", "/test", "/Receiver",
                null, null);
        theRequest.addParameter("passphrase","test");
    }

    public void testGetPassphrase() throws ServletException, IOException, GeneralSecurityException, NeuClearException {
        MockPassphraseServlet servlet=new MockPassphraseServlet();
        servlet.init(config);
        servlet.service(request,response);
        assertNotNull(servlet.getPassPhrase("bla"));
        assertEquals("test",new String(servlet.getPassPhrase("bla")));
    }
    public void beginNoPassphrase(WebRequest theRequest) throws GeneralSecurityException, NeuClearException {
        theRequest.setURL("http://users.neuclear.org", "/test", "/Receiver",
                null, null);
    }

    public void testNoPassphrase() throws ServletException, IOException, GeneralSecurityException, NeuClearException {
        MockPassphraseServlet servlet=new MockPassphraseServlet();
        servlet.init(config);
        servlet.service(request,response);
        assertNull(servlet.getPassPhrase("bla"));
    }


}
