package com.erbao.joystar.okhttp;

/**
 * Created by trista on 2017/3/24.
 */

public abstract class HttpCallBack<T> {

    public abstract void onSuccess(Object tag , String response) ;

    public abstract void onFail(Object tag , String msg);
}
