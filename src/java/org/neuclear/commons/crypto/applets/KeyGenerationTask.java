package org.neuclear.commons.crypto.applets;

import java.security.KeyPairGenerator;
import java.security.KeyPair;

/**
 * (C) 2003 Antilles Software Ventures SA
 * User: pelleb
 * Date: Nov 19, 2003
 * Time: 9:18:12 AM
 * $Id: KeyGenerationTask.java,v 1.3 2003/12/19 00:31:15 pelle Exp $
 * $Log: KeyGenerationTask.java,v $
 * Revision 1.3  2003/12/19 00:31:15  pelle
 * Lots of usability changes through out all the passphrase agents and end user tools.
 *
 * Revision 1.2  2003/11/21 04:43:40  pelle
 * EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
 * Otherwise You will Finaliate.
 * Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
 * This should hopefully make everything more stable (and secure).
 *
 * Revision 1.1  2003/11/19 14:37:37  pelle
 * ConsoleAgent now masks the passphrase input using the JLine library which is now a dependency.
 * And the beginnings of a KeyGeneratorApplet
 *
 */
public final class KeyGenerationTask implements Runnable{
    public KeyGenerationTask(final KeyPairGenerator kpg,final KeyGeneratorApplet applet) {
        this.kpg = kpg;
        this.applet=applet;
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see     Thread#run()
     */
    public final void run() {
        final KeyPair kp=kpg.generateKeyPair();
        applet.setKp(kp);
    }

    private final KeyGeneratorApplet applet;
    private final KeyPairGenerator kpg;
}
