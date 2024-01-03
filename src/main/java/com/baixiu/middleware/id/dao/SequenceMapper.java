package com.baixiu.middleware.id.dao;

import com.baixiu.middleware.id.model.SequenceModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * sequence mapper
 * 用以查询sequence config 配置 by name
 * @author baixiu
 * @date 2023年12月22日
 */
@Mapper
public interface SequenceMapper {

    /**
     * list sequence model
     * @return list config
     */
    @Select ("select * from sequence")
    @Results(value = {
            @Result(column = "tenant_id", property = "tenantId"),
            @Result(column = "step_size", property = "stepSize"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    List<SequenceModel> listSequenceModel();

    /**
     * get one sequence config by name
     * @param name name
     * @return sequence config
     */
    @Select ("select * from sequence where name=#{name}")
    @Results(value = {
            @Result(column = "tenant_id", property = "tenantId"),
            @Result(column = "step_size", property = "stepSize"),
            @Result(column = "create_time", property = "createTime"),
            @Result(column = "update_time", property = "updateTime")
    })
    SequenceModel getSequenceConfigByName(@Param("name") String name);

    /**
     * update sequence config by version 
     * @param data data 
     * @return
     */
    @Update("update sequence set `start`=#{data.start},`end`=#{data.end},update_time=#{data.updateTime}" +
            " where tenant_id=#{data.tenantId} and `name`=#{data.name} and `end`=#{data.endVersion}")
    int save(@Param ("data") SequenceModel data);
    
}
