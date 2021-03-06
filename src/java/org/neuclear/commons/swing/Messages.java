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

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * User: pelleb
 * Date: Apr 16, 2004
 * Time: 11:46:54 AM
 */
public class Messages {
    /**
     */
    private Messages() {
        this(Messages.class, null, "cryptodialogs");
    }

    private Messages(Class ref, Messages parent, String name) {
        this.name = name;
        this.parent = parent;
        System.setProperty("file.encoding", "UTF-8");
        System.out.println("encoding: " + System.getProperty("file.encoding"));
        this.bundle = ResourceBundle.getBundle(name, getLocale(), ref.getClassLoader());
    }

    public final String getString(String key) {
        final String value = bundle.getString(key);
        if (value == null && parent != null)
            return parent.getString(key);
        return value;
    }

    private final String name;
    private final Messages parent;
    private final ResourceBundle bundle;

    private static Messages messages;
    private static Object lock = new Object();

    public static Messages getMessages() {
        synchronized (lock) {
            if (messages == null)
                messages = new Messages();
        }
        return messages;
    }

    public static void updateMessageRoot(Class ref, String name) {
        synchronized (lock) {
            messages = new Messages(ref, messages, name);
        }

    }

    public static String getTitle(String id) {
        return getComponentText(id, "title");
    }

    public static String getDescription(String id) {
        return getComponentText(id, "desc");
    }

    public static String getProcessTitle(String id) {
        return getTitle(id + ".process");
    }

    public static String getProcessDescription(String id) {
        return getDescription(id + ".process");
    }

    public static String getOK(String id) {
        final String text = getComponentText(id, "ok.title");
        if (text != null)
            return text;
        else
            return getText("ok");
    }

    public static String getOKToolTip(String id) {
        final String text = getComponentText(id, "ok.desc");
        if (text != null)
            return text;
        else
            return getOK("ok");
    }

    public static String getComponentText(String id, String part) {
        return getText(id + "." + part);
    }

    public static String getText(String id) {
        return getMessages().getString(id);
    }

    public static String getText(Class ref, String id) {
        return getMessages().getString(id);
    }

    public static String getText(Object object, String id) {
        return getText(object.getClass(), id);
    }


    private static Preferences getPrefs() {
        return Preferences.userNodeForPackage(Messages.class);
    }

    public static Locale getLocale() {
        Locale deflocale = Locale.getDefault();
        Preferences prefs = getPrefs();
//        String cc = prefs.get(CC, deflocale.getCountry());
        String lang = prefs.get(LANG, deflocale.getLanguage());
        if (deflocale.getLanguage().equals(lang)) {
            return deflocale;
        }
//        String variant=prefs.get("LANGUAGE",deflocale.getVariant());

        final Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        return locale;
    }

    public static void updateLocale(String language, String country) {
        Preferences prefs = getPrefs();
        Locale.setDefault(new Locale(language, country));
        prefs.put(LANG, language);
        prefs.put(CC, country);
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    public static void updateLocale(String language) {
        Preferences prefs = getPrefs();
        Locale.setDefault(new Locale(language));
        prefs.put(LANG, language);
        try {
            prefs.flush();
        } catch (BackingStoreException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }

    private static final String CC = "CC";
    private static final String LANG = "LANG";


}
