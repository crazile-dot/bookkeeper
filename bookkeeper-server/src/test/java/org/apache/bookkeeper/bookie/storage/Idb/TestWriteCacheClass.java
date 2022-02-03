import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate;
/*import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.apache.bookkeeper.bookie.storage.ldb.WriteCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collection;
//import java.util.HexFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;


@RunWith(Parameterized.class)
public class TestWriteCacheClass {

    private ByteBufAllocator allocator;
    private long maxCacheSize;
    private int maxSegmentSize;
    private long ledgerId;
    private long entryId;
    private int bufSize;
    //private ByteBuf entry;

    private long[] entryList;
    private ByteBuf[] bufList;

    public TestWriteCacheClass(Params params) {
        this.allocator = params.getAllocator();
        this.maxCacheSize = params.getMaxCacheSize();
        this.maxSegmentSize = params.getMaxSegmentSize();
        this.ledgerId = params.getLedgerId();
        this.entryId = params.getEntryId();
        this.bufSize = params.getBufSize();
        //this.entry = params.getEntry();
    }

    @Parameterized.Parameters
    public static Collection<Object[]> configure() throws Exception {
        return Arrays.asList(new Object[][]{ });
    }

    @Test
    public void putTest() {
        WriteCache writeCache = new WriteCache(this.allocator, this.maxCacheSize, this.maxSegmentSize);
        ByteBuf newEntry = this.allocator.buffer(this.bufSize, this.bufSize);
        boolean bool1 = false;
        boolean bool2 = false;
        try {
            bool1 = writeCache.put(this.ledgerId, this.entryId, newEntry);
            bool2 = writeCache.put(this.ledgerId, this.entryId+1, newEntry);
            assertEquals(writeCache.get(this.ledgerId, this.entryId), writeCache.get(this.ledgerId, this.entryId+1));
            assertTrue(bool1);
            assertTrue(bool2);

        } catch(Exception e) {
            System.out.println(e.getStackTrace());
            assertFalse(bool1);
            assertFalse(bool2);
        }

    }

    @Test
    public void getTest() {
        WriteCache writeCache = new WriteCache(this.allocator, this.maxCacheSize, this.maxSegmentSize);
        ByteBuf newEntry = this.allocator.buffer(this.bufSize, this.bufSize);
        ByteBuf ret = null;
        try {
            if(writeCache.size() == 0) {
                assertNull(writeCache.get(this.ledgerId, this.entryId));
                writeCache.put(this.ledgerId, this.entryId, newEntry);
                ret = writeCache.get(this.ledgerId, this.entryId);
                assertEquals(ret, newEntry);
            } else {
                ret = writeCache.get(this.ledgerId, this.entryId);
                assertNotEquals(ret, null);
            }

        } catch(Exception e) {
            System.out.println(e.getStackTrace());
            assertTrue(ret.isError());
        }
    }

    @Test
    public void getLastEntryTest() {
        ByteBuf res = this.allocator.buffer();
        WriteCache writeCache = new WriteCache(this.allocator, this.maxCacheSize, this.maxSegmentSize);
        //writeCache.clear();
        ByteBuf last = this.allocator.buffer();
        long i = 0L;
        try {
            if(writeCache.size() == 0) {
                for (ByteBuf elem : this.bufList) {
                    writeCache.put(this.ledgerId, this.entryId + i, elem);
                    i++;
                    last = elem;
                }
                res = writeCache.getLastEntry(this.ledgerId);
                assertEquals(res, last);
            } else {
                res = writeCache.getLastEntry(this.ledgerId);
                assertNotEquals(res, null);
            }
        } catch (Exception e) {
            System.out.println(e.getStackTrace());
            assertTrue(res.isError());
        }
    }

    @Test
    public void clearTest() {

    }
}*/
