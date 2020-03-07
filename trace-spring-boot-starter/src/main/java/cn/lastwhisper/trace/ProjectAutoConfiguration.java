package cn.lastwhisper.trace;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnWebApplication
@ComponentScan
public class ProjectAutoConfiguration {
}
