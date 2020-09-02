package org.apache.zookeeper.server;

import org.apache.zookeeper.ZKTestCase;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MyBuffInputReadTest extends ZKTestCase {

    private static final byte[] firstData = "Apache ZooKeeper".getBytes(Charset.forName("UTF-8"));
    private static byte[] data;

    private ByteBuffer buffer;
    private ByteBufferInputStream inputBuffer;
    private byte[] bs;


    @BeforeClass
    public static void setUpClass() {
        int len = firstData.length + 2;
        data = new byte[len];           //alloco byte per Data, 2 in piu di first data
        //copio TUTTI i byte di firstData in data (quindi data avrà ultimi 2 byte nuovi)
        System.arraycopy(firstData, 0, data, 0, firstData.length);
        data[len - 2] = (byte) 0x0;         //alloco memoria x ultimi 2 byte
        data[len - 1] = (byte) 0xff;
    }


    @Before
    public void setUp() throws Exception {
        buffer = ByteBuffer.wrap(data);         //metto byte di data in buffer
        inputBuffer = new ByteBufferInputStream(buffer);
        bs = new byte[] { (byte) 1, (byte) 2, (byte) 3, (byte) 4 };     //inizializzo array di byte
    }

    @Test
    public void testRead() throws Exception {
        //testo che nel buffer che ho letto c'è effettivamente quello che avevo messo in Before (data)
        for (int i = 0; i < data.length; i++) {
            int b = inputBuffer.read();
            System.out.println("testRead:    b= "+b+"   data[i]= "+data[i]);
            assertEquals(data[i], (byte) b);
        }
        assertEquals(-1, inputBuffer.read());
    }
    @Test public void testReadArrayOffsetLength() throws Exception {
        System.out.println("testReadArrayOffsetLength:    inputBuffer[2,3]= "+inputBuffer.read(bs, 2, 1));
        assertEquals(1, inputBuffer.read(bs, 2, 1));
        byte[] expected =
                new byte[] { (byte) 1, (byte) 2, data[0], (byte) 4 };
        System.out.println("testReadArrayOffsetLength:    expected= "+expected.toString()+"    bs= "+bs.toString());
        assertArrayEquals(expected, bs);
    }
    @Test(expected=IndexOutOfBoundsException.class)
    public void testReadArrayOffsetLength_LengthTooLarge() throws Exception {
        inputBuffer.read(bs, 2, 3);
    }
    @Test public void testReadArrayOffsetLength_HitEndOfStream()
            throws Exception {
        for (int i = 0; i < data.length - 1; i++) {
            inputBuffer.read();
        }
        assertEquals(1, inputBuffer.read(bs, 2, 2));
        byte[] expected =
                new byte[] { (byte) 1, (byte) 2, data[data.length - 1],
                        (byte) 4 };
        assertArrayEquals(expected, bs);
    }
    @Test public void testReadArrayOffsetLength_AtEndOfStream()
            throws Exception {
        for (int i = 0; i < data.length; i++) {
            inputBuffer.read();
        }
        byte[] expected = Arrays.copyOf(bs, bs.length);
        assertEquals(-1, inputBuffer.read(bs, 2, 2));
        assertArrayEquals(expected, bs);
    }
    @Test public void testReadArrayOffsetLength_0Length() throws Exception {
        byte[] expected = Arrays.copyOf(bs, bs.length);
        assertEquals(0, inputBuffer.read(bs, 2, 0));
        assertArrayEquals(expected, bs);
    }
    @Test public void testReadArray() throws Exception {
        byte[] expected = Arrays.copyOf(data, 4);
        assertEquals(4, inputBuffer.read(bs));
        assertArrayEquals(expected, bs);
    }



}
