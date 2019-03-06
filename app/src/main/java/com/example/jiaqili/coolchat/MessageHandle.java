package com.example.jiaqili.coolchat;

/**
 * Created by Jiaqi Li on 2018/4/1.
 */

public class MessageHandle {
    public static final int TYPE_RECEIVE=0;
    public static final int TYPE_SEND=1;
    private String content;
    private  int type;
    public MessageHandle(String ct, int tp){
        this.content=ct;
        this.type=tp;
    }
    public String getContent(){
        return this.content;
    }
    public int getType(){
        return this.type;
    }
}
