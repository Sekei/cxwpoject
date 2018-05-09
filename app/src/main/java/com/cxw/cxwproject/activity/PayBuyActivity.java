package com.cxw.cxwproject.activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import com.alipay.sdk.app.PayTask;
import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.adapter.PayBuyAdapter;
import com.cxw.cxwproject.bean.BuyBean;
import com.cxw.cxwproject.bean.ChargesBean;
import com.cxw.cxwproject.bean.PayMentBean;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.imp.PayBuyDateImp;
import com.cxw.cxwproject.inter.ResponseInter;
import com.cxw.cxwproject.widget.PayResult;
import com.cxw.cxwproject.widget.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;



import java.util.Map;

//import org.json.JSONObject;

public class PayBuyActivity extends ActionBarActivity implements ResponseInter<PayMentBean>, OnClickListener, OnItemClickListener {
    private PayBuyAdapter adapter;
    private int REQUEST_CODE_PAYMENT = 1;
    private BuyBean mBuyDataBean;
    private LoadingDialog dialog;
    private int mPayType = 0;//默认使用支付宝
    private static final int SDK_PAY_FLAG = 1001;
    private String charges;
    private IWXAPI msgApi; //微信
    private Gson gson;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_payment;
    }

    @Override
    public int getActionBarType() {
        return ACTIONBAR_NOT_SHOW;
    }

    @Override
    protected void initView() {
        mBuyDataBean = (BuyBean) getIntent().getSerializableExtra("buydata");
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("订单支付");
        ImageView back = (ImageView) findViewById(R.id.back); // 返回
        back.setOnClickListener(this);
        //接口请求成功数据反馈
        PayBuyDateImp.getInstance().ResponseInter(this);
        adapter = new PayBuyAdapter(this, R.layout.paymentoptions_item);
        // 支付列表
        ListView pay_list = (ListView) findViewById(R.id.pay_list);
        pay_list.setOnItemClickListener(this);
        pay_list.setAdapter(adapter);
        // 确认支付按钮
        TextView buy_pay_btn = (TextView) findViewById(R.id.buy_pay_btn);
        buy_pay_btn.setOnClickListener(this);
        //支付宝沙漏
        //EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        //初始化WX
        //商户APP工程中引入微信JAR包，调用API前，需要先向微信注册您的APPID，代码如下：
        msgApi = WXAPIFactory.createWXAPI(this, null);
        // 将该app注册到微信
        msgApi.registerApp("wx0a8cfd71599ccd8a");
        //这两句代码必须的，为的是初始化出来gson这个对象，才能拿来用
        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();
        //注册广播
        IntentFilter filter = new IntentFilter("com.cxw.rechager");
        registerReceiver(broadcastReceiver, filter);
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            PaymentSuccess();//支付成功
        }
    };

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        this.mPayType = arg2;
        adapter.notifyDataSetInvalidated(arg2);
    }
//
//    // 跳转后的回调方法
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CODE_PAYMENT) {// 支付页面返回处理
//            if (resultCode == Activity.RESULT_OK) {
//                dialog.dismiss();
//                String result = data.getExtras().getString("pay_result");
//                if (result.equals("success")) {// 支付成功
//                    PaymentSuccess();
//                } else {
//                    ToastUtils.show("订单支付失败");
//                }
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.buy_pay_btn:
                dialog = new LoadingDialog(this);
//                //这里先注释支付宝，默认是1
//                mPayType = 1;
                PayBuyDateImp.getInstance().PayBuyDateImpApi(dialog, mBuyDataBean.getOrder_id(), mPayType);
                break;
        }

    }

    @Override
    public void onResponse(PayMentBean mData) {
        if (mPayType == 0) {//支付宝支付
            charges = mData.getCharges();
            try {
                org.json.JSONObject obj = new org.json.JSONObject(charges);
                charges=obj.getString("order_string");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("===========>>" + charges);
            Runnable payRunnable = new Runnable() {
                @Override
                public void run() {
                    PayTask alipay = new PayTask(PayBuyActivity.this);
                    // 调用支付接口，获取支付结果
                    //String result = alipay.pay(charges, true);
                    Map<String, String> result = alipay.payV2(charges, true);
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        } else {
            if (isJson(mData.getCharges())) {
                ChargesBean mChargesBean = gson.fromJson(mData.getCharges(), ChargesBean.class);
                setWxPay(mChargesBean);//微信支付
            } else {
                ToastUtils.show(mData.getCharges());
            }
        }
//        Intent intent = new Intent();
//        String packageName = getPackageName();
//        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
//        intent.setComponent(componentName);
//        intent.putExtra(PaymentActivity.EXTRA_CHARGE, mData.getCharges());
//        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    public boolean isJson(String content) {
        try {
            JSONObject jsonStr = JSONObject.parseObject(content);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //微信支付
    private void setWxPay(ChargesBean wxData) {
        PayReq request = new PayReq();
        request.appId = wxData.getAppid();
        request.partnerId = wxData.getPartnerid();
        request.prepayId = wxData.getPrepayid();
        request.packageValue = "Sign=WXPay";
        request.nonceStr = wxData.getNoncestr();
        request.timeStamp = wxData.getTimestamp();
        request.sign = wxData.getSign();
        msgApi.sendReq(request);
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultStatus = payResult.getResultStatus();
                    if ("9000".equals(resultStatus)) {
                        ToastUtils.show("支付成功");
                        PaymentSuccess();
                    } else if ("6001".equals(resultStatus)) {
                        ToastUtils.show("取消支付");
                    } else if ("8000".equals(resultStatus)) {
                        ToastUtils.show("支付结果确认中");
                    } else {
                        ToastUtils.show("支付失败" + resultStatus);
                    }
                    break;
            }
        }
    };


    /*
     * 支付成功
     */
    private void PaymentSuccess() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("支付成功").setMessage("您可以在订单管理查看您的订单进程。").setPositiveButton("返回",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.setNegativeButton("查看订单", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(PayBuyActivity.this, OrderManagementActivity.class);
                intent.putExtra("id", 0);
                startActivity(intent);
                dialog.dismiss();
                finish();
            }
        });
        dialog.setCancelable(false); // 设置对话框以外的地方都不可以点击
        dialog.create().show();
    }
}
