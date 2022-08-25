package com.vicious.viciouscore.common.events;

import com.vicious.viciouscore.common.util.server.ServerHelper;

import java.util.Objects;

public class VCTask {
    private Runnable future;
    private long executionMoment;
    private long period = -1;
    private VCTask(Runnable future, long delayticks){
        this.future = future;
        this.executionMoment = ServerHelper.server.getNextTickTime()-1+delayticks*50;
    }
    private VCTask(Runnable future, long delayticks, long period){
        this.period=period;
        this.future = future;
        this.executionMoment = ServerHelper.server.getNextTickTime()-1+delayticks*50;
    }
    public static VCTask delay(long delayTicks, Runnable future){
        VCTask task = new VCTask(future,delayTicks);
        Ticker.tasks.add(task);
        return task;
    }
    public static VCTask repeat(long repeatTicks, Runnable future){
        VCTask task = new VCTask(future,0,repeatTicks);
        Ticker.tasks.add(task);
        return task;
    }
    public static VCTask repeat(long delayticks,long repeatTicks, Runnable future){
        VCTask task = new VCTask(future,delayticks,repeatTicks);
        Ticker.tasks.add(task);
        return task;
    }

    public long getNextExecution(){
        return executionMoment;
    }
    public boolean isRepeatable(){
        return period != -1;
    }
    public void cancel(){
        future=null;
    }
    public void run(){
        if(period != -1){
            executionMoment+=period*50;
        }
        future.run();
    }
    public boolean isCancelled(){
        return future == null;
    }

    public boolean shouldRun(long l) {
        return executionMoment <= l && !isCancelled();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VCTask vcTask = (VCTask) o;
        return executionMoment == vcTask.executionMoment && period == vcTask.period && Objects.equals(future, vcTask.future);
    }

    @Override
    public int hashCode() {
        return Objects.hash(future, executionMoment, period);
    }
}
