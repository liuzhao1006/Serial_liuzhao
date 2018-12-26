package com.lz.serial.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


/**
 * 作者      : 刘朝
 * 创建日期  : 2018/12/22 下午11:06
 * 描述     :
 */
public class ReadAdapter extends BaseAdapter {

    private Context context;
    private List<String> mList;
    public ReadAdapter(Context context){
        this.context = context;
    }

    public void notify(List<String> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(mList == null){
            return 0;
        }
        return mList.size();
    }

    @Override
    public String getItem(int i) {
        if(mList == null){
            return "";
        }
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder mHolder;
        if(view == null){
            mHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);
            mHolder.mTextView = view.findViewById(android.R.id.text1);
            view.setTag(mHolder);
        }else {
            mHolder = (ViewHolder) view.getTag();
        }

        if(mList != null){
            mHolder.mTextView.setText(mList.get(i));
        }
        return view;
    }

    private class ViewHolder {
        public TextView mTextView;
    }
}
