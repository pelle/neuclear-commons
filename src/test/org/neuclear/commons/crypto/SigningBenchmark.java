package org.neuclear.commons.crypto;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

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

$Id: SigningBenchmark.java,v 1.3 2003/11/21 04:43:42 pelle Exp $
$Log: SigningBenchmark.java,v $
Revision 1.3  2003/11/21 04:43:42  pelle
EncryptedFileStore now works. It uses the PBECipher with DES3 afair.
Otherwise You will Finaliate.
Anything that can be final has been made final throughout everyting. We've used IDEA's Inspector tool to find all instance of variables that could be final.
This should hopefully make everything more stable (and secure).

Revision 1.2  2003/11/12 23:47:50  pelle
Much work done in creating good test environment.
PaymentReceiverTest works, but needs a abit more work in its environment to succeed testing.

Revision 1.1  2003/11/12 18:54:42  pelle
Updated SimpleSignerStoreTest to use a StoredPassPhraseAgent eliminating the popup during testing.
Created SigningBenchmark for running comparative performance benchmarks on various key algorithms.

*/

/**
 * Tests performances of various cryptographic such as key generation
 * signing and verification.
 */
public final class SigningBenchmark {
    public SigningBenchmark(final String algorithm, final int size) throws NoSuchAlgorithmException {
        kpg = KeyPairGenerator.getInstance(algorithm);
        kpg.initialize(size);
        results = new long[3];
        this.algorithm = algorithm;
        this.size = size;
        testdata = TESTSTRING.getBytes();
    }


    public final void generateKeys() {
        kp = kpg.generateKeyPair();
    }

    public final void sign() throws CryptoException {
        testsig = CryptoTools.sign(kp, testdata);
    }

    public final void verify() throws CryptoException {
        CryptoTools.verify(kp.getPublic(), testdata, testsig);
    }

    public final void benchmark() throws CryptoException {

        Date start = new Date();
        for (int i = 0; i < ITERATIONS; i++)
            generateKeys();
        Date end = new Date();
        results[0] = end.getTime() - start.getTime();

        start = new Date();
        for (int i = 0; i < ITERATIONS; i++)
            sign();
        end = new Date();
        results[1] = end.getTime() - start.getTime();

        start = new Date();
        for (int i = 0; i < ITERATIONS; i++)
            verify();
        end = new Date();
        results[2] = (end.getTime() - start.getTime()) / ITERATIONS;
        printResults();
    }

    public final void printResults() {

        System.out.println(algorithm + "\t" + size + "\t" + results[0] + "\t" + results[1] + "\t" + results[2]);
    }

    public static void main(final String[] args) {
        System.out.println("Performance Analysis of key sizes and algorithms");

        final String[] algorithms = new String[]{"RSA", "DSA"};
        final int[] keysizes = new int[]{512, 1024};
        try {
            for (int a = 0; a < algorithms.length; a++)
                for (int k = 0; k < keysizes.length; k++)
                    new SigningBenchmark(algorithms[a], keysizes[k]).benchmark();
        } catch (CryptoException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private static final int ITERATIONS = 100;
    private final KeyPairGenerator kpg;

    private KeyPair kp;
    private final String algorithm;
    private final int size;

    private final long[] results;

    private static final String TESTSTRING = "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "1234567890ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private byte[] testsig;
    private final byte[] testdata;

}
