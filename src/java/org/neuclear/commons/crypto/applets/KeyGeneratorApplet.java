package org.neuclear.commons.crypto.applets;

import org.neuclear.commons.crypto.passphraseagents.GuiDialogAgent;

import java.applet.Applet;
import java.util.Random;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * (C) 2003 Antilles Software Ventures SA
 * User: pelleb
 * Date: Nov 19, 2003
 * Time: 8:57:03 AM
 * $Id: KeyGeneratorApplet.java,v 1.3 2003/12/19 00:31:15 pelle Exp $
 * $Log: KeyGeneratorApplet.java,v $
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
public final class KeyGeneratorApplet extends Applet {
    /**
     * Called by the browser or applet viewer to inform
     * this applet that it should start its execution. It is called after
     * the <code>init</code> method and each time the applet is revisited
     * in a Web page.
     * <p>
     * A subclass of <code>Applet</code> should override this method if
     * it has any operation that it wants to perform each time the Web
     * page containing it is visited. For example, an applet with
     * animation might want to use the <code>start</code> method to
     * resume animation, and the <code>stop</code> method to suspend the
     * animation.
     * <p>
     * The implementation of this method provided by the
     * <code>Applet</code> class does nothing.
     *
     * @see     Applet#destroy()
     * @see     Applet#init()
     * @see     Applet#stop()
     */
    public final void start() {
        // TODO seed Random NumberGenerator
        statusLabel.setText("Click OK to Start Key Generation");

    }

    /**
     * Called by the browser or applet viewer to inform
     * this applet that it has been loaded into the system. It is always
     * called before the first time that the <code>start</code> method is
     * called.
     * <p>
     * A subclass of <code>Applet</code> should override this method if
     * it has initialization to perform. For example, an applet with
     * threads would use the <code>init</code> method to create the
     * threads and the <code>destroy</code> method to kill them.
     * <p>
     * The implementation of this method provided by the
     * <code>Applet</code> class does nothing.
     *
     * @see     Applet#destroy()
     * @see     Applet#start()
     * @see     Applet#stop()
     */
    public final void init() {
        try {
            random=SecureRandom.getInstance("SHA1PRNG");
            kpg=KeyPairGenerator.getInstance("RSA");
            kpg.initialize(1024,random);
        } catch (NoSuchAlgorithmException e) {

        }
        agent=new GuiDialogAgent();
        final Panel panel = new Panel();
        panel.setLayout(new BorderLayout());
        add(panel);
        final Panel text = new Panel(new FlowLayout());
        panel.add(text, BorderLayout.NORTH);
        keygenTask=new Thread(new KeyGenerationTask(kpg,this));
        try {
            final Image img = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("org/neuclear/commons/crypto/passphraseagents/neuclear.png"));
            final Canvas canvas = new Canvas() {
                public void paint(final Graphics g) {
                    setSize(50, 50);
                    g.drawImage(img, 0, 0, this);

                }
            };
            canvas.setSize(50, 50);
            text.add(canvas);
        } catch (Throwable e) {
            ;
//        } catch (InterruptedException e) {
            ;//System.out.println("Couldn't load Image");
        }

        statusLabel = new Label();
        statusLabel.setForeground(Color.blue);

        text.add(statusLabel);
        final Panel buttons = new Panel(new FlowLayout());
        panel.add(buttons, BorderLayout.SOUTH);
        ok = new Button("OK");
        buttons.add(ok);
        final Button cancel = new Button("Cancel");
        buttons.add(cancel);
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                if (keygenTask!=null)
                    keygenTask.interrupt();
                ok.setEnabled(true);
            }
        });

        final ActionListener action = new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                ok.setEnabled(false);
                keygenTask.run();

            }
        };
        ok.addActionListener(action);
    }

    /**
     * Called by the browser or applet viewer to inform
     * this applet that it is being reclaimed and that it should destroy
     * any resources that it has allocated. The <code>stop</code> method
     * will always be called before <code>destroy</code>.
     * <p>
     * A subclass of <code>Applet</code> should override this method if
     * it has any operation that it wants to perform before it is
     * destroyed. For example, an applet with threads would use the
     * <code>init</code> method to create the threads and the
     * <code>destroy</code> method to kill them.
     * <p>
     * The implementation of this method provided by the
     * <code>Applet</code> class does nothing.
     *
     * @see     Applet#init()
     * @see     Applet#start()
     * @see     Applet#stop()
     */
    public final void destroy() {
        random=null;
    }

    final void setKp(final KeyPair kp) {
        publickey=kp.getPublic().getEncoded();
        privatekey = kp.getPrivate().getEncoded();
    }

    private SecureRandom random;
    private GuiDialogAgent agent;
    private KeyPairGenerator kpg;
    private KeyPair kp;
    private byte[] publickey;
    private byte[] privatekey;
    private Button ok;
    private Label statusLabel;
    private Thread keygenTask;

}
