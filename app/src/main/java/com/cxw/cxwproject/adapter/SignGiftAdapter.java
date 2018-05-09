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
 * Created by Administrator on 2017/8/25.
 */

public class SignGiftAdapter extends QuickAdapter<RewardBean> {
    int display;

    public SignGiftAdapter(Context context, int layoutResId, List<RewardBean> mData) {
        super(context, layoutResId, mData);
        display = MyApp.getApp().getDisplayHightAndWightPx()[1];
    }

    @Override
    protected void convert(BaseAdapterHelper helper, RewardBean item) {
        helper.setText(R.id.days, item.getDays() + "天");
        String score = item.getScore();
        helper.setText(R.id.title, Html.fromHtml("送<font color='#FF5449'>" + score + "</font>积分"));
        int height = display / 6;
        helper.setImageViewLinearLayout(R.id.img, height, height).setImageUrl(R.id.img, item.getImg_url());
    }
}
