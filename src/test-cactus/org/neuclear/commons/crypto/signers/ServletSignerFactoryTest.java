package org.neuclear.commons.crypto.signers;

import org.apache.cactus.ServletTestCase;
import org.apache.cactus.WebRequest;
import org.neuclear.commons.NeuClearException;

import javax.servlet.ServletException;
import java.security.GeneralSecurityException;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: pelleb
 * Date: Dec 14, 2003
 * Time: 12:56:31 PM
 * To change this template use Options | File Templates.
 */
public class ServletSignerFactoryTest extends ServletTestCase {
    public ServletSignerFactoryTest(String string) throws GeneralSecurityException, NeuClearException {
        super(string);
    }

    public void beginConfigDemo(WebRequest theRequest) throws GeneralSecurityException, NeuClearException {
        theRequest.setURL("http://users.neuclear.org", "/test", "/Receiver",
                null, null);
    }

    public void testConfigDemo() throws ServletException, IOException, GeneralSecurityException, NeuClearException {
        config.setInitParameter("keystore","test");
        config.setInitParameter("passphraseagent","signers");
        Signer signer=ServletSignerFactory.getInstance().createSigner(config);
        assertTrue(signer instanceof TestCaseSigner);
        assertEquals(signer, ServletSignerFactory.getInstance().createSigner(config));

    }

    public void beginConfigDefault(WebRequest theRequest) throws GeneralSecurityException, NeuClearException {
        theRequest.setURL("http://users.neuclear.org", "/test", "/Receiver",
                null, null);
    }

    public void testConfigDefault() throws ServletException, IOException, GeneralSecurityException, NeuClearException {
        config.setInitParameter("keystore","");
        config.setInitParameter("passphraseagent","gui");
        Signer signer=ServletSignerFactory.getInstance().createSigner(config);
        assertTrue(signer instanceof DefaultSigner);
        assertEquals(signer, ServletSignerFactory.getInstance().createSigner(config));

    }

}