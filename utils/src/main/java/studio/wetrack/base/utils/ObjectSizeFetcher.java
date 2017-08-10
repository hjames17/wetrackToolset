package studio.wetrack.base.utils;

import java.lang.instrument.Instrumentation;

/**
 * Created by zhanghong on 2017/8/10.
 */
public class ObjectSizeFetcher {
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object o) {
        return instrumentation.getObjectSize(o);
    }
}
