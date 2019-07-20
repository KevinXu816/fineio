package com.fineio.v3.file.impl.write;

import com.fineio.accessor.FileMode;
import com.fineio.io.file.FileBlock;
import com.fineio.storage.Connector;
import com.fineio.v3.buffer.IntDirectBuffer;
import com.fineio.v3.buffer.impl.IntDirectBuf;
import com.fineio.v3.memory.Offset;

/**
 * @author anchore
 * @date 2019/4/3
 */
public class IntWriteFile extends WriteFile<IntDirectBuffer> {
    public IntWriteFile(FileBlock fileBlock, Connector connector, boolean asyncWrite) {
        super(fileBlock, Offset.INT, connector, asyncWrite);
        buffers = new IntDirectBuffer[16];
    }

    public static IntWriteFile ofAsync(FileBlock fileBlock, Connector connector) {
        return new IntWriteFile(fileBlock, connector, true);
    }

    public static IntWriteFile ofSync(FileBlock fileBlock, Connector connector) {
        return new IntWriteFile(fileBlock, connector, false);
    }

    public void putInt(int pos, int value) {
        ensureOpen();
        int nthBuf = nthBuf(pos);
        syncBufIfNeed(nthBuf);
        int nthVal = nthVal(pos);
        try {
            buffers[nthBuf].putInt(nthVal, value);
        } catch (NullPointerException e) {
            // buffers[nthBuf]为null，对应写完一个buffer的情况
            newAndPut(nthBuf, nthVal, value);
        } catch (ArrayIndexOutOfBoundsException e) {
            // buffers数组越界，对应当前buffers全写完的情况，也考虑了负下标越界
            growBufferCache(nthBuf);
            newAndPut(nthBuf, nthVal, value);
        }
        updateLastPos(pos);
    }

    private void newAndPut(int nthBuf, int nthVal, int value) {
        buffers[nthBuf] = new IntDirectBuf(new FileBlock(fileBlock.getPath(), String.valueOf(nthBuf)),
                1 << (connector.getBlockOffset() - offset.getOffset()), FileMode.WRITE);
        buffers[nthBuf].putInt(nthVal, value);
    }

}