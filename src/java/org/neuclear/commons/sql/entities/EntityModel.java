package org.neuclear.commons.sql.entities;

import org.neuclear.commons.sql.entities.drivers.DDLDriver;
import org.neuclear.commons.sql.statements.StatementFactory;

import java.util.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

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

$Id: EntityModel.java,v 1.4 2004/01/02 23:19:02 pelle Exp $
$Log: EntityModel.java,v $
Revision 1.4  2004/01/02 23:19:02  pelle
Added StatementFactory pattern and refactored the ledger to use it.

Revision 1.3  2003/12/31 00:39:29  pelle
Added Drivers for handling different Database dialects in the entity model.
Added Statement pattern to ledger, simplifying the statement writing process.

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
 * Time: 2:13:21 PM
 */
public final class EntityModel {
    EntityModel(final String name,String idname,boolean uri){
        columns=new HashMap();
        colseq=new LinkedList();
        foreign=new LinkedList();
        this.name=name;
        this.uri=uri;
        id=new DataColumnDefinition(this,idname,uri?ColumnDefinition.FIELD_URI:ColumnDefinition.FIELD_ID);
        addColumn(id);
    }
    EntityModel(final String name,boolean uri){
        this(name,"id",uri);
    }
    public synchronized final void create(StatementFactory fact, DDLDriver driver) {
        //first lets build all the Dependencies
        for (int i = 0; i < foreign.size(); i++) {
            ReferenceDefinition definition = (ReferenceDefinition) foreign.get(i);
            definition.getRef().create(fact,driver);
        }
        final String ddl = createDDL(driver);
        try {
            PreparedStatement stmt=fact.prepareStatement(ddl);
            stmt.execute();
            System.out.println("Created: "+getName());

        } catch (SQLException e) {
            if (e.getMessage().indexOf("exists")<0)
                System.out.println(e.getLocalizedMessage());
            System.err.println(ddl);
        }
    }
    public final String createDDL(DDLDriver driver){
        StringBuffer buf=new StringBuffer("CREATE TABLE ");
        buf.append(name);
        buf.append(" (\n");
        Iterator iter=colseq.iterator();
        boolean first=true;
        while(iter.hasNext()) {
            if (first)
                first=false;
            else
                buf.append(",\n");
            ColumnDefinition col=(ColumnDefinition)iter.next();
            driver.appendColumnDefinition(buf,col);
        }
        iter=foreign.iterator();
        while(iter.hasNext()) {
            ReferenceDefinition col=(ReferenceDefinition)iter.next();
            driver.appendForeignKey(buf,col);
        }
        if (uri){
            driver.appendPrimaryKey(buf,getId());
        }
        buf.append("\n)");
        return buf.toString();
    }

    public final String getName() {
        return name;
    }
    private final ColumnDefinition addColumn(ColumnDefinition col){
        columns.put(col.getName(),col);
        colseq.add(col);
        return col;
    }
    public final DataColumnDefinition addURI(String name){
        return (DataColumnDefinition) addColumn(new DataColumnDefinition(this,name,ColumnDefinition.FIELD_URI));
    }
    public final DataColumnDefinition addURI(){
        return addURI("name");
    }
    public final DataColumnDefinition addMoney(String name){
        return (DataColumnDefinition) addColumn(new DataColumnDefinition(this,name,ColumnDefinition.FIELD_MONEY));
    }
    public final DataColumnDefinition addMoney(){
        return addMoney("amount");
    }
    public final DataColumnDefinition addTimeStamp(String name){
        return (DataColumnDefinition) addColumn(new DataColumnDefinition(this,name,ColumnDefinition.FIELD_TIMESTAMP));
    }
    public final DataColumnDefinition addTimeStamp(){
        return addTimeStamp("created");
    }
    public final DataColumnDefinition addValueTime(){
        return addTimeStamp("valuetime");
    }
    public final DataColumnDefinition addComment(String name){
        return (DataColumnDefinition) addColumn(new DataColumnDefinition(this,name,ColumnDefinition.FIELD_COMMENT));
    }
    public final DataColumnDefinition addComment(){
        return addComment("comment");
    }
    public final DataColumnDefinition addTitle(){
        return addURI("title");
    }
    public final DataColumnDefinition addBoolean(String name){
        return (DataColumnDefinition) addColumn(new DataColumnDefinition(this,name,ColumnDefinition.FIELD_BOOLEAN));
    }
    public final ReferenceDefinition addReference(EntityModel def) {
        ReferenceDefinition ref=new ReferenceDefinition(this,def);
        return addReference(ref);
    }

    private ReferenceDefinition addReference(ReferenceDefinition ref) {
        addColumn(ref);
        foreign.add(ref);
        return ref;
    }

    public final ReferenceDefinition addReference(String name,EntityModel def) {
        return addReference(new ReferenceDefinition(this,def,name));
    }

    public final DataColumnDefinition getId() {
        return id;
    }
    public final Entity insertEntity(Connection con,Object data[]) throws SQLException {
        if ((data.length%2==1))//&&(data.length/2<columns.size()))
            throw new SQLException("Incorrect amount of parameters");
        StringBuffer buf=new StringBuffer(22+getName().length()+3*(data.length/2));
        buf.append("INSERT INTO ");
        buf.append(getName());
        buf.append(" (");
        for (int i = 0; i<data.length; i+=2){
            if (i>0)
                buf.append(", ");
            buf.append(data[i]);
        }

        buf.append(") VALUES (");
        for (int i = 0; i<data.length; i+=2){
            if (i>0)
                buf.append(", ");
            buf.append("?");
        }
        buf.append(")");
        PreparedStatement stmt=con.prepareStatement(buf.toString());
        String id=null;
        for (int i = 0; i<(data.length/2); i++){
            int j=i*2;
            ColumnDefinition definition = (ColumnDefinition) columns.get(data[j]);
            if (definition instanceof ReferenceDefinition) {
                ReferenceDefinition ref=(ReferenceDefinition) definition;
                if (data[2*i+1] instanceof Object[]) {
                    Entity refentity=ref.getRef().insertEntity(con,(Object[]) data[j+1]);
                    stmt.setString(i+1,refentity.getID());
                } else {
                    stmt.setString(i+1,data[j+1].toString());
                }
            } else {
                switch (((DataColumnDefinition)definition).getType()) {
                    case ColumnDefinition.FIELD_MONEY:
                        stmt.setDouble(i+1,((Double)data[j+1]).doubleValue());
                        break;
                    case ColumnDefinition.FIELD_TIMESTAMP:
                        stmt.setTimestamp(i+1,((Timestamp)data[j+1]));
                        break;
                    case ColumnDefinition.FIELD_ID:
                        stmt.setLong(i+1,((Long)data[j+1]).longValue());
                        break;
                    case ColumnDefinition.FIELD_BOOLEAN:
                        stmt.setBoolean(i+1,((Boolean)data[j+1]).booleanValue());
                        break;
                    default:
                        stmt.setString(i+1,data[j+1].toString());
                }
                if (getId().getName().equals(data[j]))
                    id=data[j+1].toString();
            }
        }
        stmt.execute();
        return new Entity(this,id,data);
    }
    private final DataColumnDefinition id;
    private final Map columns;
    private final List colseq;

    private final List foreign;
    private final String name;
    private final boolean uri;
}