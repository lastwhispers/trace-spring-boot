package cn.lastwhisper.trace.common.collection;

import cn.lastwhisper.trace.model.RealVo;

import java.util.ArrayList;

/**
 * 存入的数据保证有序性
 * @author lastwhisper
 * @date 2020/3/7
 */
@Deprecated
public class SortArrayList<T> extends ArrayList<T> {

    /**
     * @param traceId 删除traceId结点
     */
    public void delete(long traceId) {
        int index = binarySearch(traceId);
        if (index != -1) {
            this.remove(index);
        }
    }

    /**
     * 二分查找某结点
     */
    public int binarySearch(long target) {
        int start = 0, end = size() - 1, middle;
        while (end >= start) {
            middle = (end - start) / 2 + start;
            T t = get(middle);
            if (t instanceof RealVo) {
                long traceId = ((RealVo) t).getTraceId();
                if (traceId == target) {
                    return middle;
                } else if (traceId > target) {
                    end = middle - 1;
                } else {
                    start = middle + 1;
                }
            } else {
                break;
            }
        }
        return -1;
    }

}
