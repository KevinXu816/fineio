package com.fineio.io.file;

import com.fineio.accessor.buffer.Buf;
import com.fineio.accessor.file.IAppendFile;
import com.fineio.io.Buffer;
import com.fineio.storage.Connector;

import java.net.URI;

/**
 * @author yee
 * @date 2018/9/20
 */
public final class AppendIOFile<B extends Buf> extends BaseReadIOFile<B> implements IAppendFile<B> {
    private final boolean sync;

    AppendIOFile(Connector connector, URI uri, FileModel model, boolean sync) {
        super(connector, uri, model);
        this.sync = sync;
        int maxBlock = blocks - 1;
        if (maxBlock >= 0) {
            buffers[maxBlock] = initBuffer(maxBlock);
        }
    }

    public static final <E extends Buf> AppendIOFile<E> createFineIO(Connector connector, URI uri, FileModel model, boolean sync) {
        return new AppendIOFile<E>(connector, uri, model, sync);
    }

    @Override
    protected FileLevel getFileLevel() {
        return FileLevel.APPEND;
    }

    @Override
    protected Buffer initBuffer(int index) {

        synchronized (this) {
            Buffer buffer = buffers[index];
            if (buffer == null) {
                buffer = model.createBuffer(connector, createIndexBlock(index), block_size_offset, sync);
            } else {
                return buffer;
            }
            if (index == blocks - 1) {
                buffers[index] = buffer.asAppend();
            } else {
                buffers[index] = buffer.asWrite();
            }
            return buffers[index];
        }
    }

    @Override
    public final void close() {
        writeHeader();
        super.close();
    }
}