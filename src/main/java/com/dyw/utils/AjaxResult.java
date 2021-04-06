package com.dyw.utils;

import java.io.Serializable;

public class AjaxResult implements Serializable {

    //状态
    private boolean state;

    //响应的数据
    private Object data;

    //提示信息
    private String msg;


    public static AjaxResult build(boolean state, String msg, Object data) {
        return new AjaxResult(state, msg, data);
    }

    public static AjaxResult ok(Object data) {
        return new AjaxResult(data);
    }

    public static AjaxResult ok() {
        return new AjaxResult(null);
    }

    public AjaxResult() {

    }

    public static AjaxResult build(boolean state, String msg) {
        return new AjaxResult(state, msg, null);
    }

    public AjaxResult(boolean state, String msg, Object data) {
        this.state = state;
        this.msg = msg;
        this.data = data;
    }

    public AjaxResult(boolean state, String msg) {
        this.state = state;
        this.msg = msg;
        this.data = null;
    }

    public AjaxResult(Object data) {
        this.state = true;
        this.msg = "OK";
        this.data = data;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
