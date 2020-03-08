package cn.lastwhisper.trace.controller;

import cn.lastwhisper.trace.model.MethodNode;
import cn.lastwhisper.trace.service.TraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

/**
 *
 * @author lastwhisper
 * @date 2020/2/17
 */
@Controller
@RequestMapping("/trace")
public class TraceController {

    @Autowired
    private TraceService traceService;

    @GetMapping("/url")
    @ResponseBody
    public Collection<String> url() {
        return traceService.getTraceUrl();
    }

    // 根据traceId获取单个调用链
    @GetMapping("/singleTrace")
    @ResponseBody
    public MethodNode singleTrace(Long traceId) {
        return traceService.getCallChain(traceId);
    }

    // 根据randomId获取单个MethodNode
    @GetMapping("/singleMethodNode")
    @ResponseBody
    public MethodNode singleMethodNode(Long randomId) {
        return traceService.getMethodNode(randomId);
    }

    // 清空数据
    @GetMapping("/clear")
    @ResponseBody
    public void clear() {
        traceService.clear();
    }
}
