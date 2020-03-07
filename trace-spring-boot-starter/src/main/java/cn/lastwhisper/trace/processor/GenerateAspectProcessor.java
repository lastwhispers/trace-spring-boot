package cn.lastwhisper.trace.processor;

import cn.lastwhisper.trace.processor.annotation.EnableTrace;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.*;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 生产动态代理类注解处理器
 * @author lastwhisper
 * @date 12/22/2019
 */
//@AutoService(Processor.class)
public class GenerateAspectProcessor extends AbstractProcessor {
    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<String>();
        annotations.add(EnableTrace.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            // 获取被@EnableTrace的类
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(EnableTrace.class);
            // 存放注解上的信息
            List<String> info = new ArrayList<>();
            for (Element element : elements) {
                if (element.getKind() == ElementKind.CLASS && element instanceof TypeElement) {
                    TypeElement typeElement = (TypeElement) element;
                    String qname = typeElement.getQualifiedName().toString();
                    EnableTrace annotation = typeElement.getAnnotation(EnableTrace.class);
                    info.add(getPackage(qname));
                    info.add(annotation.value());
                    messager.printMessage(Diagnostic.Kind.NOTE, getPackage(qname));
                    break;
                }
            }
            // 未在类上使用注释则直接返回，返回false停止编译
            if (info.isEmpty()) {
                return true;
            }
            // 生成一个Java源文件
            JavaFileObject sourceFile = filer.createSourceFile("TraceAspect");
            // 写入代码
            createSourceFile(info, sourceFile.openWriter());
            // 手动编译
            compile(sourceFile.toUri().getPath());
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Trace 处理失败");
        }
        return true;
    }

    private void createSourceFile(List<String> set, Writer writer) throws IOException {
        writer.write("package " + set.get(0) + ";\n" +
                "import cn.lastwhisper.trace.util.IdUtil;\n" +
                "import cn.lastwhisper.trace.service.TraceInstance;\n" +
                "import org.aspectj.lang.ProceedingJoinPoint;\n" +
                "import org.aspectj.lang.annotation.Around;\n" +
                "import org.aspectj.lang.annotation.Aspect;\n" +
                "import org.aspectj.lang.annotation.Pointcut;\n" +
                "import org.slf4j.Logger;\n" +
                "import org.slf4j.LoggerFactory;\n" +
                "import org.springframework.stereotype.Component;\n" +
                "@Aspect\n" +
                "@Component\n" +
                "public class TraceAspect {\n" +
                "    private static Logger logger = LoggerFactory.getLogger(TraceAspect.class);\n" +
                "    @Pointcut(\"" + set.get(1) + "&& !@annotation(cn.lastwhisper.trace.aspect.annotation.Mark)\")\n" +
                "    public void matchCondition() {}\n" +
                "    @Around(\"matchCondition()\")\n" +
                "    public Object methodProxy(ProceedingJoinPoint pjp) throws Throwable {\n" +
                "        logger.info(\"trace begin:{},traceId:{}\", pjp.getTarget().getClass().getName(), IdUtil.getTraceId());\n" +
                "        TraceInstance.before(pjp);\n" +
                "        Object result = null;\n" +
                "        try { result = pjp.proceed();\n" +
                "        } catch (Throwable throwable) {\n" +
                "            TraceInstance.exceptionAfter(throwable);\n" +
                "            throw throwable;}\n" +
                "        logger.info(\"trace end:{},traceId:{}\", pjp.getTarget().getClass().getName(), IdUtil.getTraceId());\n" +
                "        TraceInstance.after();\n" +
                "        return result;\n" +
                "    }\n" +
                "}\n");
        writer.flush();
        writer.close();
    }

    /**
     * 编译文件
     */
    private void compile(String path) throws IOException {
        //拿到编译器
        JavaCompiler complier = ToolProvider.getSystemJavaCompiler();
        //文件管理者
        StandardJavaFileManager fileMgr =
                complier.getStandardFileManager(null, null, null);
        //获取文件
        Iterable<? extends JavaFileObject> units = fileMgr.getJavaFileObjects(path);
        //编译任务
        JavaCompiler.CompilationTask t = complier.getTask(null, fileMgr, null, null, null, units);
        //进行编译
        t.call();
        fileMgr.close();
    }


    /**
     * 读取包名
     */
    private String getPackage(String name) {
        String result;
        if (name.contains(".")) {
            result = name.substring(0, name.lastIndexOf("."));
        } else {
            result = "";
        }
        return result;
    }
}


