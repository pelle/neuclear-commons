package org.neuclear.commons.configuration;

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
 * Date: Dec 31, 2003
 * Time: 11:40:56 AM
 * To change this template use Options | File Templates.
 */
public class ServletContextContainerTest extends ServletTestCase{
    public ServletContextContainerTest(String string) {
        super(string);
    }

    public void beginConfigDemo(WebRequest theRequest) throws GeneralSecurityException, NeuClearException {
        theRequest.setURL("http://users.neuclear.org", "/test", "/Receiver",
                null, null);
    }

    public void testConfigDemo() throws ServletException, IOException, GeneralSecurityException, NeuClearException {

//        config.setInitParameter("configurator","org.neuclear.commons.configuration.MockCactusConfiguration");
        MockConfiguredServlet servlet=new MockConfiguredServlet();
        servlet.init(config);
        assertNotNull(servlet.getPico());
        assertNotNull(servlet.getPico().getComponentInstance(org.neuclear.commons.crypto.passphraseagents.PassPhraseAgent.class));

    }

}
