package com.baixiu.middleware.id.model;

/**
 * @author baixiu
 * @date 创建时间 2023/12/22 4:09 PM
 */
public class SequenceStepModel {

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

    public SequenceStepModel(SequenceModel sequenceModel) {
        this.name=sequenceModel.getName();
        this.start=sequenceModel.getStart();
        this.end=sequenceModel.getEnd();
        this.stepSize=sequenceModel.getStepSize();
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
}
