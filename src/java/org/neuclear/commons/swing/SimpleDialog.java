package org.neuclear.commons.swing;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import org.neuclear.commons.crypto.passphraseagents.UserCancellationException;

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
 * Time: 11:55:51 AM
 */
public class SimpleDialog extends NeuClearDialog {
    public SimpleDialog(Frame frame) throws HeadlessException {
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
        return (String) openAndWait(new SimpleDialogRunner(this));
    }

    private JTextField input;

    private class SimpleDialogRunner extends AbstractDialogRunner {
        public SimpleDialogRunner(NeuClearDialog dia) {
            super(dia);
        }

        public void execute() {
            setResult(input.getText());
        }

    }

    public static void main(String args[]) {
        SimpleDialog dia = new SimpleDialog(new JFrame());
        try {
            System.out.println(dia.getInput());
        } catch (UserCancellationException e) {
            System.out.println("User Cancelled");
        }
        System.exit(0);
    }
}
