package org.neuclear.commons.sql.entities.drivers;

import org.neuclear.commons.sql.entities.ReferenceDefinition;
import org.neuclear.commons.sql.entities.DataColumnDefinition;
import org.neuclear.commons.sql.entities.ColumnDefinition;



/*
NeuClear Distributed Transaction Clearing Platform
(C) 2003 Pelle Braendgaard

This library is free software); you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation); either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY); without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library); if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

$Id: DDLDriver.java,v 1.1 2003/12/31 00:39:29 pelle Exp $
$Log: DDLDriver.java,v $
Revision 1.1  2003/12/31 00:39:29  pelle
Added Drivers for handling different Database dialects in the entity model.
Added Statement pattern to ledger, simplifying the statement writing process.

*/

/**
 * User: pelleb
 * Date: Dec 30, 2003
 * Time: 2:42:47 PM
 */
public class DDLDriver {
     protected void appendComment(StringBuffer buf) {
                appendString(buf,100);
     }
     protected void appendMoney(StringBuffer buf) {
                buf.append( "DECIMAL");
     }
     protected void appendTimestamp(StringBuffer buf){
                buf.append( "TIMESTAMP");
     }
    protected void appendDate(StringBuffer buf){
               buf.append( "DATE");
    }
    protected void appendTime(StringBuffer buf){
               buf.append( "TIME");
    }
     protected void appendIdentity(StringBuffer buf){
                buf.append( "IDENTITY");
     }
     protected void appendBoolean(StringBuffer buf){
                buf.append( "BIT");
     }
     protected void appendString(StringBuffer buf,int size){
         buf.append( "VARCHAR(");
         buf.append(size);
         buf.append(")");
     }
     protected void appendURI(StringBuffer buf){
       appendString(buf,50);
         buf.append(" NOT NULL");
     }
     protected void appendDefault(StringBuffer buf){
        appendString(buf,50);
     }
    protected void appendInteger(StringBuffer buf) {
        buf.append("INTEGER");
    }
    protected void appendCharacter(StringBuffer buf) {
        buf.append("CHAR");
    }
    protected void appendByte(StringBuffer buf) {
        buf.append("TINYINT");
    }
    protected void appendShort(StringBuffer buf) {
        buf.append("SMALLINT");
    }
    protected void appendLong(StringBuffer buf) {
        buf.append("BIGINT");
    }

     public final void appendColumnDefinition(StringBuffer buf,ColumnDefinition col){
         buf.append(col.getName());
         buf.append(" ");
         int type=0;
         if (col instanceof ReferenceDefinition)
            type=((ReferenceDefinition)col).getRef().getId().getType();
         else
            type=((DataColumnDefinition)col).getType();

         switch(type){
             case ColumnDefinition.FIELD_COMMENT:
                    appendComment(buf);
                 break;
             case ColumnDefinition.FIELD_MONEY:
                    appendMoney(buf);
                 break;
             case ColumnDefinition.FIELD_TIMESTAMP:
                    appendTimestamp(buf);
                 break;
             case ColumnDefinition.FIELD_ID:
                    appendIdentity(buf);
                 break;
             case ColumnDefinition.FIELD_URI:
                    appendURI(buf);
                 break;
             case ColumnDefinition.FIELD_BOOLEAN:
                    appendBoolean(buf);
                 break;
             case ColumnDefinition.FIELD_TIME:
                    appendTime(buf);
                 break;
             case ColumnDefinition.FIELD_DATE:
                    appendDate(buf);
                 break;
             case ColumnDefinition.FIELD_INTEGER:
                    appendInteger(buf);
                 break;
             case ColumnDefinition.FIELD_BYTE:
                    appendByte(buf);
                 break;
             case ColumnDefinition.FIELD_SHORT:
                    appendShort(buf);
                 break;
             case ColumnDefinition.FIELD_LONG:
                    appendLong(buf);
                 break;
             case ColumnDefinition.FIELD_CHARACTER:
                    appendCharacter(buf);
                 break;
             default:
                 appendDefault(buf);
         }

     }

    public void appendPrimaryKey(StringBuffer buf, DataColumnDefinition id){
         buf.append(",\nPRIMARY KEY(");
         buf.append(id.getName());
         buf.append(")");
     }

     public void appendForeignKey(StringBuffer buf, ReferenceDefinition col){
            buf.append(",\nFOREIGN KEY (");
            buf.append(col.getName());
            buf.append(") REFERENCES ");
            buf.append(col.getRef().getName());
            buf.append("(");
            buf.append(col.getRef().getId().getName());
            buf.append(") ON DELETE CASCADE");
    }
    public final static DDLDriver getInstance(String type){
        if (type.equals("mysql"))
            return new MySQLDriver();
        if (type.equals("hsqldb"))
            return new HSQLDriver();
        return new DDLDriver();
    }

}
