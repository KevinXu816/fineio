package com.fineio.v3.file.impl;

import com.fineio.accessor.file.IFile;
import com.fineio.io.file.FileBlock;
import com.fineio.logger.FineIOLoggers;
import com.fineio.storage.Connector;
import com.fineio.v3.buffer.DirectBuffer;
import com.fineio.v3.file.FileClosedException;
import com.fineio.v3.memory.Offset;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author anchore
 * @date 2019/4/16
 */
public abstract class File<B extends DirectBuffer> implements Closeable, IFile<B> {
    private static final String LAST_POS = "last_pos";

    protected final FileBlock fileBlock;

    protected final Connector connector;

    protected final AtomicBoolean closed = new AtomicBoolean(false);

    protected final Offset offset;

    protected B[] buffers;

    protected File(FileBlock fileBlock, Offset offset, Connector connector) {
        this.fileBlock = fileBlock;
        this.offset = offset;
        this.connector = connector;
    }

    protected int nthBuf(int pos) {
        return pos >> (connector.getBlockOffset() - offset.getOffset());
    }

    protected int nthVal(int pos) {
        return (int) (pos & ((1L << connector.getBlockOffset() - offset.getOffset()) - 1));
    }

    protected void ensureOpen() {
        if (closed.get()) {
            throw new FileClosedException(fileBlock);
        }
    }

    @Override
    public boolean exists() {
        return connector.exists(new FileBlock(fileBlock.getPath(), LAST_POS));
    }

    @Override
    public abstract void close();

    protected static void writeLastPos(File<?> file, int lastPos) {
        byte[] bytes = ByteBuffer.allocate(4).putInt(lastPos).array();
        try {
            file.connector.write(new FileBlock(file.fileBlock.getPath(), LAST_POS), bytes);
        } catch (IOException e) {
            FineIOLoggers.getLogger().error(e);
        }
    }

    protected static int getLastPos(File<?> file) {
        FileBlock lastPosFileKey = new FileBlock(file.fileBlock.getPath(), LAST_POS);
        if (file.connector.exists(lastPosFileKey)) {
            try (InputStream input = file.connector.read(lastPosFileKey)) {
                byte[] bytes = new byte[4];
                if (input.read(bytes) == bytes.length) {
                    return ByteBuffer.wrap(bytes).getInt();
                }
            } catch (IOException e) {
                FineIOLoggers.getLogger().error(e);
            }
        }
        return 0;
    }
}