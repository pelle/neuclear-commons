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

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;

import javax.swing.*;
import java.awt.*;

/**
 * User: pelleb
 * Date: May 18, 2004
 * Time: 11:55:51 AM
 */
public class SimpleProcessDialog extends ProcessDialog {
    public SimpleProcessDialog(Frame frame) throws HeadlessException {
        super(frame, "simple");
        input.addKeyListener(keyValidator);
        input.addActionListener(okAction);
    }


    protected boolean validateForm() {
        return input.getText().length() > 0;
    }

    protected Component buildPanel() {
        FormLayout layout = new FormLayout("right:pref, 3dlu, pref:grow ",
                "pref,5dlu:grow");
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();

        builder.setDefaultDialogBorder();
        input = new JTextField();

        builder.addLabel("Input", cc.xy(1, 1)).setLabelFor(input);
        builder.add(input, cc.xy(3, 1));
        return builder.getPanel();
    }

    protected void initializeForm() {
        input.requestFocus();
    }

    public String getInput() throws UserCancellationException {
        return (String) openAndWait(new SimpleProcessRunner());
    }

    private JTextField input;

    private class SimpleProcessRunner extends LongChildProcess {

        /**
         * When an object implementing interface <code>Runnable</code> is used
         * to create a thread, starting the thread causes the object's
         * <code>run</code> method to be called in that separately executing
         * thread.
         * <p/>
         * The general contract of the method <code>run</code> is that it may
         * take any action whatsoever.
         *
         * @see Thread#run()
         */
        public void run() {
            try {
                Thread.sleep(3000);
                parent.setResult(input.getText());
            } catch (InterruptedException e) {
                parent.cancel();
            }
        }

    }

    public static void main(String args[]) {
        SimpleProcessDialog dia = new SimpleProcessDialog(new JFrame());
        try {
            System.out.println(dia.getInput());
            System.out.println(dia.getInput());
        } catch (UserCancellationException e) {
            System.out.println("User Cancelled");
        }
        System.exit(0);
    }
}
