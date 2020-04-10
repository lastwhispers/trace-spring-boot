package cn.lastwhisper.trace.controller;

import cn.lastwhisper.trace.model.Method;
import cn.lastwhisper.trace.model.StaticVo;
import cn.lastwhisper.trace.service.StaticTraceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 静态调用链
 * @author lastwhisper
 * @date 2020/4/3
 */
@Controller
@RequestMapping("/static")
public class StaticTraceController {

    @Autowired
    private StaticTraceService staticTraceService;

    @GetMapping("/total")
    @ResponseBody
    public int total() {
        return staticTraceService.methodTotal();
    }

    @GetMapping("/page/{curr}/{size}")
    @ResponseBody
    public List<StaticVo> listByPage(@PathVariable Integer curr, @PathVariable Integer size) {
        return staticTraceService.listByPage(curr, size);
    }

    // 根据signatureId获取静态调用链
    @GetMapping("/chain/{chainId}")
    @ResponseBody
    public Method chain(@PathVariable Long chainId) {
        return staticTraceService.getChainByChainId(chainId);
    }

    // 根据signatureId获取静态方法信息
    @GetMapping("/method/{signatureId}")
    @ResponseBody
    public Method method(@PathVariable Long signatureId) {
        return staticTraceService.getMethodBySignatureId(signatureId);
    }

}
