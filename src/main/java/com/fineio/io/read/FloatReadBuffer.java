package com.fineio.io.read;

import com.fineio.io.FloatBuffer;
import com.fineio.io.file.FileBlock;
import com.fineio.io.file.ReadModel;
import com.fineio.memory.MemoryConstants;
import com.fineio.memory.MemoryUtils;
import com.fineio.storage.Connector;

import java.net.URI;

public final class FloatReadBuffer extends ReadBuffer implements FloatBuffer {
    public static final ReadModel MODEL;

    static {
        MODEL = new ReadModel<FloatBuffer>() {
            @Override
            protected final FloatReadBuffer createBuffer(final Connector connector, final FileBlock fileBlock, final int n) {
                return new FloatReadBuffer(connector, fileBlock, n);
            }

            @Override
            public final FloatReadBuffer createBuffer(final Connector connector, final URI uri) {
                return new FloatReadBuffer(connector, uri);
            }

            @Override
            protected final byte offset() {
                return MemoryConstants.OFFSET_FLOAT;
            }
        };
    }

    private FloatReadBuffer(final Connector connector, final FileBlock fileBlock, final int n) {
        super(connector, fileBlock, n);
    }

    private FloatReadBuffer(final Connector connector, final URI uri) {
        super(connector, uri);
    }

    @Override
    protected int getLengthOffset() {
        return MemoryConstants.OFFSET_FLOAT;
    }

    @Override
    public final float get(final int n) {
        this.checkIndex(n);
        return MemoryUtils.getFloat(this.address, n);
    }
}
