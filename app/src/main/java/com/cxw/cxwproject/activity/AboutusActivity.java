package com.cxw.cxwproject.activity;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.R;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutusActivity extends ActionBarActivity implements View.OnClickListener {
    private PackageInfo packInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_aboutus;
    }

    @Override
    public int getActionBarType() {
        return ACTIONBAR_NOT_SHOW;
    }

    @Override
    protected void initView() {
        TextView title = (TextView) findViewById(R.id.title);
        title.setText("关于我们");
        ImageView back = (ImageView) findViewById(R.id.back); // 返回
        back.setOnClickListener(this);
        // 版本号获取
        TextView setup_versionname = (TextView) findViewById(R.id.setup_versionname);
        PackageManager packageManager = this.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        try {
            packInfo = packageManager.getPackageInfo(AboutusActivity.this.getPackageName(), 0);
            setup_versionname.setText("版本号v" + packInfo.versionName);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        // 特别申明
        TextView statement = (TextView) findViewById(R.id.statement);
        statement.setOnClickListener(this);
        // 欢迎页面
        TextView novice = (TextView) findViewById(R.id.novice);
        novice.setOnClickListener(this);
        // 版权页面
        TextView copyright = (TextView) findViewById(R.id.copyright);
        copyright.setOnClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.statement:
                startActivity(StatementActivity.class);
                break;
            case R.id.novice:
                startActivity(NoviceActivity.class);
                break;
            case R.id.copyright:
                startActivity(CopyrightActivity.class);
                break;
        }
    }
}
