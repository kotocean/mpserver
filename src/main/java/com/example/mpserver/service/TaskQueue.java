package com.example.mpserver.service;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by zhanghd16 on 2018/6/8.
 */
public class TaskQueue {
    private BlockingDeque<ITask> mTaskQueue; //等待办事的人
    private TaskExecutor[] mTaskExecutors; //办事窗口

    public TaskQueue(int size){
        mTaskQueue = new LinkedBlockingDeque<>(); //初识排队
        mTaskExecutors = new TaskExecutor[size]; //实例化多个办事窗口
    }

    public void start(){
        stop();
        for (int i = 0; i < mTaskExecutors.length; i++) {
            mTaskExecutors[i] = new TaskExecutor(mTaskQueue);
            mTaskExecutors[i].start();
        }
    }

    public void stop(){
        if(null != mTaskExecutors){
            for(TaskExecutor taskExecutor : mTaskExecutors){
                if(null != taskExecutor){
                    taskExecutor.quit();
                }
            }
        }
    }
    //开门，迎接办事人
    public <T extends ITask> int add(T task){
        if(!mTaskQueue.contains(task)){
            mTaskQueue.add(task);
        }
        return mTaskQueue.size();
    }
}
