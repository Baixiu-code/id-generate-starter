package com.baixiu.middleware.id.api;

/**
 * id generator interface
 * @author baixiu
 * @date 2023年12月22日
 */
public interface IdGenerator {

    /**
     * 生成全局id
     * @param name 业务类型 .默认使用 tenantId =9527
     * @return id
     */
    long generatorGlobalId(String name);

    /**
     * 生成全局id
     * @param tenantId tenantId
     * @param name 业务类型
     * @return id
     */
    long generatorGlobalId(long tenantId,String name);



}
