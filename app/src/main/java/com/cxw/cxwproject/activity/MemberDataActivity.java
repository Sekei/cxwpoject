package com.cxw.cxwproject.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.cxw.cxwproject.ActionBarActivity;
import com.cxw.cxwproject.AppConfig;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.UserBean;
import com.cxw.cxwproject.dialog.MemberLogoDialog;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.imagedisplay.ImageLoader;
import com.cxw.cxwproject.tool.SharePreferenceUtil;
import com.cxw.cxwproject.util.UploadUtil;
import com.cxw.cxwproject.util.UploadUtil.OnUploadProcessListener;
import com.cxw.cxwproject.util.camera.CameraProxy;
import com.cxw.cxwproject.util.mvc.MVCHelper;
import com.cxw.cxwproject.util.mvc.MVCHelper.RequestListener;
import com.cxw.cxwproject.util.mvc.MVCViewHelper;
import com.cxw.cxwproject.widget.EmptyLayout;
import com.cxw.cxwproject.widget.ToastUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MemberDataActivity extends ActionBarActivity
		implements OnUploadProcessListener, OnClickListener, RequestListener {
	private ImageView member_logo;
	private MVCViewHelper<UserBean> mvcViewHelper;
	private TextView nickname, gender;
	private CameraProxy cameraProxy;
	UploadUtil uploadUtil;// 文件上传工具类

	@Override
	protected int getLayoutId() {
		return R.layout.activity_memberdata;
	}

	@Override
	public int getActionBarType() {
		return ACTIONBAR_NOT_SHOW;
	}

	@Override
	protected void initView() {
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("个人资料");
		ImageView back = (ImageView) findViewById(R.id.back); // 返回
		back.setOnClickListener(this);
		// 头像修改
		member_logo = (ImageView) findViewById(R.id.member_logo);// 头像
		LinearLayout member_logo_btn = (LinearLayout) findViewById(R.id.member_logo_btn);
		member_logo_btn.setOnClickListener(this);
		// 昵称---性别---修改
		LinearLayout news_btn = (LinearLayout) findViewById(R.id.news_btn);
		news_btn.setOnClickListener(this);
		LinearLayout setup_update = (LinearLayout) findViewById(R.id.setup_update);
		setup_update.setOnClickListener(this);
		// 忘记密码
		TextView modify_btn = (TextView) findViewById(R.id.modify_btn);
		modify_btn.setOnClickListener(this);
		// -用户名--昵称---性别
		TextView usermobile = (TextView) findViewById(R.id.usermobile);
		usermobile.setText(new SharePreferenceUtil().getMember());
		nickname = (TextView) findViewById(R.id.nickname);
		gender = (TextView) findViewById(R.id.gender);
		// 网络请求加载显示页面
		EmptyLayout emptyLayout = (EmptyLayout) findViewById(R.id.empty_view);
		mvcViewHelper = MVCHelper.createViewHelper(null, emptyLayout);
		mvcViewHelper.setRequestListener(this);
		mvcViewHelper.refresh();
		// ==============头像处理相关=======================
		uploadUtil = UploadUtil.getInstance();
		uploadUtil.setOnUploadProcessListener(this);
		cameraProxy = new CameraProxy(this);
		// 拍照成功，裁剪成功，选择图片成功的回调
		cameraProxy.setPhotoHelper(new CameraProxy.PhotoHelper() {
			@Override
			public void success(String path) {
				ImageLoader.displayImage("file://" + path, member_logo);
				Map<String, String> params = new HashMap<String, String>();
				params.put("sid", "20003");
				uploadUtil.uploadFile(MemberDataActivity.this, new File(path).getPath(), "avatarFile",
						AppConfig.BASE_URL, params);
			}
		});
	}

	/**
	 * 请求用户详情
	 */
	@Override
	public void requestData(int page) {
		Api.getShopDetail(new OnResponse<UserBean>() {
			@Override
			public void onResponse(UserBean o) {
				mvcViewHelper.executeOnLoadDataSuccess(o);
				ImageLoader.displayImage(o.getAvatar(), member_logo);
				nickname.setText(o.getNickname());
				gender.setText(UserBean.Gender());
			}

			@Override
			public void onErrorResponse(String error) {
				mvcViewHelper.executeOnLoadDataFail(error);
			}
		});
	}

	@SuppressLint("InflateParams")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			finish();
			break;
		case R.id.member_logo_btn:// 头像相关修改
			cameraProxy.setCrop(200, 200);
			new MemberLogoDialog(this, cameraProxy).show();
			break;
		case R.id.modify_btn:// 修改密码
			startActivity(new Intent(MemberDataActivity.this, ForgetPasswordActivity.class));
			break;
		case R.id.news_btn:// 昵称修改
			startActivityForResult(new Intent(MemberDataActivity.this, NicknameActivity.class), 1);
			break;
		case R.id.setup_update:// 性别修改
			startActivityForResult(new Intent(MemberDataActivity.this, GenderActivity.class), 2);
			break;
		default:
			break;
		}

	}

	/**
	 * 第二个页面传回来的值
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 头像修改
		cameraProxy.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 1:// 昵称修改
			nickname.setText(UserBean.defaultShop().getNickname());
			break;
		case 2:// 性别修改
			gender.setText(UserBean.Gender());
			break;
		default:
			break;
		}

	}

	/**
	 * 上传响应
	 * 
	 * @param responseCode
	 * @param message
	 */
	@Override
	public void onUploadDone(int responseCode, String message) {
		if (responseCode==1) {//表示上传成功
			try {
				JSONObject arr=new JSONObject(message);
				//保存头像
				UserBean.defaultShop().setAvatar(arr.getString("avatar"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}else{
			ToastUtils.show(message);
		}
	}

	/**
	 * 上传中
	 * 
	 * @param uploadSize
	 */
	@Override
	public void onUploadProcess(int uploadSize) {
		// TODO Auto-generated method stub

	}

	/**
	 * 准备上传
	 */
	@Override
	public void initUpload(int fileSize) {

	}
}
