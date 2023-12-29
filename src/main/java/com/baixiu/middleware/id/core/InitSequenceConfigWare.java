package com.baixiu.middleware.id.core;

import com.baixiu.middleware.id.config.SequenceConfig;
import com.baixiu.middleware.id.model.SequenceModel;
import com.baixiu.middleware.id.model.SequenceSimpleValue;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ConcurrentReferenceHashMap;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author baixiu
 * @date 创建时间 2023/12/22 4:04 PM
 */
@Component
public class InitSequenceConfigWare implements InitializingBean {

    @Resource
    private SequenceConfig sequenceConfig;

    private Map<String, SequenceSimpleValue> ALL_STEPS=null;

    @Override
    public void afterPropertiesSet() throws Exception {
        List<SequenceModel> lists= sequenceConfig.allConfigs();
        if(!CollectionUtils.isEmpty(lists)){
            ALL_STEPS=new ConcurrentReferenceHashMap<>(lists.size(),0.75f);
            for (SequenceModel item : lists) {
                ALL_STEPS.put(item.getName(),new SequenceSimpleValue(item));
            }
        }
    }

}