package com.ukg.datahub.perf.pipeline;

public class PipelinePerformanceSettings {

    private int personCount;
    private int dayCount;


    public PipelinePerformanceSettings(int personCount, int dayCount) {
        this.personCount = personCount;
        this.dayCount = dayCount;
    }

    public int getPersonCount() {
        return personCount;
    }

    public void setPersonCount(int personCount) {
        this.personCount = personCount;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    @Override
    public String toString() {
        return "PipelinePerformanceSettings{" +
                "personCount=" + personCount +
                ", dayCount=" + dayCount +
                '}';
    }
}
