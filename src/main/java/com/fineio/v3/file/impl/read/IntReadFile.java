package com.fineio.v3.file.impl.read;

import com.fineio.io.file.FileBlock;
import com.fineio.storage.Connector;
import com.fineio.v3.buffer.IntDirectBuffer;
import com.fineio.v3.buffer.impl.IntDirectBuf;
import com.fineio.v3.file.FileClosedException;
import com.fineio.v3.memory.Offset;

/**
 * @author anchore
 * @date 2019/4/12
 */
public class IntReadFile extends ReadFile<IntDirectBuffer> {
    public IntReadFile(FileBlock fileBlock, Connector connector) {
        super(fileBlock, Offset.INT, connector);
    }

    public int getInt(long pos) throws FileClosedException, IllegalArgumentException {
        ensureOpen();
        checkPos(pos);
        return getBuffer(nthBuf(pos)).getInt(nthVal(pos));
    }

    @Override
    IntDirectBuffer newDirectBuf(long address, int size, FileBlock fileBlock) {
        return new IntDirectBuf(address, size, fileBlock, size);
    }
}