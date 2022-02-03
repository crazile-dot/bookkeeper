import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.apache.bookkeeper.bookie.FileInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
import java.util.HexFormat;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class TestFileInfoClass {

    private ByteBuffer[] testBuf;
    private long position;
    private static File lf;
    private static byte[] masterKey;
    private int fileInfoVersionToWrite;
    private int size;
    private boolean bestEffort;
    private long fileSize;

    private static String pathname = "C:\\Users\\Ilenia\\Desktop\\test.txt";

    public TestFileInfoClass(Params params) {
        this.testBuf = params.getTestBuf();
        this.position = params.getPosition();
        this.masterKey = params.getMasterKey();
        this.fileInfoVersionToWrite = params.getFileInfoVersionToWrite();
        this.size = params.getSize();
        this.bestEffort = params.isBestEffort();
        this.fileSize = params.getFileSize();

    }

    @Parameterized.Parameters
    public static Collection<Object[]> configure() throws Exception {
        ByteBuffer buf = ByteBuffer.allocate(1024);
        fill(buf, 0, 1024, (byte)0L);
        masterKey = HexFormat.of().parseHex("e04fd020ea3a6910a2d808002b30309d");
        return Arrays.asList(new Object[][]{
                new Params(buf, 0L, masterKey, FileInfo.V1, 1024, true, 2048),
                new Params (buf, 0L, Unpooled.buffer(0), FileInfo.V1, 55, false, 1024),
        });
    }

    public static void fill(ByteBuffer buffer, int off, int len, byte val) {
        buffer.position(off);
        while (--len >= 0)
            buffer.put(val);
    }

    @Before
    public void createFile() {
        try {
            File file = new File("demo1.txt");
            file.createNewFile();
            System.out.println("File: " + file);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void deleteFile() {
        try {
            File f = new File("demo.txt");
            if(f.delete()) {
                System.out.println(f.getName() + " deleted");
            } else {
                System.out.println("failed");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void writeTest() {
        long numWritten = 0;
        try {
            FileInfo fileInfo = new FileInfo(lf, masterKey, this.fileInfoVersionToWrite);
            numWritten = fileInfo.write(this.testBuf, this.position);
            assertTrue(numWritten > 0);
            assertTrue(fileInfo.size() > 0);
            assertFalse(numWritten.isError());
            assertEquals(numWritten, fileInfo.size());
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            assertTrue(numWritten.isError());
        }
    }

    @Test
    public void readTest() {
        int numRead = 0;
        ByteBuffer byteBuffer = ByteBuffer.allocate(this.size);
        try {
            FileInfo fileInfo = new FileInfo(lf, masterKey, this.fileInfoVersionToWrite);
            numRead = fileInfo.read(byteBuffer, this.position, this.bestEffort);
            if(this.bestEffort) {
                assertNotEquals(numRead.getClass(), ShortReadException.class);
            } else {
                assertEquals(numRead.getClass(), ShortReadException.class);
            }
            assertTrue(numRead > 0);
            assertEquals(byteBuffer, numRead);
            assertFalse(numRead.isError());

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            assertTrue(numRead.isError());
        }
    }

    @Test
    public void moveToNewLocationTest() {
        try {
            long numWritten = 0;
            File dest = new File(pathname);

            FileInfo fileInfo = new FileInfo(lf, masterKey, this.fileInfoVersionToWrite);
            numWritten = fileInfo.write(this.testBuf, this.position);

            assertTrue(dest.exists());
            assertTrue(lf.exists());

            String text1 = readFile(lf);
            assertTrue(text1.length() > 0);
            //assertEquals(text1.getClass(), String.class);

            fileInfo.moveToNewLocation(dest, this.fileSize);
            String text2 = readFile(dest);
            assertTrue(text2.length() > 0);
            //assertEquals(text2.getClass(), String.class);
            assertEquals(text1, text2);

            assertTrue(dest.exists());
            if (lf.exists()) {
                assertTrue(readFile(lf).length() < 1);
            } else {
                assertFalse(lf.exists());
            }


        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            //assertEquals(e.getClass(), IOException.class);
        }

    }

    private String readFile(File file) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;

            while ((st = br.readLine()) != null)
                result += st;
                System.out.println(st);
        } catch (FileNotFoundException e) {
            System.out.println(e.getStackTrace());
        } catch (IOException ioe) {
            System.out.println(ioe.getStackTrace());
        }
        return result;
    }
}



