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

$Id: Base32Tests.java,v 1.7 2004/03/03 23:24:25 pelle Exp $
$Log: Base32Tests.java,v $
Revision 1.7  2004/03/03 23:24:25  pelle
Added a "test" alias to testkeys.jks

Revision 1.6  2004/02/19 15:29:13  pelle
Various cleanups and corrections

Revision 1.5  2004/02/18 00:13:42  pelle
Many, many clean ups. I've readded Targets in a new method.
Gotten rid of NamedObjectBuilder and revamped Identity and Resolvers

Revision 1.4  2004/01/19 23:49:29  pelle
Unit testing uncovered further issues with Base32
NSTools is now uptodate as are many other classes. All transactional builders habe been updated.
Well on the way towards full "green" on Junit.

Revision 1.3  2004/01/19 17:53:14  pelle
Various clean ups

Revision 1.2  2004/01/18 21:20:20  pelle
Created Base32 encoder that now fully complies with Tyler's spec.

Revision 1.1  2004/01/16 23:41:59  pelle
Added Base32 class. The Base32 encoding used wasnt following the standards.
Added user creatable Identity for Public Keys

*/

/**
 * User: pelleb
 * Date: Jan 16, 2004
 * Time: 9:52:41 PM
 */
public class Base32Tests extends TestCase {
    public Base32Tests(String string) {
        super(string);
    }

    public void testLength() throws CryptoException {
        assertEquals(32, Base32.encode(CryptoTools.digest("hello")).length());
        assertEquals(8, Base32.encode("hello").length());
        assertEquals(10, Base32.encode("hello1").length());
        assertEquals(12, Base32.encode("hello12").length());
        assertEquals(13, Base32.encode("hello123").length());
        assertEquals(15, Base32.encode("hello1234").length());
        assertEquals(16, Base32.encode("hello12345").length());
        assertEquals(18, Base32.encode("hello123456").length());

    }

    public void testBase32Codec() throws CryptoException {

        for (int i = 0; i < TESTSTRINGS.length; i++) {
//            System.out.print("Encoding: "+TESTSTRINGS[i]+" ...");
            final String encoded = Base32.encode(TESTSTRINGS[i]);
            System.out.println(" ->" + encoded);
            assertEquals("TESTSTRINGS[" + i + "]", TESTSTRINGS[i].getBytes(), Base32.decode(encoded));
        }
    }

    public void testSHA1vsDecodedTyler() throws CryptoException {
        for (int i=0;i<TESTSTRINGS.length;i++){
            assertTrue("TESTSTRINGS["+i+"]",CryptoTools.equalByteArrays(CryptoTools.digest(TESTSTRINGS[i]),Base32.decode(TYLER_SHA1_OUTPUT[i])));
        }
    }
    public void testSHA1vsDecodedOwn() throws CryptoException {
        for (int i=0;i<TESTSTRINGS.length;i++){
            byte[] hash=Base32.encode(CryptoTools.digest(TESTSTRINGS[i])).getBytes();
            assertTrue("TESTSTRINGS["+i+"]",CryptoTools.equalByteArrays(CryptoTools.digest(TESTSTRINGS[i]),Base32.decode(hash)));
        }
    }
    public void testSHA1HomevsTyler() throws CryptoException {
        for (int i=0;i<TESTSTRINGS.length;i++){
            assertEquals("TESTSTRINGS["+i+"]",Base32.encode(CryptoTools.digest(TESTSTRINGS[i])),TYLER_SHA1_OUTPUT);
        }
    }

    public void testBase32vsTyler() throws CryptoException {

        for (int i=0;i<TESTSTRINGS.length;i++){
            final String encoded = Base32.encode(TESTSTRINGS[i]);
            assertEquals("TESTSTRINGS["+i+"]",TYLER_OUTPUT[i],encoded);
        }
    }

    public void testDecodeTyler() throws CryptoException {

        for (int i=0;i<TESTSTRINGS.length;i++){
            final byte decoded[] = Base32.decode(TYLER_OUTPUT[i]);
            assertEquals("TESTSTRINGS["+i+"]",TESTSTRINGS[i].getBytes(),decoded);
        }
    }
    public void assertEquals(String description, byte a[], byte b[]) {
        assertEquals(description + " length", a.length, b.length);
        for (int i = 0; i < a.length; i++)
            assertEquals(description + "[" + i + "]", a[i], b[i]);

    }


/*
    public void testOutputTyler() throws CryptoException{
        for (int i=0;i<TESTSTRINGS.length;i++){
            final String encoded = com.waterken.url.Base32.encode(CryptoTools.digest(TESTSTRINGS[i]));
            System.out.println("\""+encoded+"\",");
        }
    }
*/

    static final String TESTSTRINGS[] = new String[]{
        "",
        "0",
        "01",
        "012",
        "0123",
        "01234",
        "012345",
        "0123456",
        "01234567",
        "012345678",
        "0123456789",
        "0123456789A",
        "0123456789A0123456789As0123456789A",
        new String(CryptoTools.digest("0123456"))

    };
    static final String TYLER_OUTPUT[]=new String[]{
        "",
        "ga",
        "gayq",
        "gayte",
        "gaytemy",
        "gaytemzu",
        "gaytemzugu",
        "gaytemzugu3a",
        "gaytemzugu3do",
        "gaytemzugu3dooa",
        "gaytemzugu3doobz",
        "gaytemzugu3doobzie",
        "gaytemzugu3doobzieydcmrtgq2tmnzyhfaxgmbrgiztinjwg44dsqi",
        "h47qqpz7h4cd6hb7cq7t6pz7ha7wwx3x"
    };

    static final String TYLER_SHA1_OUTPUT[]=new String[]{
        "3i42h3s6nnfq2msvx7xzkyayscx5qbyj",
        "wzmj7rvlbxecz4jathi4fvakxgkoqqim",
        "3x7bmm2f2m4bsowcxxayh6hj3t7zas2d",
        "ysrntg6crurwbgfasutxw7vqoggwxydi",
        "ys24q26vo7nd3e76u7ejzotby6furzmj",
        "cgieutulo73cilrnfcdqkar23liaveyq",
        "7x4lywauknxwmajiqtqunkeipjchbgsw",
        "w7wqramqyiclghgxcsconiofhcmgwx3x",
        "zsvi3dompubqzvvgo2g3qh4q2dxzo3b5",
        "tjyutjnhpbv3g2hanuemlv3xotvuhje6",
        "q6woyf6ntxgsbjywzqwpm5axw4oiu4aw",
        "weh5oeb3kibeinn6i3jiaskxum76y7dn",
        "uo4jb3ds2i2bb2qbds2h5nusuwum2fxh",
        "5bhw3daqpo2iq5eqbqgt54g5otdjldov"
    };
}
