package cn.lastwhisper.trace.controller;

import cn.lastwhisper.trace.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Controller
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    /**
     * 解决静态资源不加载
     */
    @RequestMapping(value = "/monitor/**")
    public void dispatcher(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String uri = request.getRequestURI().trim();
        if (uri.endsWith(".js")) {
            response.setContentType("application/javascript");
        } else if (uri.endsWith(".css")) {
            response.setContentType("text/css");
        } else if (uri.endsWith(".woff")) {
            response.setContentType("application/x-font-woff");
        } else if (uri.endsWith(".ttf")) {
            response.setContentType("application/x-font-truetype");
        } else if (uri.endsWith(".html")) {
            response.setContentType("text/html");
        }
        byte[] s = resourceService.read(uri);
        response.getOutputStream().write(Optional.ofNullable(s).orElse("404".getBytes()));
    }
}
