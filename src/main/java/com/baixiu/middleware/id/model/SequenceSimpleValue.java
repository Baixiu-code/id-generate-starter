package com.baixiu.middleware.id.model;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author baixiu
 * @date 创建时间 2023/12/28 3:01 PM
 */
public class SequenceSimpleValue {

    /**
     * name sequence
     */
    private String name;

    /**
     * 模糊的 起始值
     */
    private LongAdder start;

    /**
     * 精准的 起始值
     */
    private AtomicLong preciseStart;

    /**
     * end 上限值
     */
    private long end;

    /**
     * 步长 size
     */
    private long stepSize;

    public SequenceSimpleValue(SequenceModel sequenceModel) {
        this.name=sequenceModel.getName();
        this.start.add(sequenceModel.getStart());
        this.end=sequenceModel.getEnd();
        this.stepSize=sequenceModel.getStepSize();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LongAdder getStart() {
        return start;
    }

    public void setStart(LongAdder start) {
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

    public AtomicLong getPreciseStart() {
        return preciseStart;
    }

    public void setPreciseStart(AtomicLong preciseStart) {
        this.preciseStart = preciseStart;
    }
}
