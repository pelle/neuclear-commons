package org.neuclear.commons.swing;

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
import org.neuclear.commons.crypto.passphraseagents.AgentMessages;
import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;
import org.neuclear.commons.crypto.passphraseagents.icons.IconTools;
import org.neuclear.commons.crypto.passphraseagents.swing.MessageLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * User: pelleb
 * Date: May 14, 2004
 * Time: 12:11:53 PM
 */
public abstract class NeuClearDialog extends JDialog {
    public NeuClearDialog(Frame frame, final String name) {
        this(frame, name, IconTools.getOK());
    }

    public NeuClearDialog(Frame frame, final String id, Icon okIcon) throws HeadlessException {
        super(frame, true);
        this.id = id;
        SwingTools.setLAF();
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(AgentMessages.getTitle(id));
        ok = new JButton(AgentMessages.getOK(id));
        ok.setToolTipText(AgentMessages.getOKToolTip(id));
        ok.setIcon(okIcon);
        ok.setEnabled(false);
        cancel = new JButton(AgentMessages.getText("cancel"));
        cancel.setIcon(IconTools.getCancel());
        message = new MessageLabel();
        banner = new com.l2fprod.common.swing.BannerPanel();
        banner.setIcon(IconTools.getLogo());
        banner.setTitle(AgentMessages.getTitle(id));
        banner.setSubtitle(AgentMessages.getDescription(id));
        contents = new JPanel();
        contents.setLayout(new BorderLayout());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(contents, BorderLayout.CENTER);
        contents.add(banner, BorderLayout.NORTH);
        contents.add(buildPanel(), BorderLayout.CENTER);
        contents.add(buildButtonBar(), BorderLayout.SOUTH);
        pack();
        setResizable(false);

        closeAction = new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                clear();
                hide();
                runner.cancel();

            }
        };
        cancel.addActionListener(closeAction);

        okAction = new ActionListener() {
            public void actionPerformed(final ActionEvent actionEvent) {
                if (validateForm()) {
                    runner.execute();
                }

            }
        };

        ok.addActionListener(okAction);
        keyValidator = new KeyListener() {
            public void keyPressed(KeyEvent e) {

            }

            public void keyReleased(KeyEvent e) {
                ok.setEnabled(validateForm());
            }

            public void keyTyped(KeyEvent e) {

            }
        };
        ((JComponent) contents).registerKeyboardAction(closeAction,
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                clear();
                runner.cancel();
            }
        });


    }

    protected void clear() {
    }

    abstract protected boolean validateForm();

    abstract protected Component buildPanel();

    protected JPanel buildButtonBar() {
        ButtonBarBuilder bb = new ButtonBarBuilder();
        addExtraButtons(bb);
        bb.addGlue();
        bb.addUnrelatedGap();
        bb.addGridded(ok);
        bb.addGridded(cancel);
        final JPanel buttonpanel = bb.getPanel();
        final JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(buttonpanel, BorderLayout.CENTER);
        panel.add(message, BorderLayout.NORTH);
        return panel;
    }

    protected void addExtraButtons(ButtonBarBuilder bb) {

    }

    protected void open() {
        switchToMain();
        initializeForm();
        pack();
        message.clear();
        com.l2fprod.common.swing.UIUtilities.centerOnScreen(dia);
        show();
        toFront();
    }

    protected void initializeForm() {

    }

    protected Object openAndWait(WaitForInput wait) throws UserCancellationException {
        runner = wait;
        new Thread(wait).start();
        return wait.getResult();
    }

    protected void switchToMain() {
        getContentPane().removeAll();
        getContentPane().add(contents);
        pack();
    }


    private final com.l2fprod.common.swing.BannerPanel banner;
    protected final JButton ok;
    private final JButton cancel;
    protected final MessageLabel message;
    protected WaitForInput runner;
    protected Dialog dia = this;


    protected JPanel contents;
    protected KeyListener keyValidator;
    protected ActionListener closeAction;
    protected ActionListener okAction;
    protected final String id;


}

