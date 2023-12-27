package com.baixiu.middleware.id.model;

import java.util.Date;

/**
 * @author baixiu
 * @date 创建时间 2023/12/22 3:14 PM
 */
public class SequenceModel {

    private long id;

    /**
     * 租户id 。不为空是支持 SaaS 化产品
     */
    private Long tenantId;

    /**
     * name sequence
     */
    private String name;

    /**
     * 起始值
     */
    private long start;

    /**
     * end 上限值
     */
    private long end;

    /**
     * 步长 size
     */
    private long stepSize;


    /**
     * createTime 创建时间
     */
    private Date createTime;

    /**
     * update  跟随 sql server 进行更新
     */
    private Date updateTime;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getStepSize() {
        return stepSize;
    }

    public void setStepSize(long stepSize) {
        this.stepSize = stepSize;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
