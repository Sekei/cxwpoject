package com.cxw.cxwproject.util.mvc;

/**
 * Created by Administrator on 2017/8/24.
 */

public interface ILoadView {
    public void showLoading();

    public void showFail(String e);

    public void showEmpty();

    public void tipFail(String e);

    public void hide();
}
