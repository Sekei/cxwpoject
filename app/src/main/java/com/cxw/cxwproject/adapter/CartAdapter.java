package com.cxw.cxwproject.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.ShoppingCartBean;
import com.cxw.cxwproject.bean.ShoppingCartBean.ProductBean;
import com.cxw.cxwproject.http.Api;
import com.cxw.cxwproject.http.BeanRequest.OnResponse;
import com.cxw.cxwproject.imagedisplay.ImageLoader;
import com.cxw.cxwproject.tool.CartTool;
import com.cxw.cxwproject.widget.ToastUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class CartAdapter extends BaseAdapter {
    DecimalFormat df = new DecimalFormat("0.00");// 保留2位小数
    private Handler mHandler;
    private LayoutInflater listContainer;
    private List<ShoppingCartBean> listitems; // 数据集合List
    private HashMap<Integer, Boolean> isSelected;
    private ListItemView listview;
    private int shopNum;// 数量

    public class ListItemView {
        public ImageView img;
        public TextView name, cart_money, cart_add, cart_reduction, cart_name;
        public TextView cart_num;
        public CheckBox shop_check; // 商品选择按钮
        public LinearLayout mCartPrompt;//温馨提示
    }

    @SuppressLint("UseSparseArrays")
    public CartAdapter(Context context, Handler mHandler, List<ShoppingCartBean> list) {
        this.listitems = list;
        listContainer = LayoutInflater.from(context);
        this.mHandler = mHandler;
        isSelected = new HashMap<Integer, Boolean>();
        initDate();
    }

    /**
     * 数据刷新
     *
     * @param list
     */
    public void notifyDataSetInvalidated(List<ShoppingCartBean> list) {
        this.listitems = list == null ? new ArrayList<ShoppingCartBean>() : new ArrayList<ShoppingCartBean>(list);
        initDate();
        notifyDataSetChanged();
    }

    /**
     * 初始化isSelected的数据
     */
    private void initDate() {
        for (int i = 0; i < listitems.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    public HashMap<Integer, Boolean> getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        this.isSelected = isSelected;
    }

    public int getCount() {
        return listitems.size();
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public View getView(final int position, View helper, ViewGroup parent) {
        if (helper == null) {
            listview = new ListItemView();
            helper = listContainer.inflate(R.layout.item_cart, parent, false);
            listview.mCartPrompt = (LinearLayout) helper.findViewById(R.id.mCartPrompt);
            listview.cart_money = (TextView) helper.findViewById(R.id.cart_money);// 价格
            listview.img = (ImageView) helper.findViewById(R.id.img);// 图片
            listview.cart_reduction = (TextView) helper.findViewById(R.id.cart_reduction);// 减少
            listview.cart_add = (TextView) helper.findViewById(R.id.cart_add); // 添加
            listview.cart_num = (TextView) helper.findViewById(R.id.cart_num);// 数量
            listview.shop_check = (CheckBox) helper.findViewById(R.id.shop_check); // 选中
            listview.cart_name = (TextView) helper.findViewById(R.id.cart_name);// 名字
            helper.setTag(listview);
        } else {
            listview = (ListItemView) helper.getTag();
        }
        if (position == 0) {
            listview.mCartPrompt.setVisibility(View.VISIBLE);
        } else {
            listview.mCartPrompt.setVisibility(View.GONE);
        }
        ProductBean mproduct = listitems.get(position).getProduct();
        ImageLoader.displayImage(mproduct.getImage(), listview.img);
        listview.cart_name.setText(mproduct.getName() + "\t" + mproduct.getAttribute());
        listview.cart_money.setText(df.format(mproduct.getPrice()));
        listview.cart_num.setText(String.valueOf(listitems.get(position).getNumber()));
        listview.shop_check.setTag(position);
        listview.shop_check.setChecked(getIsSelected().get(position));
        listview.shop_check.setOnClickListener(new CheckBoxChangedListener());
        listview.cart_add.setTag(position);
        listview.cart_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = (Integer) view.getTag();
                shopNum = listitems.get(position).getNumber();
                shopNum++;
                getModifyCart(listitems.get(position).getChart_id(), shopNum, position);
            }
        });
        listview.cart_reduction.setTag(position);
        listview.cart_reduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int position = (Integer) view.getTag();
                shopNum = listitems.get(position).getNumber();
                if (shopNum > 1) {
                    shopNum--;
                    getModifyCart(listitems.get(position).getChart_id(), shopNum, position);
                }
            }
        });
        return helper;
    }

    // CheckBox选择改变监听器
    private final class CheckBoxChangedListener implements OnClickListener {
        @Override
        public void onClick(View cb) {
            int position = (Integer) cb.getTag();
            getIsSelected().put(position, ((CheckBox) cb).isChecked());
            ShoppingCartBean bean = listitems.get(position);
            bean.setChoosed(((CheckBox) cb).isChecked());
            // 如果所有的物品全部被选中，则全选按钮也默认被选中
            mHandler.sendMessage(
                    mHandler.obtainMessage(11, CartTool.getInstance().isAllSelected(listitems, getIsSelected())));
            mHandler.sendMessage(mHandler.obtainMessage(10, CartTool.getInstance().getTotalPrice(listitems)));
            // 数量
            mHandler.sendMessage(mHandler.obtainMessage(13, CartTool.getInstance().getShopNumber(listitems)));
        }
    }

    /**
     * 修改购物车
     *
     * @param chart_id
     * @param number
     */
    private void getModifyCart(String chart_id, int number, final int position) {
        Api.getModifyCart(chart_id, number, new OnResponse<String>() {
            @Override
            public void onResponse(String item) {
                listitems.get(position).setNumber(shopNum);
                mHandler.sendMessage(mHandler.obtainMessage(12, CartTool.getInstance().getTotalPrice(listitems)));
                mHandler.sendMessage(mHandler.obtainMessage(13, CartTool.getInstance().getShopNumber(listitems)));
            }

            @Override
            public void onErrorResponse(String error) {
                ToastUtils.show(error);
            }
        });
    }
}
