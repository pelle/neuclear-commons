package org.neuclear.commons.crypto.passphraseagents.swing;

import com.jgoodies.plaf.Options;

import javax.swing.*;

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
 * Date: Apr 21, 2004
 * Time: 10:14:53 AM
 */
public class SwingTools {

    public static void setLAF() {
        try {
            if (!ismac) {
                UIManager.setLookAndFeel("com.jgoodies.plaf.plastic.PlasticXPLookAndFeel");
                UIManager.put(Options.USE_SYSTEM_FONTS_APP_KEY, Boolean.TRUE);
            }
            System.setProperty("com.apple.macos.useScreenMenuBar", "true");

        } catch (Exception e) {
            // Likely PlasticXP is not in the class path; ignore.
        }

    }

    private static boolean isMac() {
        return UIManager.getSystemLookAndFeelClassName().equals("apple.laf.AquaLookAndFeel");
    }

    private SwingTools() {
    };

    private static boolean ismac = isMac();
}
