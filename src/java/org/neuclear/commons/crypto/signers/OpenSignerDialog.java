package org.neuclear.commons.crypto.signers;

/*
 *  The NeuClear Project and it's libraries are
 *  (c) 2002-2004 Antilles Software Ventures SA
 *  For more information see: http://neuclear.org
 *
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.plaf.Options;
import org.neuclear.commons.crypto.CryptoTools;
import org.neuclear.commons.crypto.passphraseagents.GuiDialogAgent;
import org.neuclear.commons.crypto.passphraseagents.InteractiveAgent;
import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;
import org.neuclear.commons.crypto.passphraseagents.icons.IconTools;
import org.neuclear.commons.crypto.passphraseagents.swing.JKSFilter;
import org.neuclear.commons.crypto.passphraseagents.swing.MessageLabel;
import org.neuclear.commons.crypto.passphraseagents.swing.NewPassphraseDialog;
import org.neuclear.commons.swing.WaitForInput;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.prefs.Preferences;

/**
 * User: pelleb
 * Date: May 14, 2004
 * Time: 12:11:53 PM
 */
public class OpenSignerDialog extends JDialog {
    public OpenSignerDialog(Frame frame, InteractiveAgent agent) throws HeadlessException {
        super(frame, TITLE, true);
        try {
            if (UIManager.getSystemLookAndFeelClassName().equals("apple.laf.AquaLookAndFeel"))
                System.setProperty("com.apple.laf.useScreenMenuBar", "true");
            else {
                UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
                UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, Boolean.TRUE);
            }
        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }

        this.agent = agent;
        prefs = Preferences.userNodeForPackage(PersonalSigner.class);
        filename = prefs.get(KEYSTORE, CryptoTools.DEFAULT_KEYSTORE);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        find = new JButton(IconTools.getOpen());
        ok = new JButton("Open");
        ok.setIcon(IconTools.getOK());
        ok.setEnabled(false);
        cancel = new JButton("Cancel");
        cancel.setIcon(IconTools.getCancel());
        filefield = new JTextField(filename);
        passphrase = new JPasswordField();

        message = new MessageLabel();
        banner = new com.l2fprod.common.swing.BannerPanel();
        banner.setIcon(IconTools.getLogo());
        banner.setTitle(TITLE);
        banner.setSubtitle("Select a <b>Personality</b> file or leave as is. Enter your <b>password</b> and youre in.");
        Container contents = getContentPane();
//        contents.setLayout(new BorderLayout());
//        contents.add(banner,BorderLayout.NORTH);
        contents.add(buildPanel());
        pack();
        setResizable(false);

