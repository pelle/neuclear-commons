package org.neuclear.commons.sql.statements;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.IOException;

/*
NeuClear Distributed Transaction Clearing Platform
(C) 2003 Pelle Braendgaard

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

$Id: StatementFactory.java,v 1.1 2004/01/02 23:19:03 pelle Exp $
$Log: StatementFactory.java,v $
Revision 1.1  2004/01/02 23:19:03  pelle
Added StatementFactory pattern and refactored the ledger to use it.

*/

/**
 * User: pelleb
 * Date: Jan 2, 2004
 * Time: 3:35:09 PM
 */
public interface StatementFactory {
    PreparedStatement prepareStatement(String sql) throws SQLException;
}
