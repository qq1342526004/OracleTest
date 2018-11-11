package com.hasee.oracletest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hasee.oracletest.MyListener;
import com.hasee.oracletest.R;
import com.hasee.oracletest.SelectActivity;

import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SelectAdapter extends BaseAdapter {
    private Context mContext;
    private List<String[]> list = new ArrayList<>();
    private MyListener listener;

    public SelectAdapter(Context context,List<String[]> list,MyListener listener){
        this.mContext = context;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        View view;
        if(convertView == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.select_item,viewGroup,false);
            viewHolder = new ViewHolder();
            viewHolder.selectItemTvValue = (TextView) view.findViewById(R.id.select_item_tv_value);
            viewHolder.selectItemTvValue.setOnClickListener(onClickListener);
            viewHolder.selectItemTvCondition = (TextView) view.findViewById(R.id.select_item_tv_condition);
            viewHolder.selectItemTvCondition.setOnClickListener(onClickListener);
            viewHolder.selectItemEt = (EditText) view.findViewById(R.id.select_item_et);
            viewHolder.selectItemButtonConditon = (Button) view.findViewById(R.id.select_item_button_conditon);
            viewHolder.selectItemButtonConditon.setOnClickListener(onClickListener);
            viewHolder.selectItemButtonDelect = (Button) view.findViewById(R.id.select_item_button_delect);
            viewHolder.selectItemButtonDelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SelectActivity.list.remove(i);
                    SelectActivity.adapter.notifyDataSetChanged();
                }
            });
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        return view;
    }

    class ViewHolder{
        TextView selectItemTvValue;
        TextView selectItemTvCondition;
        TextView selectItemEt;
        Button selectItemButtonConditon;
        Button selectItemButtonDelect;
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            JSONArray jsonArray = new JSONArray();
            switch (view.getId()){
                case R.id.select_item_tv_value://选择列名
                    jsonArray.add("0");
                    listener.sendMessage(jsonArray);
                    break;
                case R.id.select_item_tv_condition://选择运算符
                    jsonArray.add("1");
                    listener.sendMessage(jsonArray);
                    break;
                case R.id.select_item_button_conditon://选择
                    jsonArray.add("2");
                    listener.sendMessage(jsonArray);
                    break;
            }
        }
    };
}
