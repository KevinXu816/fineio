package com.fineio.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author yee
 * @date 2018/9/12
 */
public class FineIOThreadFactory implements ThreadFactory {
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    public FineIOThreadFactory(Class<?> c) {
        this(getSimpleName(c));
    }

    public FineIOThreadFactory(String namePrefix) {

        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        this.namePrefix = namePrefix;
    }

    static Thread newThread(ThreadGroup group, Runnable target, String name, long stackSize) {
        Thread t = new Thread(group, target, name, stackSize);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }

    static String getSimpleName(Class<?> c) {
        String name = c.getName();
        return name.substring(name.lastIndexOf('.') + 1);
    }

    @Override
    public Thread newThread(Runnable r) {
        return newThread(group, r, String.format("%s-%d", namePrefix, threadNumber.getAndIncrement()), 0);
    }
}