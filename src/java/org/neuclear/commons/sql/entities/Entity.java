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

$Id: Entity.java,v 1.2 2003/12/26 22:51:17 pelle Exp $
$Log: Entity.java,v $
Revision 1.2  2003/12/26 22:51:17  pelle
Mainly fixes to SQLLedger to support the schema generated by the new EntityModel

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
 * Time: 4:38:23 PM
 */
public class Entity {
    Entity(EntityModel model,String id,Object[] data) {
        this.model = model;
        this.data=data;
        this.id=id;
    }
    public String getID(){
        return id;
    }

    public Object[] getData() {
        return data;
    }

    public EntityModel getModel() {
        return model;
    }


    private final EntityModel model;
    private final Object data[];
    private final String id;
}