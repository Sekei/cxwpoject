package com.cxw.cxwproject.inter;

/**
 * 接口请求成功回调接口
 * Created by Administrator on 2017/8/28.
 */

public interface ResponseInter<T> {
    void onResponse(T mData);
}
