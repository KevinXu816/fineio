package com.fineio.io.write;

import com.fineio.file.FileBlock;
import com.fineio.io.CharBuffer;
import com.fineio.memory.MemoryUtils;
import com.fineio.storage.Connector;

/**
 * Created by daniel on 2017/2/14.
 */
public final  class CharWriteBuffer extends WriteBuffer  implements CharBuffer{

    private CharWriteBuffer(Connector connector, FileBlock block, int max_offset) {
        super(connector, block, max_offset);
    }

    protected int getLengthOffset() {
        return OFFSET;
    }

    /**
     *
     * @param position 位置
     * @param b 值
     */
    public  final void put(int position, char b) {
        ensureCapacity(position);
        MemoryUtils.put(address, position, b);
    }

    public final char get(int p) {
        checkIndex(p);
        return MemoryUtils.getChar(address, p);
    }
}
