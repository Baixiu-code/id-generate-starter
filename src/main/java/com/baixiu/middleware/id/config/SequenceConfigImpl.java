package com.baixiu.middleware.id.config;

import com.baixiu.middleware.id.dao.SequenceMapper;
import com.baixiu.middleware.id.model.SequenceModel;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import java.util.List;

/**
 * sequence config impl
 * @author baixiu
 * @date 创建时间 2023/12/22 3:36 PM
 */
@Component
public class SequenceConfigImpl implements SequenceConfig{

    @Resource
    private SequenceMapper sequenceMapper;
    @Override
    public SequenceModel getConfigByName(String name) {
        return sequenceMapper.getSequenceConfigByName(name);
    }

    @Override
    public List<SequenceModel> allConfigs() {
        return sequenceMapper.listSequenceModel();
    }

    @Override
    public int saveConfigByLastEndValue(SequenceModel sequenceModel) {
        return sequenceMapper.save(sequenceModel);
    }
}
