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

$Id: ColumnDefinition.java,v 1.1 2003/12/24 00:25:40 pelle Exp $
$Log: ColumnDefinition.java,v $
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
 * Time: 2:15:18 PM
 */
public abstract class ColumnDefinition {
    protected ColumnDefinition(EntityModel table, String name) {
        this.model=table;
        this.name=name;
    }
    public abstract String createDDL();

    public final EntityModel getModel(){
        return model;
    }

    public String getName() {
        return name;
    }

    public int hashCode() {
        return (getClass().hashCode()+name.hashCode())/2;    //To change body of overriden methods use Options | File Templates.
    }

    public boolean equals(Object object) {
        if (object==this)
            return true;
        if (!object.getClass().equals(this.getClass()))
           return false;
        final ColumnDefinition col2 = ((ColumnDefinition)object);
        return getModel().equals(col2.getModel())&&getName().equals(col2.getName());
    }

    private final String name;
    private final EntityModel model;
    public static final int FIELD_URI=0;
    public static final int FIELD_COMMENT=1;
    public static final int FIELD_MONEY=2;
    public static final int FIELD_TIMESTAMP=3;
    public static final int FIELD_ID=4;
    public static final int FIELD_BOOLEAN=5;


}
