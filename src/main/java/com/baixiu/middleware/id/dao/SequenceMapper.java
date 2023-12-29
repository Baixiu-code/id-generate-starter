package com.baixiu.middleware.id.dao;

import com.baixiu.middleware.id.model.SequenceModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
    List<SequenceModel> listSequenceModel();

    /**
     * get one sequence config by name
     * @param name name
     * @return sequence config
     */
    @Select ("select * from sequence where name=#{name}")
    SequenceModel getSequenceConfigByName(@Param("name") String name);

    /**
     * update sequence config by version 
     * @param data data 
     * @return
     */
    @Update ("update set `start`=#{data.start},`end`=#{data.end},create_time=#{data.createTime},upate_time=now()" +
            " where id=#{data.id} and `end`=#{data.end}")
    int save(@Param ("data") SequenceModel data);
    
}