        final ActionListener cancelOrClose = new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                synchronized (passphrase) {
                    passphrase.setText("");
                    hide();
                    runner.cancel();
                }

            }
        };
        cancel.addActionListener(cancelOrClose);

        final ActionListener action = new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                synchronized (passphrase) {
                    if (validateForm()) {
                        runner.execute();
                    }
                }

            }
        };

        ok.addActionListener(action);
        passphrase.addActionListener(action);
        final KeyListener validate = new KeyListener() {
            public void keyPressed(KeyEvent e) {

            }

            public void keyReleased(KeyEvent e) {
                ok.setEnabled(validateForm());
                if (e.getSource().equals(filefield))
                    updateOKText();
            }

            public void keyTyped(KeyEvent e) {

            }
        };
        passphrase.addKeyListener(validate);
        filefield.addKeyListener(validate);
        ((JComponent) contents).registerKeyboardAction(cancelOrClose,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                runner.cancel();
            }
        });
        find.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                prepFileChooser(filefield.getText(), "Select Personalities File");
                int result = fc.showOpenDialog(dia);
                if (result == JFileChooser.APPROVE_OPTION)
                    filefield.setText(fc.getSelectedFile().getAbsolutePath());
                passphrase.requestFocus();
            }

        });


    }

    private void updateOKText() {
        File file = new File(filefield.getText());
        if (file.exists())
            ok.setText("Open");
        else
            ok.setText("Create");
    }

    private boolean validateForm() {
        return (passphrase.getPassword().length > 0);
    }

    private void prepFileChooser(String def, String title) {
        File file = new File(def);
        if (fc == null) {
            fc = new JFileChooser();
            fc.setFileFilter(new JKSFilter());
        }

        fc.setCurrentDirectory(file.getParentFile());
        fc.setSelectedFile(file);
        fc.setDialogTitle(title);
    }

    private Component buildPanel() {
        FormLayout layout = new FormLayout("right:pref, 3dlu, pref:grow,1dlu,pref ",
                "pref,3dlu,pref,3dlu,pref, 3dlu,pref,  5dlu:grow, pref");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.setDefaultDialogBorder();

        builder.add(banner, cc.xyw(1, 1, 5));
        builder.addLabel("Personalities File:", cc.xy(1, 3)).setLabelFor(filefield);
        builder.add(filefield, cc.xy(3, 3));
        builder.add(find, cc.xy(5, 3));

        final JLabel label = builder.addLabel("Passphrase:", cc.xy(1, 5));
        label.setLabelFor(passphrase);
        label.setIcon(IconTools.getPassword());
        builder.add(passphrase, cc.xyw(3, 5, 3));
        builder.add(message, cc.xyw(1, 7, 5));


        ButtonBarBuilder bb = new ButtonBarBuilder();
        bb.addGlue();
        bb.addUnrelatedGap();
        bb.addGridded(ok);
        bb.addGridded(cancel);
        builder.add(bb.getPanel(), cc.xyw(1, 9, 5));

        return builder.getPanel();
    }

    private void open() {
        pack();
        filefield.setText(filename);
        updateOKText();
        message.clear();
        com.l2fprod.common.swing.UIUtilities.centerOnScreen(dia);
        passphrase.requestFocus();
        show();
        toFront();
    }

    public BrowsableSigner openSigner() throws UserCancellationException {
        WaitForInput wait = new DialogRunner();
        new Thread(wait).start();

        return (BrowsableSigner) wait.getResult();
    }

    public void save(JCESigner signer, boolean force) throws UserCancellationException {
        if (!force) {
            prepFileChooser(filename, "Select Personalities File");
            int result = fc.showSaveDialog(dia);
            if (result == JFileChooser.APPROVE_OPTION)
                filename = fc.getSelectedFile().getAbsolutePath();
            else
                throw new UserCancellationException(filename);
            if (npd == null)
                npd = new NewPassphraseDialog();
            WaitForInput wait = npd.createGetNewPassphraseTask(filename);
            new Thread(wait).start();
            storedPassphrase = (char[]) wait.getResult();
        }
        try {
            signer.save(filename, storedPassphrase);
        } catch (FileNotFoundException e) {
            save(signer, false);
        }
    }

    private final com.l2fprod.common.swing.BannerPanel banner;
    private final JTextField filefield;
    private final JPasswordField passphrase;
    private char storedPassphrase[] = null;
    private final JButton find;
    private final JButton ok;
    private final JButton cancel;
    private final MessageLabel message;
    private JFileChooser fc;
    private WaitForInput runner;
    private NewPassphraseDialog npd;

    private String filename;
    private Preferences prefs;

    private final InteractiveAgent agent;
    private Dialog dia = this;
    public static final String TITLE = "Open Personalities File";
    private static final String KEYSTORE = "KEYSTORE";

    class DialogRunner extends WaitForInput {
        public DialogRunner() {
        }

        public void run() {
            runner = this;
            open();
//            if (incorrect)


        }

        public void execute() {
            final char[] phrase = passphrase.getPassword();
            try {
                BrowsableSigner signer = new JCESigner(filefield.getText(), "JKS", "SUN", agent, phrase);
                prefs.put(KEYSTORE, filefield.getText());
                filename = filefield.getText();
                hide();
                storedPassphrase = passphrase.getPassword();
                passphrase.setText("");
                setResult(signer);
            } catch (InvalidPassphraseException e) {
                message.invalid("You entered an invalid passphrase. Try again...");
                return;
            }
        }


    }

    public static void main(String args[]) {
        JFrame frame = new JFrame("test");

        OpenSignerDialog dialog = new OpenSignerDialog(frame, new GuiDialogAgent());
        try {
            BrowsableSigner signer = dialog.openSigner();
        } catch (UserCancellationException e) {
            System.out.println("User Cancelled");
        }
        System.exit(0);
    }


}

