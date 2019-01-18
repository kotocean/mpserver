package com.example.mpserver.service;

/**
 * Created by zhanghd16 on 2018/6/8.
 */
public class TestTask implements ITask {
    private String username;

    public TestTask(String username) {
        this.username = username;
    }

    @Override
    public void run(){
        try{
            Thread.sleep(30000);
        }catch (InterruptedException e){}
        System.out.println("===>>>>----- "+username+" :事情已办完。下一个！");
    }
}
