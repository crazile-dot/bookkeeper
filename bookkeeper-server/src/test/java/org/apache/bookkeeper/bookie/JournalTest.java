package org.apache.bookkeeper.bookie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
//import jdk.jfr.Timestamp;
import org.junit.Test;
import java.util.Collection;
//import javax.management.ConstructorParameters;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import org.apache.bookkeeper.bookie.Journal.LastLogMark;
import org.apache.bookkeeper.bookie.Journal;
import org.apache.bookkeeper.client.ClientUtil;
import org.apache.bookkeeper.client.LedgerHandle;
import org.apache.bookkeeper.conf.TestBKConfiguration;
import org.apache.bookkeeper.conf.ServerConfiguration;
import org.apache.bookkeeper.bookie.LedgerDirsManager;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import java.io.File;
import org.apache.bookkeeper.util.IOUtils;
import org.apache.commons.io.FileUtils;

@RunWith(Parameterized.class)
public class JournalTest {
    File journalDir;
    //JournalIdFilter filter;
    long journalId;
    //segno su dove dobbiamo leggere
    long journalPos;
    long logId;
    long preAllocSize;
    int writeBufferSize;
    int journalAlignSize;
    long position;
    boolean fRemoveFromPageCache;
    //int formatVersionToWrite;
    ServerConfiguration server;
    //JournalScanner journalScanner;
    LedgerDirsManager ledgerDirsManager;
    final static List<File> tempDirs = new ArrayList<File>();
    
    
    private class DummyJournalScan implements Journal.JournalScanner {
    	
        @Override
        public void process(int journalVersion, long offset, ByteBuffer entry) throws IOException {
            System.out.println("Journal Version : " + journalVersion);
        }
    };
	
    
    static File createTempDir(String prefix, String suffix) throws IOException {
        File dir = IOUtils.createTempDir(prefix, suffix);
        tempDirs.add(dir);
        return dir;
    }
    
    @Parameters 
    public static Collection<Object[]> configure() throws Exception{
        return Arrays.asList(new Object[][]{
            {createTempDir("bookie", "journal"),1,1,1,4096,1024,1024,1,false,TestBKConfiguration.newServerConfiguration(),mock(LedgerDirsManager.class)},{createTempDir("bookie", "journal"),1,1,1,4096,1024,1024,-1,true,TestBKConfiguration.newServerConfiguration(),mock(LedgerDirsManager.class)},{createTempDir("bookie", "journal"),0,0,0,512,512,1024,1,false,TestBKConfiguration.newServerConfiguration().setBookieId("bookie"),mock(LedgerDirsManager.class)}
        });
    }


    public JournalTest(File journalDir,long journalId,long journalPos,long logId,long preAllocSize,
    int writeBufferSize, int journalAlignSize, long position, boolean fRemoveFromPageCache, ServerConfiguration server,LedgerDirsManager ledgerDirsManager){
    	this.journalDir = journalDir;
        //JournalIdFilter filter;
        this.journalId = journalId;
        this.journalPos = journalPos;
        this.logId = logId;
        this.preAllocSize = preAllocSize;
        this.writeBufferSize = writeBufferSize;
        this.journalAlignSize = journalAlignSize;
        this.position = position;
        this.fRemoveFromPageCache = fRemoveFromPageCache;
        //int formatVersionToWrite;
        this.server = server;
        //this.journalScanner = journalScanner;
        this.ledgerDirsManager = ledgerDirsManager;
    }

    @Test 
    public void scan_Test() throws Exception {
        Journal journal = new Journal(0, journalDir, server, ledgerDirsManager);
        Journal.JournalScanner scanner = new DummyJournalScan();
    	List<Long> journalIds = journal.listJournalIds(journal.getJournalDirectory(), null);
        long off = journal.scanJournal(journalId,journalPos,scanner);
        assertTrue(off >= 0);
    }
}
