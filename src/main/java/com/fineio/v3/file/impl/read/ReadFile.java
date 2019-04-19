package com.fineio.v3.file.impl.read;


import com.fineio.logger.FineIOLoggers;
import com.fineio.v3.buffer.DirectBuffer;
import com.fineio.v3.connector.Connector;
import com.fineio.v3.file.FileKey;
import com.fineio.v3.file.impl.File;
import com.fineio.v3.memory.MemoryUtils;
import com.fineio.v3.memory.Offset;

import java.io.BufferedInputStream;
import java.io.InputStream;

/**
 * @author anchore
 * @date 2019/4/16
 */
abstract class ReadFile<B extends DirectBuffer> extends File<B> {
    ReadFile(FileKey fileKey, Offset offset, Connector connector) {
        super(fileKey, offset, connector);
    }

    @Override
    protected B getBuffer(int nthBuf) {
        return buffers.computeIfAbsent(nthBuf, this::loadBuffer);
    }

    private B loadBuffer(int nthBuf) {
        // TODO: 2019/4/16 anchore 先拿cache，拿不到就生成buffer，put进cache
        Long address = null;
        try (InputStream input = new BufferedInputStream(connector.read(new FileKey(fileKey.getPath(), String.valueOf(nthBuf))))) {
            int avail = input.available();
            address = MemoryUtils.allocate(avail);
            long ptr = address;
            byte[] bytes = new byte[1024];
            for (int read; (read = input.read(bytes)) != -1; ptr += read) {
                MemoryUtils.copyMemory(bytes, ptr, read);
            }
            return newDirectBuf(address, (int) ((ptr - address) >> offset.getOffset()), fileKey);
        } catch (Throwable e) {
            if (address != null) {
                MemoryUtils.free(address);
            }
            FineIOLoggers.getLogger().error(e);
            return null;
        }
    }

    abstract B newDirectBuf(long address, int size, FileKey fileKey);

    @Override
    public void close() {
        if (closed.compareAndSet(false, true)) {
            // TODO: 2019/4/15 anchore read file直接归还buffer给cache，由cache管理buffer生命周期
            buffers.clear();
        }
    }
}