package com.fineio.java;

import java.util.Collection;
import java.util.List;

/**
 * @author yee
 * @date 2019-05-22
 */
public class JavaVersion {

    /**
     * The major version number of class files
     * for JDK 1.3.
     */
    public static final int JAVA_3 = 47;

    /**
     * The major version number of class files
     * for JDK 1.5.
     */
    public static final int JAVA_5 = 49;

    /**
     * The major version number of class files
     * for JDK 1.6.
     */
    public static final int JAVA_6 = 50;

    /**
     * The major version number of class files
     * for JDK 1.7.
     */
    public static final int JAVA_7 = 51;

    /**
     * The major version number of class files
     * for JDK 1.8.
     */
    public static final int JAVA_8 = 52;

    /**
     * The major version number of class files
     * for JDK 1.9.
     */
    public static final int JAVA_9 = 53;

    /**
     * The major version number of class files
     * for JDK 10.
     */
    public static final int JAVA_10 = 54;

    /**
     * The major version number of class files
     * for JDK 11.
     */
    public static final int JAVA_11 = 55;

    /**
     * The major version number of class files created
     * from scratch.  The default value is 47 (JDK 1.3).
     * It is 49 (JDK 1.5)
     * if the JVM supports <code>java.lang.StringBuilder</code>.
     * It is 50 (JDK 1.6)
     * if the JVM supports <code>java.util.zip.DeflaterInputStream</code>.
     * It is 51 (JDK 1.7)
     * if the JVM supports <code>java.lang.invoke.CallSite</code>.
     * It is 52 (JDK 1.8)
     * if the JVM supports <code>java.util.function.Function</code>.
     * It is 53 (JDK 1.9)
     * if the JVM supports <code>java.lang.reflect.Module</code>.
     * It is 54 (JDK 10)
     * if the JVM supports <code>java.util.List.copyOf(Collection)</code>.
     * It is 55 (JDK 11)
     * if the JVM supports <code>java.util.Optional.isEmpty()</code>.
     */
    private static final int MAJOR_VERSION;

    static {
        int ver = JAVA_3;
        try {
            Class.forName("java.lang.StringBuilder");
            ver = JAVA_5;
            Class.forName("java.util.zip.DeflaterInputStream");
            ver = JAVA_6;
            Class.forName("java.lang.invoke.CallSite", false, ClassLoader.getSystemClassLoader());
            ver = JAVA_7;
            Class.forName("java.util.function.Function");
            ver = JAVA_8;
            Class.forName("java.lang.Module");
            ver = JAVA_9;
            List.class.getMethod("copyOf", Collection.class);
            ver = JAVA_10;
            Class.forName("java.util.Optional").getMethod("isEmpty");
            ver = JAVA_11;
        } catch (Throwable t) {
        }
        MAJOR_VERSION = ver;
    }


    public static boolean isOverJava8() {
        return MAJOR_VERSION >= JAVA_8;
    }
}