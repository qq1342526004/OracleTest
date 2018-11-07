package com.hasee.oracletest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.hasee.oracletest.R;

import java.util.List;

public class UpdateMsgAdapter extends BaseAdapter {
    private Context mContext;
    private List<List<String>> stringList;

    public UpdateMsgAdapter(Context mContext,List<List<String>> stringList){
        this.mContext = mContext;
        this.stringList = stringList;
    }

    @Override
    public int getCount() {
        return stringList.size();
    }

    @Override
    public Object getItem(int i) {
        return stringList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view;
        UpdateMsgAdapter.ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.updatemsg_item,viewGroup,false);
            viewHolder = new UpdateMsgAdapter.ViewHolder();
            viewHolder.textView_key = (TextView)view.findViewById(R.id.updatemsg_item_tv_key);
            viewHolder.textView_value = (EditText)view.findViewById(R.id.updatemsg_item_et_value);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (UpdateMsgAdapter.ViewHolder) view.getTag();
        }
        viewHolder.textView_key.setText(stringList.get(i).get(0));
        viewHolder.textView_value.setText(stringList.get(i).get(1));
        return view;
    }

    class ViewHolder{
        TextView textView_key;
        EditText textView_value;
    }
}
