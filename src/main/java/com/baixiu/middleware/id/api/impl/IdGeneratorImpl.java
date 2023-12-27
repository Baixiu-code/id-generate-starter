package com.baixiu.middleware.id.api.impl;

import com.baixiu.middleware.id.api.IdGenerator;
import com.baixiu.middleware.id.consts.Consts;
import org.springframework.stereotype.Component;

/**
 * id 生产
 * @author baixiu
 * @date 创建时间 2023/12/22 4:01 PM
 */
@Component
public class IdGeneratorImpl implements IdGenerator {
    @Override
    public long generatorGlobalId(String name) {
        return generatorGlobalId(Consts.DEFAULT_TENANT_ID,name);
    }

    @Override
    public long generatorGlobalId(long tenantId, String name) {
        return 0;
    }
}
