package cn.lastwhisper.trace.service.impl;

import cn.lastwhisper.trace.model.HttpNode;
import cn.lastwhisper.trace.model.Node;
import cn.lastwhisper.trace.model.RealVo;
import cn.lastwhisper.trace.repository.RealTraceRepository;
import cn.lastwhisper.trace.service.RealTraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author lastwhisper
 * @date 2020/4/2
 */
@Service
public class RealTraceServiceImpl implements RealTraceService {

    @Autowired
    private RealTraceRepository realTraceRepository;

    @Override
    public int realTotal() {
        return realTraceRepository.requestTotal();
    }

    /**
     * 获取controller的url、http method
     * curr=1,size=10 表示 1~10    traceInfoList.size=5
     * curr=2,size=10 表示 10~20
     * curr=3,size=10 表示 20~30
     * curr=4,size=10 表示 30~40
     *
     * start=(curr-1)*size
     * end=(curr)*size
     *
     */
    @Override
    public List<RealVo> listByPage(int curr, int size) {
        int count = realTraceRepository.requestTotal() - 1;

        int start, end;
        if (curr == 1) {
            start = 0;
            end = size;
        } else {
            start = (curr - 1) * size;
            end = (curr) * size;
        }

        if (start > count) {
            return null;
        }
        if (end > count) {
            end = count;
        } else {
            //size=8时:start=0,end=8，会查9条数据
            //size=8时:start=8,end=16，会查9条数据
            end--;
        }

        return realTraceRepository.listByLimit(start, end);
    }

    @Override
    public Node getTraceByTraceId(Long traceId) {
        return realTraceRepository.getTraceByTraceId(traceId);
    }

    @Override
    public Node getNodeBySpanId(Long nodeId) {
        return realTraceRepository.getNodeBySpanId(nodeId);
    }

    @Override
    public void saveNode(Node node) {
        realTraceRepository.saveNode(node);
    }

    @Override
    public void saveHttpNode(Node node, Boolean flag) {
        realTraceRepository.saveHttpNode(node);
        savePage(node, flag);
    }

    @Override
    public void savePage(Node node, Boolean flag) {
        HttpNode httpNode = (HttpNode) node;
        realTraceRepository.savePage(new RealVo(httpNode.getTraceId(), httpNode.getUri(),
                httpNode.getMethod().getClassName(), httpNode.getMethod().getName(), httpNode.getHttpMethod(),
                httpNode.getStart(), httpNode.getEnd(), httpNode.getRelative(), flag));
    }

    @Override
    public void clear() {
        realTraceRepository.clear();
    }

}
