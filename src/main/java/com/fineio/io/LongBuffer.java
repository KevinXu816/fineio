package com.fineio.io;

/**
 * @author yee
 * @version 1.0
 * Created by yee on 2019/9/10
 */
public interface LongBuffer extends Buffer {
    long getLong(int pos);

    void putLong(int pos, long v);
}
