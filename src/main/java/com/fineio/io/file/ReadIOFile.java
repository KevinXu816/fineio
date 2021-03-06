package com.fineio.io.file;

import com.fineio.io.Buffer;
import com.fineio.io.ByteBuffer;
import com.fineio.io.CharBuffer;
import com.fineio.io.DoubleBuffer;
import com.fineio.io.FloatBuffer;
import com.fineio.io.IntBuffer;
import com.fineio.io.LongBuffer;
import com.fineio.io.ShortBuffer;
import com.fineio.io.read.ByteReadBuffer;
import com.fineio.io.read.CharReadBuffer;
import com.fineio.io.read.DoubleReadBuffer;
import com.fineio.io.read.FloatReadBuffer;
import com.fineio.io.read.IntReadBuffer;
import com.fineio.io.read.LongReadBuffer;
import com.fineio.io.read.ShortReadBuffer;
import com.fineio.storage.Connector;

import java.net.URI;

public final class ReadIOFile<T extends Buffer> extends AbstractReadIOFile<T> {
    public static final ReadModel<ByteBuffer> BYTE;
    public static final ReadModel<DoubleBuffer> DOUBLE;
    public static final ReadModel<LongBuffer> LONG;
    public static final ReadModel<IntBuffer> INT;
    public static final ReadModel<FloatBuffer> FLOAT;
    public static final ReadModel<CharBuffer> CHAR;
    public static final ReadModel<ShortBuffer> SHORT;

    static {
        BYTE = ByteReadBuffer.MODEL;
        DOUBLE = DoubleReadBuffer.MODEL;
        LONG = LongReadBuffer.MODEL;
        INT = IntReadBuffer.MODEL;
        FLOAT = FloatReadBuffer.MODEL;
        CHAR = CharReadBuffer.MODEL;
        SHORT = ShortReadBuffer.MODEL;
    }

    private ReadIOFile(final Connector connector, final URI uri, final ReadModel<T> readModel) {
        super(connector, uri, readModel);
    }

    @Override
    protected void writeHeader() {
    }

    public static final <E extends Buffer> ReadIOFile<E> createFineIO(final Connector connector, final URI uri, final ReadModel<E> readModel) {
        return new ReadIOFile<E>(connector, uri, readModel);
    }
}
