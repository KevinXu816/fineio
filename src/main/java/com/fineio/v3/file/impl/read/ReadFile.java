package com.fineio.v3.file.impl.read;


import com.fineio.accessor.FileMode;
import com.fineio.accessor.file.IReadFile;
import com.fineio.io.file.FileBlock;
import com.fineio.logger.FineIOLoggers;
import com.fineio.storage.Connector;
import com.fineio.v3.buffer.BufferAcquireFailedException;
import com.fineio.v3.buffer.DirectBuffer;
import com.fineio.v3.file.impl.BufferCache;
import com.fineio.v3.file.impl.File;
import com.fineio.v3.memory.MemoryManager;
import com.fineio.v3.memory.MemoryUtils;
import com.fineio.v3.memory.Offset;
import com.fineio.v3.utils.IOUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author anchore
 * @date 2019/4/16
 */
abstract class ReadFile<B extends DirectBuffer> extends File<B> implements IReadFile<B> {
    ReadFile(FileBlock fileBlock, Offset offset, Connector connector) {
        super(fileBlock, offset, connector);
    }

    @Override
    protected B getBuffer(int nthBuf) {
        return loadBuffer(nthBuf);
    }

    private B loadBuffer(int nthBuf) {
        // TODO: 2019/4/16 anchore 先拿cache，拿不到就生成buffer，put进cache
        FileBlock nthFileBlock = new FileBlock(fileBlock.getPath(), String.valueOf(nthBuf));

        DirectBuffer buf = BufferCache.get().get(nthFileBlock, fb -> {
            Long address = null;
            int size = 0;
            try (InputStream input = new BufferedInputStream(connector.read(fb));
                 ByteArrayOutputStream byteOutput = new ByteArrayOutputStream()) {
                IOUtils.copyBinaryTo(input, byteOutput);
                size = byteOutput.size();
                address = MemoryManager.INSTANCE.allocate(size, FileMode.READ);
                MemoryUtils.copyMemory(byteOutput.toByteArray(), address, size);

                return newDirectBuf(address, size >> offset.getOffset(), fb);
            } catch (Throwable e) {
                if (address != null) {
                    MemoryManager.INSTANCE.release(address, size);
                }
                FineIOLoggers.getLogger().error(e);
                return null;
            }
        });
        if (buf == null) {
            throw new BufferAcquireFailedException(fileBlock);
        }
        return (B) buf;
    }

    abstract B newDirectBuf(long address, int size, FileBlock fileBlock);

    @Override
    public void close() {
        closed.compareAndSet(false, true);
    }
}