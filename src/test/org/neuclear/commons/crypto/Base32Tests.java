package org.neuclear.commons.crypto;

import junit.framework.TestCase;

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

$Id: Base32Tests.java,v 1.1 2004/01/16 23:41:59 pelle Exp $
$Log: Base32Tests.java,v $
Revision 1.1  2004/01/16 23:41:59  pelle
Added Base32 class. The Base32 encoding used wasnt following the standards.
Added user creatable Identity for Public Keys

*/

/**
 * User: pelleb
 * Date: Jan 16, 2004
 * Time: 9:52:41 PM
 */
public class Base32Tests extends TestCase{
    public Base32Tests(String string) {
        super(string);
    }

    public void testBase32() throws CryptoException {
        assertEquals(32,Base32.encode(CryptoTools.digest("hello")).length());
        assertEquals(8,Base32.encode("hello").length());
        assertEquals(10,Base32.encode("hello1").length());
        assertEquals(12,Base32.encode("hello12").length());
        assertEquals(9,Base32.encode("hello123").length());
        assertEquals(14,Base32.encode("hello1234").length());
        assertEquals(15,Base32.encode("hello12345").length());
        assertEquals(16,Base32.encode("hello123456").length());

        for (int i=0;i<TESTSTRINGS.length;i++){
            assertEquals(TESTSTRINGS[i],new String(Base32.decode(Base32.encode(TESTSTRINGS[i]))));
        }
    }
    static final String  TESTSTRINGS[] =new String[]{
            "0123456789",
            "",
            "0",
            "abcde56"
    };

}
