package org.neuclear.commons.crypto.passphraseagents;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

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
 * Date: Apr 16, 2004
 * Time: 11:46:54 AM
 */
public class AgentMessages {
    /**
     */
    private AgentMessages() {
    }

    private static ResourceBundle bundle;

    /**
     */
    public synchronized static ResourceBundle getMessages() {
        if (bundle == null) {
            bundle = createBundle();
        }
        return bundle;
    }

    public static synchronized void updateBundle() {
        bundle = createBundle();
    }

    private static ResourceBundle createBundle() {
        return ResourceBundle.getBundle("cryptodialogs", getLocale(), AgentMessages.class.getClassLoader());
    }

    private static Preferences getPrefs() {
        return Preferences.userNodeForPackage(AgentMessages.class);
    }

    private static Locale getLocale() {
        Locale deflocale = Locale.getDefault();
        Preferences prefs = getPrefs();
        String cc = prefs.get("COUNTRY", deflocale.getCountry());
        String lang = prefs.get("LANGUAGE", deflocale.getLanguage());
//        String variant=prefs.get("LANGUAGE",deflocale.getVariant());

        return new Locale(lang, cc);
    }

    public static void updateLocale(String language, String country) {
        Preferences prefs = getPrefs();
        prefs.put("LANGUAGE", language);
        prefs.put("COUNTRY", country);
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            System.err.println(e.getLocalizedMessage());
        }
        updateBundle();
    }


}
