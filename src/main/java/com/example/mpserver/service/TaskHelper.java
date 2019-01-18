package com.example.mpserver.service;

import javafx.concurrent.Task;

/**
 * Created by zhanghd16 on 2018/6/8.
 */
public class TaskHelper {
    private TaskQueue taskQueue;
    private static volatile TaskHelper instance; //懒汉单例终极模式

    private TaskHelper() {
        taskQueue = new TaskQueue(1);
        taskQueue.start();
    }

    public static TaskHelper getInstance() {

        if (instance == null) {
            synchronized (TaskHelper.class) {
                if (instance == null) {
                    instance = new TaskHelper();
                }
            }
        }
        return instance;
    }

    //添加任务到队列中
    public <T extends ITask> int add(T task){
        return taskQueue.add(task);
    }

}
