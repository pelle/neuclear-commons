package org.neuclear.commons.crypto.channels;

import junit.framework.TestCase;
import org.neuclear.commons.crypto.CryptoTools;
import org.neuclear.commons.crypto.CryptoException;
import org.neuclear.commons.test.JunitTools;

import java.security.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: pelleb
 * Date: Mar 6, 2004
 * Time: 11:47:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class SigningChannelTest extends TestCase {
    public SigningChannelTest(String name) throws  NoSuchAlgorithmException {
        super(name);
        kp = CryptoTools.createTinyKeyPair();
    }

    public void testSign() throws NoSuchAlgorithmException, IOException, SignatureException, InvalidKeyException, CryptoException {
        for (int i=0;i<TESTSTRINGS.length;i++){
            assertSignatureEquals(TESTSTRINGS[i]);
        }

    }

    public void testFileDigest() throws NoSuchAlgorithmException, IOException, SignatureException, InvalidKeyException, CryptoException {
        File cd=new File("src/java/org/neuclear/commons/crypto");
        File files[]=cd.listFiles();
        System.out.println("Testing Digests on: "+files.length+" files");
        for (int i=0;i<files.length;i++){
            if (files[i].isFile())
            assertSignatureEquals(files[i]);
        }
    }

    public String getChannelSignature(String data) throws NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException {
        SigningChannel ch=new SigningChannel(kp.getPrivate());
        ByteBuffer buf=ByteBuffer.wrap(data.getBytes());
        ch.write(buf);
        return new String(ch.getSignature());
    }
    public String getNormalSignature(String data) throws CryptoException {
        return new String(CryptoTools.sign(kp,data.getBytes()));
    }
    public boolean verifyChannelSignature(String data,String sig) throws NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException {
        VerifyingChannel ch=new VerifyingChannel(kp.getPublic());
        ByteBuffer buf=ByteBuffer.wrap(data.getBytes());
        ch.write(buf);
        return ch.verify(sig.getBytes());
    }
    public boolean verifyNormalSignature(String data,String sig) throws CryptoException {
        return CryptoTools.verify(kp.getPublic(),data.getBytes(),sig.getBytes());
    }
    public void assertSignatureEquals(String data) throws NoSuchAlgorithmException, IOException, SignatureException, InvalidKeyException, CryptoException {
        String sig=getChannelSignature(data);
        assertEquals("Signature Match",getNormalSignature(data),sig);
        assertTrue("Signature Channel Verify",verifyChannelSignature(data,sig));
        assertTrue("Signature Channel Normal",verifyNormalSignature(data,sig));
    }
    public void assertSignatureEquals(File file) throws NoSuchAlgorithmException, IOException, SignatureException, InvalidKeyException, CryptoException {
        String sig=getChannelSignature(file);
        assertEquals("Signature Match",getNormalSignature(file),sig);
    }

    public String getChannelSignature(File file) throws NoSuchAlgorithmException, IOException, InvalidKeyException, SignatureException {
        SigningChannel ch=new SigningChannel(kp.getPrivate());
        FileChannel fch=new FileInputStream(file).getChannel();
        fch.transferTo(0,fch.size(),ch);
        return new String(ch.getSignature());
    }
    public String getNormalSignature(File file) throws NoSuchAlgorithmException, IOException, SignatureException, InvalidKeyException {
        Signature sig=Signature.getInstance("SHA1withRSA");
        sig.initSign(kp.getPrivate());

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        int numread=0;
        byte buffer[]=new byte[1024];
        while ((numread=in.read(buffer, 0, buffer.length)) >= 0) {
                     sig.update(buffer,0,numread);
        }
        in.close();
        return new String(sig.sign());
    }

    /**
     * Silly Microbenchmark
     *
     * @throws org.neuclear.commons.crypto.CryptoException
     */
    public void testBenchmark() throws CryptoException, NoSuchAlgorithmException, IOException, SignatureException, InvalidKeyException {
        System.out.println("SigningChannel benchmarks:");
        final Runtime runtime = Runtime.getRuntime();
        long start = System.currentTimeMillis();
        for (int i = 0; i < ITERATIONS; i++) {
            getChannelSignature(TESTSTRINGS[i % TESTSTRINGS.length]);
        }
        long dur = System.currentTimeMillis() - start;
        System.out.println(ITERATIONS + " iterations took: " + dur + "ms");

        System.out.println("\nNormal Signing benchmarks:");
        start=System.currentTimeMillis();
        for (int i=0;i<ITERATIONS;i++){
            getNormalSignature(TESTSTRINGS[i % TESTSTRINGS.length]);
        }
        dur=System.currentTimeMillis()-start;
        System.out.println(ITERATIONS+" iterations took: "+dur+"ms");

    }
    /**
     * Silly Microbenchmark
     *
     * @throws org.neuclear.commons.crypto.CryptoException
     */
    public void testFileBenchmark() throws CryptoException, NoSuchAlgorithmException, IOException, SignatureException, InvalidKeyException {
        System.out.println("File SigningChannel benchmarks:");
        File cd=new File("src/java/org/neuclear/commons/crypto");
        File files[]=cd.listFiles();
        long start = System.currentTimeMillis();
        for (int i=0;i<ITERATIONS;i++){
            if (files[i% files.length].isFile())
                getChannelSignature(files[i % files.length]);
        }
        long dur = System.currentTimeMillis() - start;
        System.out.println(ITERATIONS + " iterations took: " + dur + "ms");

        System.out.println("\nNormal File signing benchmarks:");
        start=System.currentTimeMillis();
        for (int i=0;i<ITERATIONS;i++){
            if (files[i% files.length].isFile())
                getNormalSignature(files[i % files.length]);
        }
        dur=System.currentTimeMillis()-start;
        System.out.println(ITERATIONS+" iterations took: "+dur+"ms");

    }


    final static int ITERATIONS = 1000;

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
        "0123456789A0123456789As0123456789A"
    };
    private KeyPair kp;

}
