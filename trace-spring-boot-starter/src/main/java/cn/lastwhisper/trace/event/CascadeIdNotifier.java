package cn.lastwhisper.trace.event;

import cn.lastwhisper.trace.model.RealVo;
import cn.lastwhisper.trace.repository.RealTraceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 事件消息接收者
 * @author lastwhisper
 * @date 2020/4/8
 */
@Component
public class CascadeIdNotifier {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RealTraceRepository realTraceRepository;

    public void setRealTraceRepository(RealTraceRepository realTraceRepository) {
        this.realTraceRepository = realTraceRepository;
    }

    @EventListener(classes = {CascadeEvent.class})
    public void processCascadeEvent(CascadeEvent<Long> event) {
        logger.debug("receive cascade event");
        List<Long> idList = event.getIdList();
        Set<RealVo> realVoList = idList.stream().map(RealVo::new).collect(Collectors.toSet());
        realTraceRepository.removePage(realVoList);
    }

}
