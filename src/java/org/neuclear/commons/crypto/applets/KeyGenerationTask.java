package org.neuclear.commons.crypto.applets;

import java.security.KeyPairGenerator;
import java.security.KeyPair;

/**
 * (C) 2003 Antilles Software Ventures SA
 * User: pelleb
 * Date: Nov 19, 2003
 * Time: 9:18:12 AM
 * $Id: KeyGenerationTask.java,v 1.1 2003/11/19 14:37:37 pelle Exp $
 * $Log: KeyGenerationTask.java,v $
 * Revision 1.1  2003/11/19 14:37:37  pelle
 * CommandLineAgent now masks the passphrase input using the JLine library which is now a dependency.
 * And the beginnings of a KeyGeneratorApplet
 *
 */
public class KeyGenerationTask implements Runnable{
    public KeyGenerationTask(KeyPairGenerator kpg,KeyGeneratorApplet applet) {
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
    public void run() {
        KeyPair kp=kpg.generateKeyPair();
        applet.setKp(kp);
    }

    private final KeyGeneratorApplet applet;
    private final KeyPairGenerator kpg;
}
