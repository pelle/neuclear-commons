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

/**
 * User: pelleb
 * Date: May 18, 2004
 * Time: 11:51:34 AM
 */
public abstract class AbstractDialogRunner extends WaitForInput {

    public AbstractDialogRunner(NeuClearDialog dia) {
        this.dia = dia;
    }

    public void run() {
        dia.open();
        dia.toFront();
    }

    public void error(String error) {
        dia.switchToMain();
        dia.message.error(error);
    }

    public void error(Exception error) {
        dia.switchToMain();
        dia.message.error(error);
    }

    private final NeuClearDialog dia;
}
