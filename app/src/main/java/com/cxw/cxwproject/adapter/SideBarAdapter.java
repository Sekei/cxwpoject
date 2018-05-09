package com.cxw.cxwproject.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cxw.cxwproject.R;
import com.cxw.cxwproject.bean.ContactsModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 侧栏适配器
 */
public class SideBarAdapter extends BaseAdapter {
    //是否第一个Item的
    private boolean isFirstItemLetter = true;
    //记录是否显示字母标题，键为字母，值为下标
    private Map<String, Integer> map;
    private List<ContactsModel> mDatas;
    private int mItemLayoutId;
    private Context context;
    private boolean flag = true;
    
    public SideBarAdapter(Context mContext, List<ContactsModel> mDatas, int mItemLayoutId) {
        this.mDatas = mDatas;
        map = new HashMap<String, Integer>();
        this.mItemLayoutId = mItemLayoutId;
        this.context = mContext;

        traverseList();
    }

    /**
     * 遍历列表
     * 由于传进来的mDatas是一个已排好序的列表，遍历整个列表，每遇到分类的第一个字母就把下标记录下来
     */
    public void traverseList() {
        //获取初始值
        String current = mDatas.get(0).getFirstLetter();
        for (int i = 0; i < mDatas.size(); i++) {
            char tempChar = mDatas.get(i).getFirstLetter().charAt(0);
            String tempFirstLetter = mDatas.get(i).getFirstLetter();

            if (tempFirstLetter.equals(current) || (tempChar < 'A' || tempChar > 'Z')) {
                if (isFirstItemLetter) {
                    map.put(current, i);
                }
            } else {
                //更新初始值
                current = mDatas.get(i).getFirstLetter();
                map.put(current, i);
            }
            isFirstItemLetter = false;
        }
    }


    /**
     * 获取当前字母的下标
     *
     * @return
     */
    public int getCurrentLetterPosition(String currentLetter) {
        if (map.get(currentLetter) != null) {
            return map.get(currentLetter);
        } else
            return -1;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(mItemLayoutId, null);
            viewHolder.txtFirstLetter = (TextView) convertView.findViewById(R.id.txt_letter_category);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txt_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //判断是否显示字母标题
        if (map.get(mDatas.get(position).getFirstLetter()) != null && map.get(mDatas.get(position).getFirstLetter()).equals(position)) {
            viewHolder.txtFirstLetter.setVisibility(View.VISIBLE);
            viewHolder.txtFirstLetter.setText(mDatas.get(position).getFirstLetter());
        } else {
            viewHolder.txtFirstLetter.setVisibility(View.GONE);
        }
        viewHolder.txtName.setText(mDatas.get(position).getName());
        return convertView;
    }
    public class ViewHolder {
        TextView txtFirstLetter, txtName;
    }
}

