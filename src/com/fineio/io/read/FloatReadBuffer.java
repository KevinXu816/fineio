package com.fineio.io.read;

import com.fineio.file.FileBlock;
import com.fineio.memory.MemoryConstants;
import com.fineio.memory.MemoryUtils;
import com.fineio.storage.Connector;

/**
 * Created by daniel on 2017/2/14.
 */
public class FloatReadBuffer extends ReadBuffer {
    private FloatReadBuffer(Connector connector, FileBlock block) {
        super(connector, block);
    }

    protected int getLengthOffset() {
        return MemoryConstants.OFFSET_FLOAT;
    }

    public final float get(int p) {
        checkIndex(p);
        return MemoryUtils.getFloat(address, p);
    }
}
