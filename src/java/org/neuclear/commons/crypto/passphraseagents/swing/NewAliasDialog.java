package org.neuclear.commons.crypto.passphraseagents.swing;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import javax.swing.*;
import java.awt.*;

/*
$Id: NewAliasDialog.java,v 1.1 2004/04/09 18:40:45 pelle Exp $
$Log: NewAliasDialog.java,v $
Revision 1.1  2004/04/09 18:40:45  pelle
BrowsableSigner now inherits Signer and PublicKeySource, which means implementations only need to implement BrowsableSigner now.
Added NewAliasDialog, which isnt yet complete.

*/

/**
 * User: pelleb
 * Date: Apr 8, 2004
 * Time: 5:58:38 PM
 */
public class NewAliasDialog {
    public NewAliasDialog() {
        ok = new JButton("Create");
        ok.setEnabled(false);
        cancel = new JButton("Cancel");
        alias = new JTextField();
        passphrase1 = new JPasswordField();
        passphrase2 = new JPasswordField();

        frame = new JFrame();
        frame.setTitle("NeuClear Signing Agent");
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(buildPanel());
        frame.pack();

    }

    private Component buildPanel() {
        FormLayout layout = new FormLayout("right:pref, 3dlu, pref:grow ",
                "pref,3dlu,pref, 3dlu, pref, 3dlu, pref, 7dlu, pref");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.setDefaultDialogBorder();

        builder.addSeparator("Create alias", cc.xyw(1, 1, 3));
        builder.addLabel("Alias:", cc.xy(1, 3));
        builder.add(alias, cc.xy(3, 3));
        builder.addLabel("Passphrase:", cc.xy(1, 5));
        builder.add(passphrase1, cc.xy(3, 5));
        builder.addLabel("(Repeat) Passphrase:", cc.xy(1, 7));
        builder.add(passphrase2, cc.xy(3, 7));

        ButtonBarBuilder bb = new ButtonBarBuilder();
        bb.addGlue();
        bb.addUnrelatedGap();
        bb.addGridded(ok);
        bb.addGridded(cancel);
        builder.add(bb.getPanel(), cc.xyw(1, 9, 3));

        return builder.getPanel();

    }

    private JFrame frame;
    private JButton ok;
    private JButton cancel;
    private JTextField alias;
    private JPasswordField passphrase1;
    private JPasswordField passphrase2;

    public static void main(String args[]) {
        NewAliasDialog dg = new NewAliasDialog();
        dg.frame.show();
    }
}
