package org.neuclear.commons.crypto.signers;

import org.neuclear.commons.Utility;
import org.neuclear.commons.NeuClearException;
import org.neuclear.commons.servlets.ServletTools;
import org.neuclear.commons.crypto.CryptoTools;
import org.neuclear.commons.crypto.passphraseagents.*;

import javax.servlet.ServletConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.Collections;
import java.io.FileNotFoundException;
import java.security.GeneralSecurityException;

/**
 * Used to create Signers from servlet configuration parameters. It keeps a cache of Signers with similar parameters. Thus
 * if you have several Servlets with the same keystore parameters they will use the same instance of Signer<p>
 * The Configuration parameters are as follows:
 * <table border="2"><tr><th>parameter name</th><th>parameter value</th></tr>
 * <tr><td>keystore</td><td>The location of the JCE KeyStore. Defaults to the file .keystore in the users home directory
 * If you specify <tt>test</tt> the built in Test keystore will be used.</td></tr>
 * <tr><td>serviceid</td><td>The main service ID of the service. Ie. neu://superbux.com/ecurrency. This is only required (and used)
 * if you set <tt>keeppassphrase</tt> (see below)</td></tr>
 * <tr><td>passphraseagent</td><td>The type of passphraseagent to use. Valid options are <tt>servlet</tt>,
 * <tt>gui</tt>(default) and <tt>console</tt></td></tr>
 * <tr><td>keeppassphrase</td><td>This asks for the service passphrase once at startup and remembers it through the lifetime of the servlet</td></tr>
 * </table>
 * <p>
 * To use the factory. Do as follows within your servlets init() method:
 * <code>Signer signer=ServletSignerFactory.getInstance().createSigner(config);</code>
 *
 * @see PassPhraseAgent
 * @see Signer
 */
public final class ServletSignerFactory {

    private ServletSignerFactory() {
        map=Collections.synchronizedMap(new HashMap());
    }
    public synchronized Signer createSigner(ServletConfig config) throws FileNotFoundException, GeneralSecurityException, NeuClearException {
        final String keystore=ServletTools.getInitParam("keystore",config);
        final String keeppassphrase=ServletTools.getInitParam("keeppassphrase",config);
        final String agenttype=ServletTools.getInitParam("passphraseagent",config);
        final String serviceid = ServletTools.getInitParam("serviceid",config);
        final String hash = getConfigHash(keystore, keeppassphrase, agenttype,serviceid);
        if (map.containsKey(hash))
            return (Signer)map.get(hash);

        final PassPhraseAgent coreagent=getAgent(agenttype);
        final PassPhraseAgent agent=createWrapperAgent(keeppassphrase, coreagent, serviceid);
        // If keystore is "test" setup the TestCaseSigner otherwise use the JCESigner
        final Signer signer=createSigner(keystore, agent);
        map.put(hash,signer);
        return signer;
    }

    private static final PassPhraseAgent createWrapperAgent(final String keeppassphrase, final PassPhraseAgent coreagent, final String serviceid) throws UserCancelsException {
        if (!Utility.isEmpty(keeppassphrase)&&keeppassphrase.equals("1")&&coreagent instanceof InteractiveAgent)
            return new AskAtStartupAgent((InteractiveAgent)coreagent,serviceid);
        return coreagent;
    }

    private static final JCESigner createSigner(final String keystore, final PassPhraseAgent agent) throws GeneralSecurityException, NeuClearException, FileNotFoundException {
        if (!Utility.isEmpty(keystore)){
            if (keystore.toLowerCase().equals("test"))
                return new TestCaseSigner(agent);

            if (!keystore.toLowerCase().equals("default"))
                return new JCESigner(keystore,"jks", "SUN",agent);
        }
        return new DefaultSigner(agent);
    }

    private static final PassPhraseAgent getAgent(final String agenttype) {
        if (!Utility.isEmpty(agenttype)){
            if (agenttype.toLowerCase().equals("console"))
                return new CommandLineAgent();
            if (agenttype.toLowerCase().equals("servlet"))
                return new ServletPassPhraseAgent();
            if (agenttype.toLowerCase().equals("test"))
                return new AlwaysTheSamePassphraseAgent("neuclear");
        }
        return new GuiDialogAgent();  //The default DialogAgent
    }

    private static final String getConfigHash(final String keystore, final String keeppassphrase, final String agenttype,final String serviceid) {
        return new String(CryptoTools.digest((keystore+keeppassphrase+agenttype).getBytes()));
    }

    public synchronized static ServletSignerFactory getInstance(){
        if (instance==null)
            instance=new ServletSignerFactory();
        return instance;
    }

    private static ServletSignerFactory instance;
    final private Map map;
}
