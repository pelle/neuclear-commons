package org.neuclear.commons.crypto.channels;

import junit.framework.TestCase;
import org.neuclear.commons.crypto.CryptoTools;
import org.neuclear.commons.crypto.CryptoException;
import org.neuclear.commons.crypto.Base32;
import org.omg.CORBA.portable.InputStream;

import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import java.security.SignatureException;
import java.security.InvalidKeyException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: pelleb
 * Date: Mar 6, 2004
 * Time: 11:47:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class DigestChannelTest extends TestCase {
    public DigestChannelTest(String name) {
        super(name);
    }

    public void testDigest() throws NoSuchAlgorithmException, IOException, CryptoException {
        for (int i=0;i<TESTSTRINGS.length;i++){
            assertDigestEquals(TESTSTRINGS[i]);
        }
    }
    public void testFileDigest() throws NoSuchAlgorithmException, IOException, CryptoException {
        File cd=new File("src/java/org/neuclear/commons/crypto");
        File files[]=cd.listFiles();
        System.out.println("Testing Digests on: "+files.length+" files");
        for (int i=0;i<files.length;i++){
            if (files[i].isFile())
            assertDigestEquals(files[i]);
        }
    }
    public String getChannelDigest(String data) throws NoSuchAlgorithmException, IOException, CryptoException {
        DigestChannel ch=new DigestChannel();
        ByteBuffer buf=ByteBuffer.wrap(data.getBytes());
        ch.write(buf);
        return new String(ch.getDigest());
    }
    public String getNormalDigest(String data){
        return new String(CryptoTools.digest(data));
    }
    public void assertDigestEquals(String data) throws NoSuchAlgorithmException, IOException, CryptoException {
        String digest=getChannelDigest(data);
        assertEquals("Digest Length",20,digest.length());
        assertEquals("Digest Match",getNormalDigest(data),digest);
    }
    public String getChannelDigest(File file) throws NoSuchAlgorithmException, IOException, CryptoException {
        DigestChannel ch=new DigestChannel();
        FileChannel fch=new FileInputStream(file).getChannel();
        fch.transferTo(0,fch.size(),ch);
        return new String(ch.getDigest());
    }
    public String getNormalDigest(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest dig=MessageDigest.getInstance("SHA1");

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
        int numread=0;
        byte buffer[]=new byte[1024];
        while ((numread=in.read(buffer, 0, buffer.length)) >= 0) {
                     dig.update(buffer,0,numread);
        }
        in.close();
        return new String(dig.digest());
    }
    public void assertDigestEquals(File file) throws NoSuchAlgorithmException, IOException, CryptoException {
        String digest=getChannelDigest(file);
        assertEquals("Digest Length",20,digest.length());
        assertEquals("Digest Match",getNormalDigest(file),digest);
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
            getChannelDigest(TESTSTRINGS[i % TESTSTRINGS.length]);
        }
        long dur = System.currentTimeMillis() - start;
        System.out.println(ITERATIONS + " iterations took: " + dur + "ms");

        System.out.println("\nNormal Signing benchmarks:");
        start=System.currentTimeMillis();
        for (int i=0;i<ITERATIONS;i++){
            getNormalDigest(TESTSTRINGS[i % TESTSTRINGS.length]);
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
        System.out.println("File DigestChannel benchmarks:");
        File cd=new File("src/java/org/neuclear/commons/crypto");
        File files[]=cd.listFiles();
        long start = System.currentTimeMillis();
        for (int i=0;i<files.length;i++){
            if (files[i].isFile())
                getChannelDigest(files[i % files.length]);
        }
        long dur = System.currentTimeMillis() - start;
        System.out.println(ITERATIONS + " iterations took: " + dur + "ms");

        System.out.println("\nNormal File digesting benchmarks:");
        start=System.currentTimeMillis();
        for (int i=0;i<files.length;i++){
            if (files[i].isFile())
                getNormalDigest(files[i % files.length]);
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

}
