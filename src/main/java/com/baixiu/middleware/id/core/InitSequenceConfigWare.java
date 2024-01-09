package com.baixiu.middleware.id.core;

import com.baixiu.middleware.id.config.SequenceConfig;
import com.baixiu.middleware.id.model.SequenceModel;
import com.baixiu.middleware.id.model.SequenceSimpleValue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.util.List;

/**
 * initializingBean 扩展点。实现
 * @author baixiu
 * @date 创建时间 2023/12/22 4:04 PM
 */
@Component
public class InitSequenceConfigWare implements InitializingBean {

    @Resource
    private SequenceConfig sequenceConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<SequenceModel> lists= sequenceConfig.allConfigs();
        if(!CollectionUtils.isEmpty(lists)){
            for (SequenceModel item : lists) {
                SequenceSimpleValue sequenceSimpleValue=new SequenceSimpleValue(item);
                SequenceStepContextHolder.ALL_SEQUENCE_CONTEXT.put(sequenceSimpleValue.getName(),sequenceSimpleValue);
            }
        }
    }

}