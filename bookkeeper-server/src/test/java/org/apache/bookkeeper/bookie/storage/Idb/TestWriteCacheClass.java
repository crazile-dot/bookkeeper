package org.apache.bookkeeper.bookie.storage.Idb;

import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import org.apache.bookkeeper.bookie.TestInput;
import org.apache.bookkeeper.bookie.storage.ldb.WriteCache;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.nio.ByteBuffer;
import java.util.ArrayList;
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

    public TestWriteCacheClass(TestInput params) {
        this.allocator = params.getAllocator();
        this.maxCacheSize = params.getMaxCacheSize();
        this.maxSegmentSize = params.getMaxSegmentSize();
        this.ledgerId = params.getLedgerId();
        this.entryId = params.getEntryId();
        this.bufSize = params.getBufSize();
        //this.entry = params.getEntry();
    }

    @Parameterized.Parameters
    public static Collection<TestInput> configure() throws Exception {
        Collection<TestInput> params = new ArrayList<>();
        params.add(new TestInput(new PooledByteBufAllocator(), 512, 1024, 1, 1, 64));
        params.add(new TestInput(new PooledByteBufAllocator(), 1024, 1024, 1, 1, 128));
        params.add(new TestInput(new PooledByteBufAllocator(), 512, 1024, 1, 1, 32));
        params.add(new TestInput(new PooledByteBufAllocator(), 1024, 512, 1, 1, 0));
        params.add(new TestInput(null, 128, 128, 0, 0, 0));

        return params;
    }

    @Test
    public void putTest() {
        WriteCache writeCache = new WriteCache(this.allocator, this.maxCacheSize, this.maxSegmentSize);
        ByteBuf newEntry = null;
        if (this.allocator != null) {
             newEntry = this.allocator.buffer(this.bufSize, this.bufSize);
        }
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
        ByteBuf newEntry = null;
        ByteBuf ret = null;

        if (this.allocator != null) {
            newEntry = this.allocator.buffer(this.bufSize, this.bufSize);
        }
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
            //assertTrue(ret.isError());
        }
    }

    @Test
    public void getLastEntryTest() {
        ByteBuf res = null;
        ByteBuf last = null;
        if(this.allocator != null) {
            res = this.allocator.buffer();
            last = this.allocator.buffer();
        }
        WriteCache writeCache = new WriteCache(this.allocator, this.maxCacheSize, this.maxSegmentSize);
        //writeCache.clear();
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
            //assertTrue(res.isError());
        }
    }

    @Test
    public void clearTest() {
        WriteCache writeCache = new WriteCache(this.allocator, this.maxCacheSize, this.maxSegmentSize);
        writeCache.clear();
        assertEquals(writeCache.size(), 0L);
        assertEquals(writeCache.count(), 0L);
        assertTrue(writeCache.isEmpty());
    }
}
