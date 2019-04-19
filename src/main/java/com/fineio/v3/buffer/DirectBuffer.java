package com.fineio.v3.buffer;

import com.fineio.v3.file.FileKey;

import java.io.Closeable;

/**
 * @author anchore
 * @date 2019/4/16
 */
public interface DirectBuffer extends Closeable {
    FileKey getFileKey();

    long getAddress();

    int getSizeInBytes();

    @Override
    void close();
}