package cn.lastwhisper.trace.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import java.util.List;

/**
 *  事件消息推送者
 * @author lastwhisper
 * @date 2020/4/8
 */
public class CascadeEventPublisher<E> implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        publisher = applicationEventPublisher;
    }

    public void sendRemoveMessage(List<E> buffers) {
        publisher.publishEvent(new CascadeEvent<E>(this, buffers));
    }

}
