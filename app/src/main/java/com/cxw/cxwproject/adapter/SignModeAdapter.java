package com.cxw.cxwproject.adapter;

import android.content.Context;

import com.cxw.cxwproject.R;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/8/25.
 */

public class SignModeAdapter extends QuickAdapter<String> {
    int res[] = { R.drawable.sign_wx, R.drawable.sign_micro_blog, R.drawable.sign_shres };
    String str[] = { "100积分", "100积分", "+10积分" };

    public SignModeAdapter(Context context, int layoutResId) {
        super(context, layoutResId, Arrays.asList("关注微信", "关注微博", "每日分享"));
    }
    @Override
    protected void convert(BaseAdapterHelper helper, String item) {
        helper.setText(R.id.title, item);
        helper.setImageResource(R.id.img, res[helper.getPosition()]);
        helper.setText(R.id.sign_sum, str[helper.getPosition()]);
    }
}
