package org.neuclear.commons.swing;

import com.jgoodies.forms.builder.ButtonBarBuilder;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.l2fprod.common.swing.BannerPanel;
import org.neuclear.commons.crypto.passphraseagents.AgentMessages;
import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;
import org.neuclear.commons.crypto.passphraseagents.icons.IconTools;

import javax.swing.*;
import java.awt.*;

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

/**
 * User: pelleb
 * Date: May 18, 2004
 * Time: 12:24:07 PM
 */
public abstract class ProcessDialog extends NeuClearDialog {
    public ProcessDialog(Frame frame, String id) {
        this(frame, id, IconTools.getOK());
    }

    public ProcessDialog(Frame frame, String id, Icon okIcon) throws HeadlessException {
        super(frame, id, okIcon);
        busyPanel = buildProcessPanel();
    }

    private JPanel buildProcessPanel() {

        FormLayout layout = new FormLayout("right:pref, 3dlu, 100dlu:grow ",
                "pref,3dlu,pref, 7dlu, pref");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.setDefaultDialogBorder();

        BannerPanel banner = new BannerPanel();
        banner.setTitle(AgentMessages.getProcessTitle(id));
        banner.setSubtitle(AgentMessages.getProcessDescription(id));
        builder.add(banner, cc.xyw(1, 1, 3));
        progress = new JProgressBar(0, 100);
        progress.setIndeterminate(true);
        progress.setVisible(true);

        builder.add(progress, cc.xyw(1, 3, 3));
        ButtonBarBuilder bb = new ButtonBarBuilder();
        bb.addGlue();
        bb.addUnrelatedGap();
//        bb.addGridded(ok);
        cancel = new JButton(AgentMessages.getText("cancel"));
        bb.addGridded(cancel);
        cancel.addActionListener(closeAction);
        builder.add(bb.getPanel(), cc.xyw(1, 5, 3));

        return builder.getPanel();

    }

    protected Object openAndWait(LongChildProcess process) throws UserCancellationException {
        final Object waiter = openAndWait(new ProcessRunner(this, process));
        clear();
        hide();
        return waiter;
    }

    private void switchToProcess() {
        getContentPane().removeAll();
        getContentPane().add(busyPanel);
        pack();
    }

    private class ProcessRunner extends AbstractDialogRunner {
        public ProcessRunner(ProcessDialog dia, Runnable process) {
            super(dia);
            this.process = process;
            if (process instanceof LongChildProcess)
                ((LongChildProcess) process).setParent(this);
        }

        private Runnable process;

        public void execute() {
            switchToProcess();
            new Thread(process).start();

        }


    }

    private final JPanel busyPanel;
    private JProgressBar progress;
    private JButton cancel;
}
