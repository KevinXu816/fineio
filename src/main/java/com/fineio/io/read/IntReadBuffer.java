package com.fineio.io.read;

/**
 * @author yee
 * @date 2018/6/1
 */
public interface IntReadBuffer extends ReadOnlyBuffer {
    int get(int pos);
}