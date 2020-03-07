package cn.lastwhisper.trace.util;

import org.springframework.lang.Nullable;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * 集合工具类
 * @author lastwhisper
 * @date 2020/2/28
 */
public class CollectionUtil {

    public static boolean isNull(@Nullable Collection<?> collection) {
        return collection == null;
    }

    public static boolean isEmpty(@Nullable Collection<?> collection) {
        return CollectionUtils.isEmpty(collection);
    }

}
