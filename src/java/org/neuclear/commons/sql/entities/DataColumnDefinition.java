package org.neuclear.commons.sql.entities;

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

$Id: DataColumnDefinition.java,v 1.1 2003/12/24 00:25:40 pelle Exp $
$Log: DataColumnDefinition.java,v $
Revision 1.1  2003/12/24 00:25:40  pelle
Created a kind of poor man's version of ofbiz.org's EntityEngine. It doesnt use xml to configure it, but code.
Should mainly be used to create tables. Can also insert rows, but hasnt been thoroughly tested.
At some point I will improve that part and add some kind of smart querying engine to it. Similar to EntityEngine. But I dont
need that myself right now.
SQLLedger now uses this to create its tables. It is not fully working yet, but will be shortly.

*/

/**
 * User: pelleb
 * Date: Dec 23, 2003
 * Time: 2:31:13 PM
 */
public class DataColumnDefinition  extends ColumnDefinition{
    public DataColumnDefinition(EntityModel table, String name, int type) {
        super(table,name);
        this.type = type;
    }

    public final String createDDL(){
        return getName()+" "+getDefinition();
    }

    protected String getDefinition() {
        switch(type){
            case FIELD_COMMENT:
                return "VARCHAR(100)";
            case FIELD_MONEY:
                return "DECIMAL";
            case FIELD_TIMESTAMP:
                return "TIMESTAMP";
            case FIELD_ID:
                return "BIGINT";
            case FIELD_BOOLEAN:
                return "TINYINT";
        }
        return "VARCHAR(50)";
    }

    public int getType() {
        return type;
    }

    private final int type;
}
