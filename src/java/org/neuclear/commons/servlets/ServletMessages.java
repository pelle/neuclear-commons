package org.neuclear.commons.servlets;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
 * Date: Jun 6, 2004
 * Time: 3:01:56 PM
 */
public class ServletMessages {
    private ServletMessages() {
    };

    public static ResourceBundle getMessages(HttpServletRequest request) {
        final Locale locale = request.getLocale();
        System.out.println("locale " + locale.getLanguage());
        if (!bundles.containsKey(locale.getLanguage())) {
            ResourceBundle messages = ResourceBundle.getBundle("ledgermessages", locale);
            bundles.put(locale.getLanguage(), messages);
        }
        return (ResourceBundle) bundles.get(locale.getLanguage());
    }

    private static final Map bundles = Collections.synchronizedMap(new HashMap());
}
