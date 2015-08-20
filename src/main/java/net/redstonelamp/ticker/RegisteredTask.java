package net.redstonelamp.ticker;

/**
 * Created by jython234 on 8/20/2015.
 *
 * @author RedstoneLamp Team
 */
public class RegisteredTask{
    private Task task;
    private long leftTicks;
    private int repeatInterval;

    public static RegisteredTask delay(Task task, int delay){
        return new RegisteredTask(task, delay, 0);
    }
    public static RegisteredTask repeat(Task task, int repeat){
        return new RegisteredTask(task, 1, repeat);
    }
    public static RegisteredTask delayAndRepeat(Task task, int delay, int repeat){
        return new RegisteredTask(task, delay, repeat);
    }
    public RegisteredTask(Task task, int delay, int repeat){
        this.task = task;
        leftTicks = delay;
        repeatInterval = repeat;
    }

    /**
     * <font color="FF0000"><b>Warning: this method is frequently called.
     * Minimize the time to run this method.</b></font>
     * <br>Number of times run per second: number of tasks * TPS
     * @param tick the current ticker tick
     */
    public void check(long tick){
        leftTicks--;
        if(leftTicks == 0){
            task.onRun(tick);
            leftTicks += repeatInterval;
            // if repeat interval is 0, it will become -1 next time and won't get called again
        }
    }

    /**
     * <b>Warning: this method does not unregister the task.</b> It is only for
     * temporary usage.<br>
     * To unregister totally, use {@link RedstoneTicker#cancelTask(Task)}.
     */
    public void stopRepeating(){
        leftTicks = -1;
    }

    public Task getTask(){
        return task;
    }
}
