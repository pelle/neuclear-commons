package org.neuclear.commons.configuration;

import org.picocontainer.MutablePicoContainer;
import org.neuclear.commons.sql.ConnectionSource;
import org.neuclear.commons.sql.DefaultConnectionSource;
import org.neuclear.commons.sql.DefaultXAConnectionSource;
import org.neuclear.commons.sql.entities.drivers.DDLDriver;
import org.neuclear.commons.sql.entities.drivers.HSQLDriver;
import org.neuclear.commons.crypto.passphraseagents.PassPhraseAgent;
import org.neuclear.commons.crypto.passphraseagents.GuiDialogAgent;
import org.neuclear.commons.crypto.signers.Signer;
import org.neuclear.commons.crypto.signers.DefaultSigner;

/**
 * Created by IntelliJ IDEA.
 * User: pelleb
 * Date: Dec 31, 2003
 * Time: 10:53:02 AM
 * To change this template use Options | File Templates.
 */
public class DefaultConfiguration implements Configuration {
    public void configure(MutablePicoContainer pico) {
        System.out.println("Loading Default Configuration");
        pico.registerComponentImplementation(ConnectionSource.class,DefaultXAConnectionSource.class);
        pico.registerComponentImplementation(DDLDriver.class,HSQLDriver.class);
        pico.registerComponentImplementation(PassPhraseAgent.class,GuiDialogAgent.class);
        pico.registerComponentImplementation(Signer.class,DefaultSigner.class);
    }
}
