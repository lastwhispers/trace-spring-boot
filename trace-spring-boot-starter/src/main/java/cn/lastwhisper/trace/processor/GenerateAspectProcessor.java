package cn.lastwhisper.trace.processor;

import cn.lastwhisper.trace.processor.annotation.EnableTrace;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.*;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * 生产动态代理类注解处理器
 * @author lastwhisper
 * @date 12/22/2019
 */
public class GenerateAspectProcessor extends AbstractProcessor {
    private Messager messager; //用于打印日志
    private Elements elementUtils; //用于处理元素
    private Filer filer;  //用来创建java文件或者class文件

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(EnableTrace.class.getCanonicalName());
        return Collections.unmodifiableSet(set);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {
            // 返回被注释的节点
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(EnableTrace.class);
            //存放注释value
            List<String> info = new ArrayList<>();
            for (Element element : elements) {
                // 如果注释在类上，获取注释信息
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
            JavaFileObject sourceFile = filer.createSourceFile("CollectorAspect");
            // 写入代码
            createSourceFile(info, sourceFile.openWriter());
            // 手动编译
            compile(sourceFile.toUri().getPath());
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, "apt error");
        }
        return true;
    }

    private void createSourceFile(List<String> set, Writer writer) throws IOException {
        writer.write("package " + set.get(0) + ";\n" +
                "\n" +
                "import cn.lastwhisper.trace.collector.Collector;\n" +
                "import org.aspectj.lang.ProceedingJoinPoint;\n" +
                "import org.aspectj.lang.annotation.Around;\n" +
                "import org.aspectj.lang.annotation.Aspect;\n" +
                "import org.aspectj.lang.annotation.Pointcut;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.stereotype.Component;\n" +
                "\n" +
                "@Aspect\n" +
                "@Component\n" +
                "public class CollectorAspect {\n" +
                "\n" +
                "    @Autowired\n" +
                "    private Collector collector;\n" +
                "\n" +
                "    @Pointcut(\"(" + set.get(1) + " || @annotation(cn.lastwhisper.trace.aspect.annotation.Include)) && !@annotation(cn.lastwhisper.trace.aspect.annotation.Exclude)\")\n" +
                "    public void matchCondition() {}\n" +
                "\t\n" +
                "    @Around(\"matchCondition()\")\n" +
                "    public Object methodProxy(ProceedingJoinPoint pjp) throws Throwable {\n" +
                "        collector.before(pjp);\n" +
                "        Object result;\n" +
                "        try {\n" +
                "            result = pjp.proceed();\n" +
                "        } catch (Throwable throwable) {\n" +
                "            collector.exceptionAfter(pjp, throwable);\n" +
                "            throw throwable;\n" +
                "        }\n" +
                "        collector.after(pjp);\n" +
                "        return result;\n" +
                "    }\n" +
                "}");
        writer.flush();
        writer.close();
    }

    /**
     * 编译文件
     * @param path
     * @throws IOException
     */
    private void compile(String path) throws IOException {
        //拿到编译器
        JavaCompiler complier = ToolProvider.getSystemJavaCompiler();
        //文件管理者
        StandardJavaFileManager fileMgr =
                complier.getStandardFileManager(null, null, null);
        //获取文件
        Iterable units = fileMgr.getJavaFileObjects(path);
        //编译任务
        JavaCompiler.CompilationTask t = complier.getTask(null, fileMgr, null, null, null, units);
        //进行编译
        t.call();
        fileMgr.close();
    }

    /**
     * 读取类名
     * @param name
     * @return
     */
    private String getClassName(String name) {
        String result = name;
        if (name.contains(".")) {
            result = name.substring(name.lastIndexOf(".") + 1);
        }
        return result;
    }

    /**
     * 读取包名
     * @param name
     * @return
     */
    private String getPackage(String name) {
        String result = name;
        if (name.contains(".")) {
            result = name.substring(0, name.lastIndexOf("."));
        } else {
            result = "";
        }
        return result;
    }

}


