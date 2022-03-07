package com.bitgame.game.framework.util;

import java.util.Timer;
import java.util.TimerTask;

public class TimerUtil {

    /**
     * 定时执行一次性任务
     *
     * @param runnable 供给型函数式接口
     * @param delay    延时时间. 单位:秒
     */
    public static Timer setTimeOut(Runnable runnable, long delay) {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {
                    timer.cancel();
                }
            }
        }, delay);

        return timer;
    }

    /**
     * 重复定时执行任务<br>
     * 备注:
     * 1. 如果上一次定时任务没有执行完. 则后续任务会相应推迟. 非固定频率执行
     *
     * @param runnable 供给型函数式接口
     * @param period   间隔执行时间. 单位:秒
     */
    public static Timer setInterval(Runnable runnable, long period) {
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        }, 0, period);

        return timer;
    }
}
