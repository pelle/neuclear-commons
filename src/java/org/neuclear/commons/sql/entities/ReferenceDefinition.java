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

$Id: ReferenceDefinition.java,v 1.1 2003/12/24 00:25:40 pelle Exp $
$Log: ReferenceDefinition.java,v $
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
 * Time: 2:34:09 PM
 */
public class ReferenceDefinition extends ColumnDefinition{
    public ReferenceDefinition(EntityModel table, EntityModel ref){
        this(table,ref,ref.getName()+"id");
    }
    public ReferenceDefinition(EntityModel table, EntityModel ref,String name) {
        super(table,name);
        this.ref = ref;
    }


    public final String createDDL() {
        return getName()+" "+getDefinition();
    }
    private final String getDefinition(){
        if (ref.getId().getType()==ColumnDefinition.FIELD_ID)
            return "INTEGER";
        return "VARCHAR(50)";
    }

    public final EntityModel getRef() {
        return ref;
    }

    private final EntityModel ref;
}
