package org.neuclear.commons.crypto.passphraseagents;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*
NeuClear Distributed Transaction Clearing Platform
(C) 2003 Pelle Braendgaard

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

$Id: GuiDialogAgent.java,v 1.2 2003/11/21 04:43:41 pelle Exp $
$Log: GuiDialogAgent.java,v $
Revision 1.2  2003/11/21 04:43:41  pelle
EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
Otherwise You will Finaliate.
Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
This should hopefully make everything more stable (and secure).

Revision 1.1  2003/11/11 21:17:46  pelle
Further vital reshuffling.
org.neudist.crypto.* and org.neudist.utils.* have been moved to respective areas under org.neuclear.commons
org.neuclear.signers.* as well as org.neuclear.passphraseagents have been moved under org.neuclear.commons.crypto as well.
Did a bit of work on the Canonicalizer and changed a few other minor bits.

Revision 1.3  2003/11/05 23:40:21  pelle
A few minor fixes to make all the unit tests work
Also the start of getting SigningServlet and friends back working.

Revision 1.2  2003/10/31 23:58:53  pelle
The IdentityCreator now fully works with the new Signer architecture.

Revision 1.1  2003/10/29 21:16:27  pelle
Refactored the whole signing process. Now we have an interface called Signer which is the old SignerStore.
To use it you pass a byte array and an alias. The sign method then returns the signature.
If a Signer needs a passphrase it uses a PassPhraseAgent to present a dialogue box, read it from a command line etc.
This new Signer pattern allows us to use secure signing hardware such as N-Cipher in the future for server applications as well
as SmartCards for end user applications.

Revision 1.1  2003/10/28 23:44:03  pelle
The GuiDialogAgent now works. It simply presents itself as a simple modal dialog box asking for a passphrase.
The two Signer implementations both use it for the passphrase.

*/

/**
 * User: pelleb
 * Date: Oct 27, 2003
 * Time: 5:49:14 PM
 */
public final class GuiDialogAgent implements InteractiveAgent {
    public GuiDialogAgent() {
        frame = new Frame("Please Enter Passphrase...");

        frame.setVisible(false);
        frame.setSize(200, 100);
        final Panel panel = new Panel();
        panel.setLayout(new BorderLayout());
        frame.add(panel);
        final Panel text = new Panel(new FlowLayout());
        panel.add(text, BorderLayout.NORTH);


        try {
            img = Toolkit.getDefaultToolkit().getImage(this.getClass().getClassLoader().getResource("org/neuclear/commons/crypto/passphraseagents/neuclear.png"));
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

        text.add(new Label("Name: "));
        nameLabel = new Label();
        nameLabel.setForeground(Color.blue);

        text.add(nameLabel);
        passphrase = new TextField();
        passphrase.setEchoChar('*');
        panel.add(passphrase, BorderLayout.CENTER);
        final Panel buttons = new Panel(new FlowLayout());
        panel.add(buttons, BorderLayout.SOUTH);
        ok = new Button("Sign");
        buttons.add(ok);
        final Button cancel = new Button("Cancel");
        buttons.add(cancel);
        cancel.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                synchronized (passphrase) {
                    passphrase.setText("");
                    passphrase.notifyAll();
                }

            }
        });

        final ActionListener action = new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                synchronized (passphrase) {
                    passphrase.notifyAll();
                }

            }
        };
        ok.addActionListener(action);
        passphrase.addActionListener(action);

    }

    public char[] getPassPhrase(final String name) {
        synchronized (passphrase) {
            passphrase.setText("");
            nameLabel.setText(name);
            frame.pack();
            frame.setVisible(true);
            try {
                passphrase.wait();
            } catch (InterruptedException e) {
                ;
            }
            frame.setVisible(false);
            final String phrase = passphrase.getText();
            passphrase.setText("");
            return phrase.toCharArray();
        }
    }

    public static void main(final String[] args) {
        final PassPhraseAgent dia = new GuiDialogAgent();
        System.out.println("Getting passphrase... " + new String(dia.getPassPhrase("neu://pelle@test")));
        System.out.println("Getting passphrase... " + new String(dia.getPassPhrase("neu://pelle@test")));

        System.exit(0);
    }

    private final TextField passphrase;
    private final Button ok;
    private final Label nameLabel;
    private final Frame frame;
    private Image img;


}
