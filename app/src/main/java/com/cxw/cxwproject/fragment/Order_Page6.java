package com.cxw.cxwproject.fragment;

import java.util.List;

import com.cxw.cxwproject.BaseFragment;
import com.cxw.cxwproject.R;
import com.cxw.cxwproject.activity.OrderDetailsActivity;
import com.cxw.cxwproject.bean.OrderAllBean;
import com.cxw.cxwproject.dialog.CustomDialog;
import com.cxw.cxwproject.dialog.CustomDialog.Builder;
import com.cxw.cxwproject.dialog.LoadingDialog;
import com.cxw.cxwproject.enums.OderStateEnums;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.util.mvc.MVCHelper;
import com.cxw.cxwproject.util.mvc.MVCHelper.RequestListener;
import com.cxw.cxwproject.util.mvc.MVCListViewHelper;
import com.cxw.cxwproject.widget.EmptyLayout;
import com.cxw.cxwproject.widget.RefreshLayout;
import com.cxw.cxwproject.widget.ToastUtils;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
/**
 * 已完成
 * @author Administrator
 *
 */
public class Order_Page6 extends BaseFragment implements OnItemClickListener, RequestListener {
	android.view.View view;

	MVCListViewHelper<OrderAllBean> mvcHelper;
	QuickAdapter<OrderAllBean> adapter;
	List<OrderAllBean> morderdata;
	int mdata_position;
	String order_id;

	@Override
	protected int getLayoutId() {
		return R.layout.order_page1;
	}

	@Override
	protected void initView(View view) {
		super.initView(view);
		ListView order_list = (ListView) view.findViewById(R.id.order_list);
		order_list.setOnItemClickListener(this);
		RefreshLayout swipeLayout = (RefreshLayout) view.findViewById(R.id.swipelayout);
		EmptyLayout emptyLayout = (EmptyLayout) view.findViewById(R.id.empty_view);
		adapter = new QuickAdapter<OrderAllBean>(getActivity(), R.layout.order_item) {
			@Override
			protected void convert(final BaseAdapterHelper helper, OrderAllBean goods) {
				if (goods.getProducts().size() != 0) {
					helper.setImageUrl(R.id.order_goodsimg, goods.getProducts().get(0).getImage());
					helper.setText(R.id.order_name, goods.getProducts().get(0).getName());
					helper.setText(R.id.order_introduction, goods.getProducts().get(0).getIntroduction());
					helper.setText(R.id.order_number, goods.getProducts().get(0).getNumber());
				}
				helper.setText(R.id.order_no, "订单号：" + goods.getOrder_no());
				helper.setText(R.id.order_status, OderStateEnums.getWeek(goods.getOrder_status()));
				helper.setText(R.id.order_productcount, "共" + goods.getProduct_count() + "个商品，总价￥");
				helper.setText(R.id.order_price, goods.getPrice());
				// 隐藏第一个按钮
				helper.setVisible(R.id.cancel_delete_btn, false);
				// 删除订单
				helper.setText(R.id.payment_btn, "删除订单");
				helper.setOnClickListener(R.id.payment_btn, new OnClickListener() {
					@Override
					public void onClick(View v) {
						order_id = morderdata.get(helper.getPosition()).getOrder_id();
						AlertDialog("您的确定要删除当前订单？", morderdata.get(helper.getPosition()).getOrder_status(),
								helper.getPosition());
					}
				});
			}
		};
		// 网络请求
		mvcHelper = MVCHelper.createListHelper(swipeLayout, order_list, emptyLayout);
		mvcHelper.setAdapter(adapter);
		mvcHelper.setRequestListener(this);
		mvcHelper.refresh();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		this.mdata_position = position;
		Intent mIntent = new Intent(getActivity(), OrderDetailsActivity.class);
		mIntent.putExtra("order_id", morderdata.get(position).getOrder_id());
		mIntent.putExtra("details", morderdata.get(position));
		startActivityForResult(mIntent, 0);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {
			if (resultCode == 102) {// 删除订单
				if (morderdata.size() != 1) {
					morderdata.remove(mdata_position);
					adapter.notifyDataSetInvalidated(morderdata);
				} else {
					mvcHelper.refresh();// 考虑待收货订单不多，顾刷新较好
				}
			}
		}
	}

	/**
	 * 已完成
	 */
	@Override
	public void requestData(int page) {
		Api.getOrderAll(page + 1, "100006", new OnResponse<List<OrderAllBean>>() {
			@Override
			public void onResponse(List<OrderAllBean> o) {
				morderdata = o;
				mvcHelper.executeOnLoadDataSuccess(o);
			}

			@Override
			public void onErrorResponse(String error) {
				mvcHelper.executeOnLoadDataFail(error);
			}
		});
	}

	/**
	 * 删除-取消订单
	 */
	private void AlertDialog(final String msg, final int status, final int position) {
		CustomDialog.Builder builder = new Builder(getActivity());
		builder.setTitle("温馨提示");
		builder.setMessage(msg);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				OrderDelete(position);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * 订单删除
	 * 
	 * @param position
	 */
	private void OrderDelete(final int position) {
		// TriggerLoadingDialog.getInstance().show(getActivity());
		final LoadingDialog loDialog = new LoadingDialog(getActivity());
		Api.getOrderDelete(order_id, new OnResponse<String>() {
			@Override
			public void onResponse(String o) {
				try {
					loDialog.dismiss();
					ToastUtils.show(o);
					if (morderdata.size() != 1) {
						morderdata.remove(position);
						adapter.notifyDataSetInvalidated(morderdata);
					} else {
						mvcHelper.refresh();// 考虑待收货订单不多，顾刷新较好
					}
				} catch (Exception e) {
					e.getMessage();
				}
			}

			@Override
			public void onErrorResponse(String error) {
				ToastUtils.show(error);
				loDialog.dismiss();
			}
		});
	}

}
