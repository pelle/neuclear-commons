package org.neuclear.commons.crypto.passphraseagents.icons;

import javax.swing.*;
import java.net.URL;

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
 * Time: 11:12:40 AM
 */
public class IconTools {


    public static Icon getInfo() {
        if (INFO == null)
            INFO = loadIcon("org/neuclear/commons/crypto/passphraseagents/icons/info.png");
        return INFO;
    }

    public static Icon getInvalid() {
        if (INVALID == null)
            INVALID = loadIcon("org/neuclear/commons/crypto/passphraseagents/icons/invalid.png");
        return INVALID;
    }

    public static Icon getError() {
        if (ERROR == null)
            ERROR = loadIcon("org/neuclear/commons/crypto/passphraseagents/icons/error.png");
        return ERROR;
    }

    public static Icon getPersonality() {
        if (PERSONALITY == null)
            PERSONALITY = loadIcon("org/neuclear/commons/crypto/passphraseagents/icons/personality.png");
        return PERSONALITY;
    }

    public static Icon getPassword() {
        if (PASSWORD == null)
            PASSWORD = loadIcon("org/neuclear/commons/crypto/passphraseagents/icons/password.png");
        return PASSWORD;
    }

    public static Icon getPersonalities() {
        if (PERSONALITIES == null)
            PERSONALITIES = loadIcon("org/neuclear/commons/crypto/passphraseagents/icons/personalities.png");
        return PERSONALITIES;
    }

    public static Icon getAddPersonality() {
        if (PERSONALITY_ADD == null)
            PERSONALITY_ADD = loadIcon("org/neuclear/commons/crypto/passphraseagents/icons/personality_add.png");
        return PERSONALITY_ADD;
    }

    public static Icon getOK() {
        if (OK == null)
            OK = loadIcon("org/neuclear/commons/crypto/passphraseagents/icons/ok.png");
        return OK;
    }

    public static Icon getCancel() {
        if (CANCEL == null)
            CANCEL = loadIcon("org/neuclear/commons/crypto/passphraseagents/icons/cancel.png");
        return CANCEL;
    }

    public static Icon getSign() {
        if (SIGN == null)
            SIGN = loadIcon("org/neuclear/commons/crypto/passphraseagents/icons/sign.png");
        return SIGN;
    }

    public static Icon getSave() {
        if (SAVE == null)
            SAVE = loadIcon("org/neuclear/commons/crypto/passphraseagents/icons/filesave.png");
        return SAVE;
    }

    public static Icon getSaveAs() {
        if (SAVEAS == null)
            SAVEAS = loadIcon("org/neuclear/commons/crypto/passphraseagents/icons/filesaveas.png");
        return SAVEAS;
    }

    public static Icon getOpen() {
        if (OPEN == null)
            OPEN = loadIcon("org/neuclear/commons/crypto/passphraseagents/icons/fileopen.png");
        return OPEN;
    }

    public static Icon getHelp() {
        if (HELP == null)
            HELP = loadIcon("org/neuclear/commons/crypto/passphraseagents/icons/help.png");
        return HELP;
    }

    public static ImageIcon getLogo() {
        if (LOGO == null)
            LOGO = loadIcon("org/neuclear/commons/crypto/passphraseagents/neuclear.png");
        return LOGO;
    }

    public static ImageIcon loadIcon(String name) {
        return loadIcon(IconTools.class, name);
    }

    public static ImageIcon loadIcon(Class cls, String name) {
        final URL imageurl = cls.getClassLoader().getResource(name);
        if (imageurl != null) {
            final ImageIcon icon = new ImageIcon(imageurl);

            return icon;
        }
        return null;
    }

    private static Icon INFO;
    private static Icon INVALID;
    private static Icon ERROR;

    private static Icon PERSONALITY;
    private static Icon PERSONALITIES;
    private static Icon PERSONALITY_ADD;

    private static Icon PASSWORD;

    private static Icon OK;
    private static Icon CANCEL;
    private static Icon SIGN;

    private static Icon SAVE;
    private static Icon SAVEAS;
    private static Icon OPEN;

    private static Icon HELP;
    private static ImageIcon LOGO;

}
