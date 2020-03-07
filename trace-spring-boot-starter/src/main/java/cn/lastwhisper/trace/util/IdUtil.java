package cn.lastwhisper.trace.util;

import cn.hutool.core.lang.Snowflake;
import cn.lastwhisper.trace.common.NamedThreadLocal;

/**
 *
 * @author lastwhisper
 * @date 2020/3/2
 */
public class IdUtil {
    private static final ThreadLocal<Long> ID = new NamedThreadLocal<>("trace id");

    private static Snowflake snowflake = cn.hutool.core.util.IdUtil.createSnowflake(1, 1);

    /**
     * 自增的唯一id
     */
    public static long getSnowflakeId() {
        return snowflake.nextId();
    }

    /**
     * 线程内id相同
     */
    public static Long getTraceId() {
        if (ID.get() == null) {
            ID.set(getSnowflakeId());
        }
        return ID.get();
    }

    public static void clearTraceId() {
        ID.remove();
    }
}
