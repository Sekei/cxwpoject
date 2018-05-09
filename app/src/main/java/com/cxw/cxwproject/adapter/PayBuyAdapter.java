package com.cxw.cxwproject.adapter;

import android.content.Context;
import android.text.Html;

import com.cxw.cxwproject.MyApp;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.RewardBean;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * 支付方式adapter
 * Created by Administrator on 2017/8/25.
 */

public class PayBuyAdapter extends QuickAdapter<String> {
    private int res[] = {R.drawable.pay0, R.drawable.pay1};//
    private String str[] = {"推荐有支付宝用户", "微信快捷支付"};//
    private int mPayType = 0;// 标记选中的支付方式

    public PayBuyAdapter(Context context, int layoutResId) {//
        super(context, layoutResId, Arrays.asList("支付宝支付", "微信支付"));
    }

    public void notifyDataSetInvalidated(int mPayType) {
        this.mPayType = mPayType;
        notifyDataSetInvalidated();
    }

    @Override
    protected void convert(BaseAdapterHelper helper, String item) {
        helper.setText(R.id.paytextview, item);
        helper.setImageResource(R.id.payimg, res[helper.getPosition()]);
        helper.setText(R.id.pay_prompt, str[helper.getPosition()]);
        if (mPayType == helper.getPosition()) {
            helper.setImageResource(R.id.payicon, R.drawable.checkbox);
        } else {
            helper.setImageResource(R.id.payicon, R.drawable.checkbox_cart_empty);
        }
    }
}
