package cn.lastwhisper.trace.controller;

import cn.lastwhisper.trace.model.Node;
import cn.lastwhisper.trace.model.RealVo;
import cn.lastwhisper.trace.service.RealTraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 实时调用链
 * @author lastwhisper
 * @date 2020/2/17
 */
@Controller
@RequestMapping("/real")
public class RealTraceController {

    @Autowired
    private RealTraceService realTraceService;

    @GetMapping("/total")
    @ResponseBody
    public int total() {
        return realTraceService.realTotal();
    }

    @GetMapping("/page/{curr}/{size}")
    @ResponseBody
    public List<RealVo> listByPage(@PathVariable Integer curr, @PathVariable Integer size) {
        return realTraceService.listByPage(curr, size);
    }

    // 根据traceId获取实时方法调用链
    @GetMapping("/trace/{traceId}")
    @ResponseBody
    public Node trace(@PathVariable Long traceId) {
        return realTraceService.getTraceByTraceId(traceId);
    }

    // 根据spanId获取实时方法信息
    @GetMapping("/node/{nodeId}")
    @ResponseBody
    public Node node(@PathVariable Long nodeId) {
        return realTraceService.getNodeBySpanId(nodeId);
    }

    @GetMapping("/clear")
    public void clear() {
         realTraceService.clear();
    }


}
