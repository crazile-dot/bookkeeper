import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.ByteBuffer;

public class Params {
    private ByteBuffer[] testBuf;
    private long position;
    private byte[] masterKey;
    private int fileInfoVersionToWrite;
    private int size;
    private boolean bestEffort;
    private long fileSize;

    private ByteBufAllocator allocator;
    private long maxCacheSize;
    private int maxSegmentSize;
    private long ledgerId;
    private long entryId;
    //private ByteBuf entry;
    private int bufSize;

    public Params(
            ByteBuffer[] testBuf, long position, byte[] masterkey, int fileInfoVersionToWrite,
            int size, boolean bestEffort, long fileSize) {
        this.testBuf = testBuf;
        this.position = position;
        this.masterKey = masterkey;
        this.fileInfoVersionToWrite = fileInfoVersionToWrite;
        this.size = size;
        this.bestEffort = bestEffort;
        this.fileSize = fileSize;
    }


    public Params(ByteBufAllocator allocator, long maxCacheSize, int maxSegmentSize, long ledgerId, long entryId, int bufSize) {
        this.allocator = allocator;
        this.maxCacheSize = maxCacheSize;
        this.maxSegmentSize = maxSegmentSize;
        this.ledgerId = ledgerId;
        this.entryId = entryId;
        //this.entry = entry;
        this.bufSize = bufSize;
    }

    public ByteBuffer[] getTestBuf() {
        return testBuf;
    }

    public long getPosition() {
        return position;
    }

    public byte[] getMasterKey() {
        return masterKey;
    }

    public int getFileInfoVersionToWrite() {
        return fileInfoVersionToWrite;
    }

    public int getSize() {
        return size;
    }

    public boolean isBestEffort() {
        return bestEffort;
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getLedgerId() {
        return ledgerId;
    }

    public long getEntryId() {
        return entryId;
    }

    public int getBufSize() {
        return bufSize;
    }

    public ByteBufAllocator getAllocator() {
        return allocator;
    }

    public long getMaxCacheSize() {
        return maxCacheSize;
    }

    public int getMaxSegmentSize() {
        return maxSegmentSize;
    }


}
