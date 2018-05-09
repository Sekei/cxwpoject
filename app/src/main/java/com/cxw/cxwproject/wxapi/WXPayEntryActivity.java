package com.cxw.cxwproject.wxapi;

import android.content.Intent;
import android.provider.SyncStateContract;
import android.view.KeyEvent;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.widget.ToastUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by admin on 2018/4/2.
 */

public class WXPayEntryActivity extends ActionBarActivity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_wxpayentry;
    }

    @Override
    public int getActionBarType() {
        return ACTIONBAR_DARK;
    }

    @Override
    protected void initView() {
        super.initView();
        api = WXAPIFactory.createWXAPI(this, "wx0a8cfd71599ccd8a");
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                ToastUtils.show("支付成功");
                //广播传值更新数据
                sendBroadcast(new Intent("com.cxw.rechager"));
            } else if (resp.errCode == -1) {
                ToastUtils.show("支付失败");
            } else if (resp.errCode == -2) {
                ToastUtils.show("取消支付");
            } else {
                ToastUtils.show("未知错误");
            }
        }
        getOnBackPressed();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            getOnBackPressed();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void getOnBackPressed() {
        this.finish();
        overridePendingTransition(0, 0);
    }
}
