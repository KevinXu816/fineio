package com.fineio.cache.pool;

import com.fineio.exception.FileCloseException;
import com.fineio.io.AbstractBuffer;

import java.net.URI;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author yee
 * @date 2018/5/31
 */
public class BufferPool<Buffer extends com.fineio.io.Buffer> {
    /**
     * 默认10分钟清理超时buffer
     */
    private static final long DEFAULT_TIMER_TIME = 600000;
    private static final int ACTIVE_PERCENT = 10;
    private volatile long timeout = DEFAULT_TIMER_TIME;
    private PooledBufferMap<Buffer> map = new PooledBufferMap<Buffer>();
    private Timer timer = new Timer("FineIOCleanTimer");
    private Timer activeTimer = new Timer("FineIOBufferActiveTimer");

    public BufferPool() {
        timer.schedule(createTimeoutTask(), timeout, timeout);
        activeTimer.schedule(createBufferActiveTask(), timeout / ACTIVE_PERCENT, timeout / ACTIVE_PERCENT);
    }

    public void registerBuffer(Buffer buffer) {
        map.put(buffer);
    }

    public Buffer getBuffer(URI uri) {
        return map.get(uri);
    }

    private TimerTask createBufferActiveTask() {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    resetAccess(map);
                    //10秒内有访问全部命中
                    Thread.sleep(10000);
                    activeAccess(map);
                } catch (Throwable e) {
                    //doNothing
                }
            }


            private void activeAccess(PooledBufferMap<Buffer> map) {
                Iterator<Buffer> iterator = map.iterator();
                while (iterator.hasNext()) {
                    Buffer buffer = iterator.next();
                    if (buffer != null && buffer.recentAccess()) {
                        try {
                            map.updateBuffer(buffer);
                        } catch (FileCloseException e) {
                            //file closed
                        }
                    }
                }
            }

            private void resetAccess(PooledBufferMap<Buffer> map) {
                Iterator<Buffer> iterator = map.iterator();
                while (iterator.hasNext()) {
                    Buffer buffer = iterator.next();
                    if (buffer != null) {
                        buffer.resetAccess();
                    }
                }
            }
        };
    }

    //定时清超时任务的task
    private TimerTask createTimeoutTask() {
        return new TimerTask() {
            @Override
            public void run() {
                removeTimeout(map);
            }

            private void removeTimeout(PooledBufferMap<Buffer> map) {
                Iterator<Buffer> iterator = map.iterator();
                while (iterator.hasNext()) {
                    Buffer buffer = iterator.next();
                    if (buffer != null) {
                        if (map.getIdle(buffer) > timeout) {
                            ((AbstractBuffer) buffer).unReference();
                        }
                    }
                }
            }
        };
    }

    /**
     * 重设超时时间
     *
     * @param t
     */
    public synchronized void resetTimer(long t) {
        timeout = t;
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer("FineIOCleanTimer");
        timer.schedule(createTimeoutTask(), timeout, timeout);
        if (activeTimer != null) {
            activeTimer.cancel();
        }
        activeTimer = new Timer("FineIOBufferActiveTimer");
        activeTimer.schedule(createBufferActiveTask(), timeout / ACTIVE_PERCENT, timeout / ACTIVE_PERCENT);
    }

    public void remove(Buffer buffer) {
        map.remove(buffer);
    }

    public Buffer getIdleBuffer() {
        Iterator<Buffer> iterator = map.iterator();
        while (iterator.hasNext()) {
            Buffer buffer = iterator.next();
            if (buffer != null) {
                if (map.getIdle(buffer) > timeout) {
                    return buffer;
                }
            }
        }
        return null;
    }

    public void clear() {
        if (null != timer) {
            timer.cancel();
        }
        if (null != activeTimer) {
            activeTimer.cancel();
        }
        Iterator<Buffer> iterator = map.iterator();
        while (iterator.hasNext()) {
            Buffer buffer = iterator.next();
            if (buffer != null) {
                buffer.close();
            }
        }
    }
}