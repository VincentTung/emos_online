package com.vincent.emos.wx.exception;

import lombok.Data;

@Data
public class EmosException  extends  RuntimeException{
    private String msg;
    private int code = 500;

    public EmosException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public EmosException(String msg,Throwable e){
        super(msg,e);
        this.msg = msg;
    }

    public EmosException(String msg,int code){
        super(msg);
        this.msg = msg;
        this.code = code;
    }
    public EmosException(String msg,int code,Throwable e){
        super(msg,e);
        this.msg = msg;
        this.code = code;
    }
}
