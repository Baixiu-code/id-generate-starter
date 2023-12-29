package com.baixiu.middleware.id.api.impl;

import com.baixiu.middleware.id.api.IdGenerator;
import com.baixiu.middleware.id.config.SequenceConfig;
import com.baixiu.middleware.id.consts.Consts;
import com.baixiu.middleware.id.core.ReadWriteLockUtil;
import com.baixiu.middleware.id.core.SequenceStepContextHolder;
import com.baixiu.middleware.id.model.SequenceModel;
import com.baixiu.middleware.id.model.SequenceSimpleValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * id 生产implement class 
 * @author baixiu
 * @date 创建时间 2023/12/22 4:01 PM
 */
@Slf4j
@Component
public class IdGeneratorImpl implements IdGenerator {
    
    @Autowired
    private SequenceConfig sequenceConfig;
    
    @Override
    public long generatorGlobalId(String name) {
        return generatorGlobalId(Consts.DEFAULT_TENANT_ID,name);
    }

    @Override
    public long generatorGlobalId(long tenantId, String name) {
        
        String sequenceName=getSequenceName(tenantId,name);
        
        //获取seqName对应的读写锁
        ReadWriteLock rwLock = ReadWriteLockUtil.RW_LOCKS[ReadWriteLockUtil.selectLock(sequenceName
                , ReadWriteLockUtil.SEQ_ID_READ_WRITE_LOCK_NUM)];
        rwLock.readLock();
        //step1:find sequence Name local cache
        SequenceSimpleValue sequenceSimpleValue = null;
        try {
            sequenceSimpleValue=SequenceStepContextHolder.ALL_SEQUENCE_CONTEXT.get(getSequenceName(tenantId,name));
            //step2:if exist .then get and increment return +1 
            if(Objects.nonNull(sequenceSimpleValue)){
                //存在且起始值满足条件时返回当前start value +1
                if(sequenceSimpleValue.getPreciseStart().longValue()<sequenceSimpleValue.getEnd()){
                    return sequenceSimpleValue.getPreciseStart().incrementAndGet();
                }
                //模拟看看 longAdder 效率
                sequenceSimpleValue.getStart().add(1);
                log.info("sequenceSimpleValue.longAdder.value.{}",sequenceSimpleValue.getStart().sum());                  
            }
        } catch (Exception e) {
            log.error("generatorGlobalId.error.{}.{}",tenantId,name,e);
        } finally {
            rwLock.readLock().unlock ();
        }
        
        //写入场景
        rwLock.writeLock();
        try {
            if(Objects.nonNull(sequenceSimpleValue)){
                //存在且起始值满足条件时返回当前start value +1
                if(sequenceSimpleValue.getPreciseStart().longValue()<sequenceSimpleValue.getEnd()){
                    return sequenceSimpleValue.getPreciseStart().incrementAndGet();
                }
                //存在但起始值不满足条件，则需要进化一个stepSize并更新数据库后再次put进入map完成map localCache 更新
                SequenceModel sequenceModel=getNextStepLength(sequenceSimpleValue);
                //更新 数据成功
                if(updateNextStepLengthToDB(sequenceModel)){
                    return updateNextStepLengthToMapAndGet(sequenceModel,sequenceSimpleValue);       
                }else{
                    throw new RuntimeException ("update sequence config error.please check u app.");
                }            
            }else{
                //todo 当map里无Sequence Config 配置时可以选择抛出异常，也可以选择插入并返回。先实现保证通过容器启动获取到的来进行返回
                throw new RuntimeException ("not found sequence config in init map");
            }
        } catch (RuntimeException e) {
            throw  new RuntimeException (e);
        } finally {
            rwLock.writeLock ().unlock ();
        }


    }

    private long updateNextStepLengthToMapAndGet(SequenceModel sequenceModel,SequenceSimpleValue sequenceSimpleValue) {
        String sequenceName=getSequenceName(sequenceModel.getTenantId(),sequenceSimpleValue.getName());
        sequenceSimpleValue.getPreciseStart().addAndGet(sequenceSimpleValue.getStepSize ());
        sequenceSimpleValue.setEnd (sequenceSimpleValue.getEnd ()+sequenceSimpleValue.getStepSize ());
        //加1返回
        SequenceStepContextHolder.ALL_SEQUENCE_CONTEXT.put(sequenceName,sequenceSimpleValue);
        return sequenceSimpleValue.getPreciseStart ().incrementAndGet ();
    }

    private boolean updateNextStepLengthToDB(SequenceModel sequenceModel) {
        int rs=sequenceConfig.saveConfigByLastEndValue(sequenceModel);
        if(rs>0){
            return true;
        }
        for(int i=0;i<10;i++){
            if(rs==1){
                return true;
            }
        }
        return false;
    }

    /**
     * get next step length 
     * @param sequenceSimpleValue
     * @return
     */
    private SequenceModel getNextStepLength(SequenceSimpleValue sequenceSimpleValue) {
        SequenceModel sequenceModel=new SequenceModel ();
        sequenceModel.setEnd (sequenceSimpleValue.getEnd()+sequenceSimpleValue.getStepSize());
        sequenceModel.setStart (sequenceSimpleValue.getPreciseStart ().addAndGet (sequenceSimpleValue.getStepSize()));
        sequenceModel.setName (sequenceSimpleValue.getName ());
        sequenceModel.setCreateTime(new Date());
        sequenceModel.setUpdateTime(new Date());
        return sequenceModel;
    }

    private String getSequenceName(long tenantId, String name) {
        return tenantId+"_"+name;
    }
}
