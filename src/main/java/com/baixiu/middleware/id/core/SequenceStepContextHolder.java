package com.baixiu.middleware.id.core;


import com.baixiu.middleware.id.model.SequenceSimpleValue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * sequence map holder 
 * @author baixiu
 * @date 创建时间 2023/12/28 2:59 PM
 */
public class SequenceStepContextHolder {
    
    public static ConcurrentHashMap<String, SequenceSimpleValue> ALL_SEQUENCE_CONTEXT=new ConcurrentHashMap<String, SequenceSimpleValue> ();
    
}
