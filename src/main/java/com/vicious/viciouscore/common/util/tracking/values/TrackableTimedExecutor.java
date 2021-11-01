package com.vicious.viciouscore.common.util.tracking.values;


import com.vicious.viciouscore.common.util.tracking.Trackable;
import com.vicious.viciouscore.common.util.tracking.interfaces.TickableTrackableValue;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.BiPredicate;

//Allows for executing code only when the predicated date is reached.
public class TrackableTimedExecutor extends TrackableObject<Date> implements TickableTrackableValue {
    private Runnable executor;
    private BiPredicate<TrackableTimedExecutor,Date> predicator;
    public TrackableTimedExecutor(String name, Trackable<?> tracker, BiPredicate<TrackableTimedExecutor,Date> predicator, Runnable executor) {
        super(name, Date::new, tracker);
        this.predicator=predicator;
        this.executor=executor;
        globalHandler.addTickable(this);
    }
    public void tick(){
        Date now = new Date();
        if(predicator.test(this,now)){
            executor.run();
            set(now);
        }
    }
    //Predicate if it is the next week
    public static final BiPredicate<TrackableTimedExecutor,Date> WEEKLY = (executor,now)->now.getTime() > executor.setting.getTime() + TimeUnit.DAYS.toMillis(7);
    //Predicate if it is the next month
    public static final BiPredicate<TrackableTimedExecutor,Date> MONTHLY = (executor, now)-> now.getMonth() > executor.setting.getMonth() || (now.getMonth() == 0 && executor.setting.getMonth() != 0);
    //Predicate if it is the next year
    public static final BiPredicate<TrackableTimedExecutor,Date> YEARLY = (executor,now)->now.getYear() > executor.setting.getYear();
}

