package com.fineio.io.edit;

import com.fineio.io.DoubleBuffer;
import com.fineio.io.file.EditModel;
import com.fineio.io.file.FileBlock;
import com.fineio.memory.MemoryConstants;
import com.fineio.memory.MemoryUtils;
import com.fineio.storage.Connector;

import java.net.URI;

public final class DoubleEditBuffer extends EditBuffer implements DoubleBuffer {
    public static final EditModel MODEL;

    static {
        MODEL = new EditModel<DoubleBuffer>() {
            @Override
            protected final DoubleEditBuffer createBuffer(final Connector connector, final FileBlock fileBlock, final int n) {
                return new DoubleEditBuffer(connector, fileBlock, n);
            }

            @Override
            public final DoubleEditBuffer createBuffer(final Connector connector, final URI uri) {
                return new DoubleEditBuffer(connector, uri);
            }

            @Override
            protected final byte offset() {
                return MemoryConstants.OFFSET_DOUBLE;
            }
        };
    }

    private DoubleEditBuffer(final Connector connector, final FileBlock fileBlock, final int n) {
        super(connector, fileBlock, n);
    }

    private DoubleEditBuffer(final Connector connector, final URI uri) {
        super(connector, uri);
    }

    @Override
    protected int getLengthOffset() {
        return MemoryConstants.OFFSET_DOUBLE;
    }

    @Override
    public final double get(final int n) {
        this.checkIndex(n);
        return MemoryUtils.getDouble(this.address, n);
    }

    @Override
    public final void put(final double n) {
        this.put(++this.max_position, n);
    }

    @Override
    public final void put(final int n, final double n2) {
        this.ensureCapacity(n);
        this.judeChange(n, n2);
        MemoryUtils.put(this.address, n, n2);
    }

    private final void judeChange(final int n, final double n2) {
        if (!this.changed && Double.compare(n2, this.get(n)) != 0) {
            this.changed = true;
        }
    }
}
