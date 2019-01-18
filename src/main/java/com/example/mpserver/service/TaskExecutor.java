package com.example.mpserver.service;

import java.util.concurrent.BlockingDeque;

/**
 * Created by zhanghd16 on 2018/6/8.
 */
public class TaskExecutor extends Thread {
    private BlockingDeque<ITask> taskQueue;
    private boolean isRunning = true;

    public TaskExecutor(BlockingDeque<ITask> taskQueue){
        this.taskQueue = taskQueue;
    }

    public void quit(){
        isRunning = false;
        interrupt(); //中断当前进程
    }

    @Override
    public void run(){
        while(isRunning){
            ITask iTask;
            try{
                iTask = taskQueue.take();//若为空，阻塞等待去除下一个task;不为空立即返回
            }catch (InterruptedException e){
                if(!isRunning){
                    interrupt();
                    break;
                }
                continue;
            }
            iTask.run();//执行任务
        }
    }
}
