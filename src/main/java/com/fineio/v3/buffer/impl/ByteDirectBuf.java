package com.fineio.v3.buffer.impl;

import com.fineio.v3.buffer.ByteDirectBuffer;
import com.fineio.v3.file.FileKey;
import com.fineio.v3.memory.MemoryUtils;
import com.fineio.v3.memory.Offset;
import com.fineio.v3.type.FileMode;

/**
 * @author anchore
 * @date 2019/4/11
 */
public class ByteDirectBuf extends BaseDirectBuffer implements ByteDirectBuffer {
    /**
     * for write
     *
     * @param fileKey file key
     */
    public ByteDirectBuf(FileKey fileKey, int maxCap, FileMode fileMode) {
        super(fileKey, Offset.BYTE, maxCap, fileMode);
    }

    /**
     * for read
     *
     * @param address 地址
     * @param cap     容量
     * @param fileKey file key
     */
    public ByteDirectBuf(long address, int cap, FileKey fileKey, int maxCap, FileMode fileMode) {
        super(address, cap, fileKey, Offset.BYTE, maxCap, fileMode);
    }

    @Override
    public void putByte(int pos, byte val) {
        ensureOpen();
        ensureCap(pos);
        checkPos(pos);
        MemoryUtils.put(address, pos, val);
        updateSize(pos);
    }

    @Override
    public byte getByte(int pos) {
        ensureOpen();
        checkPos(pos);
        return MemoryUtils.getByte(address, pos);
    }
}