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
public class IdGeneratorImpl extends ReadWriteLockUtil implements IdGenerator {
    
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
        //这里在原有基础上(单个 ReadWriteLock 去完成不同 SequenceName 的读写逻辑)生产了不同的 rwLock 用以保证在不同
        //SequenceName 的竞争过程中去分别完成不同锁持有,隔离并发处理.
        ReadWriteLock rwLock = ReadWriteLockUtil.RW_LOCKS[ReadWriteLockUtil.selectLock(sequenceName
                , ReadWriteLockUtil.SEQ_ID_READ_WRITE_LOCK_NUM)];
        
        //step1:find sequence Name local cache
        SequenceSimpleValue sequenceSimpleValue = null;
        try {
            rwLock.readLock().lock();
            sequenceSimpleValue=SequenceStepContextHolder.ALL_SEQUENCE_CONTEXT.get(sequenceName);
            //step2:if exist .then get and increment return +1 
            if(Objects.nonNull(sequenceSimpleValue)){
                //存在且起始值满足条件时返回当前start value +1
                if(sequenceSimpleValue.getPreciseStart().longValue()<sequenceSimpleValue.getEnd()){
                    return  sequenceSimpleValue.getPreciseStart().incrementAndGet();                    
                }
            }
        } catch (Exception e) {
            log.error("generatorGlobalId.error.{}.{}",tenantId,name,e);
        } finally {
            rwLock.readLock().unlock ();
        }
        
        
        try {
            //写入场景
            rwLock.writeLock().lock();
            //这里要重新读取一次，防止第一个线程已经完成了stepSize的扩容操作，直接使用原来的读取将出现数据异常。我草
            //具体是:第一个线程已经做完成重置步长并重新set 到 localCache 且完成了 unlock(line 90).这时候第二个线程进入 lock(第 66 line.)
            //这时候不可避免的需要进行二次获取(line 70),不然就可能导致二次覆盖
            sequenceSimpleValue=SequenceStepContextHolder.ALL_SEQUENCE_CONTEXT.get(sequenceName);
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
        //加1返回
        SequenceStepContextHolder.ALL_SEQUENCE_CONTEXT.put(sequenceSimpleValue.getName(),sequenceSimpleValue);
        return sequenceSimpleValue.getPreciseStart().addAndGet(1);
    }

    private boolean updateNextStepLengthToDB(SequenceModel sequenceModel) {
        String[]  sequencesNameAndTenantIds=sequenceModel.getName().split ("_");
        sequenceModel.setTenantId(Long.parseLong (sequencesNameAndTenantIds[0]));
        sequenceModel.setName(sequencesNameAndTenantIds[1]);
        int rs=sequenceConfig.saveConfigByLastEndValue(sequenceModel);
        if(rs>0){
            return true;
        }
        for(int i=0;i<10;i++){
            rs = this.sequenceConfig.saveConfigByLastEndValue(sequenceModel);
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
        sequenceModel.setStart(sequenceSimpleValue.getPreciseStart ().longValue ());
        sequenceModel.setEndVersion(sequenceSimpleValue.getEnd());
        //最大值往后放stepSize 大小
        sequenceModel.setEnd(sequenceSimpleValue.getEnd()+sequenceSimpleValue.getStepSize());
        sequenceSimpleValue.setEnd(sequenceSimpleValue.getEnd()+sequenceSimpleValue.getStepSize());        
        sequenceModel.setName(sequenceSimpleValue.getName());
        sequenceModel.setUpdateTime(new Date());
        
        return sequenceModel;
    }

    private String getSequenceName(long tenantId, String name) {
        return tenantId+"_"+name;
    }
}
