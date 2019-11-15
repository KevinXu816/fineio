package com.fineio.test.memory;

import com.fineio.FineIoTestBootstrap;
import com.fineio.exception.MemorySetException;
import com.fineio.v2.FineIO;
import junit.framework.TestCase;

/**
 * Created by daniel on 2017/2/13.
 */
public class MemoryConfTest extends TestCase {
    @Override
    public void setUp() throws Exception {
        FineIoTestBootstrap.boot();
    }

    public void testGetAndSet(){
        assertEquals(FineIO.getMinMemSizeForSet(), 1L<<30);
        long s = FineIO.getTotalMemSize();
        assertTrue(s >= FineIO.getMinMemSizeForSet());
        boolean exp = false;
        try {
            FineIO.setTotalMemSize(100);
        } catch (MemorySetException e) {
            exp = true;
        }
        assertTrue(exp);
        exp = false;
        try {
            FineIO.setTotalMemSize(Long.MAX_VALUE);
        } catch (MemorySetException e) {
            exp = true;
        }
        assertTrue(exp);
        assertTrue(s >= FineIO.getMinMemSizeForSet());
        assertTrue(s >= FineIO.getFreeMemory());
        assertEquals(FineIO.getCurrentMemorySize(), 0);
        assertEquals(FineIO.getCurrentReadMemorySize(), 0);
        assertEquals(FineIO.getCurrentWriteMemorySize(), 0);
        assertEquals(FineIO.getReadWaitCount(), 0);
        assertEquals(FineIO.getWriteWaitCount(), 0);
    }
}