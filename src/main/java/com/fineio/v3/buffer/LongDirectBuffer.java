package com.fineio.v3.buffer;

import com.fineio.accessor.buffer.LongBuf;

/**
 * @author anchore
 * @date 2019/4/11
 */
public interface LongDirectBuffer extends DirectBuffer, LongBuf {
    void putLong(int pos, long val)
            throws BufferClosedException, BufferAllocateFailedException,
            BufferOutOfBoundsException;

    long getLong(int pos)
            throws BufferClosedException, BufferOutOfBoundsException;
}