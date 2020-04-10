package cn.lastwhisper.trace.event;

import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 事件消息体
 * @author lastwhisper
 * @date 2020/4/8
 */
public class CascadeEvent<E> extends ApplicationEvent {

    private List<E> idList;

    /**
     * Create a new {@code ApplicationEvent}.
     * @param source the object on which the event initially occurred or with
     * which the event is associated (never {@code null})
     */
    public CascadeEvent(Object source, List<E> idList) {
        super(source);
        this.idList = idList;
    }

    public List<E> getIdList() {
        return idList;
    }

    public void setIdList(List<E> idList) {
        this.idList = idList;
    }

}
