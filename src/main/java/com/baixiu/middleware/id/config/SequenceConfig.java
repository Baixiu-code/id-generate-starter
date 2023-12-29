package com.baixiu.middleware.id.config;

import com.baixiu.middleware.id.model.SequenceModel;

import java.util.List;

/**
 * sequence config 通过name 获取。也可以用户进行自行实现
 * @author baixiu
 * @date 2023年12月22日
 */
public interface SequenceConfig {

    /**
     * get config by name
     * @param name name
     * @return config
     */
    SequenceModel getConfigByName(String name);

    /**
     * all configs query
     * @return all configs
     */
    List<SequenceModel> allConfigs();

    /**
     * update sequence Model 
     * @param sequenceModel sequence model
     * @return config 
     */
    int saveConfigByLastEndValue(SequenceModel sequenceModel);
}
