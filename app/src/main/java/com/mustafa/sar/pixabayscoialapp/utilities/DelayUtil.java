package com.mustafa.sar.pixabayscoialapp.utilities;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DelayUtil {
    /**
     *  Delays the current thread execution.
     *  The thread loses ownership of any monitors.
     *  Quits immediately if the thread is interrupted
     *
     * @param durationInMillis the time duration in milliseconds
     */
    public void delay(final long durationInMillis) {
        delay(durationInMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * @param duration the time duration in the given {@code sourceUnit}
     * @param unit
     */
    public void delay(final long duration, final TimeUnit unit) {
        long currentTime = System.currentTimeMillis();
        long deadline = currentTime+unit.toMillis(duration);
        ReentrantLock lock = new ReentrantLock();
        Condition waitCondition = lock.newCondition();

        while ((deadline-currentTime)>0) {
            try {
                lock.lockInterruptibly();
                waitCondition.await(deadline-currentTime, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } finally {
                lock.unlock();
            }
            currentTime = System.currentTimeMillis();
        }
    }
}