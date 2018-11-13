package com.hasee.onlinedb.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hasee.onlinedb.MyListener;
import com.hasee.onlinedb.R;
import com.hasee.onlinedb.SelectActivity;

import net.sf.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class SelectAdapter extends BaseAdapter {
    private static final String TAG = "SelectAdapter";
    private Context mContext;
    private List<String[]> list = new ArrayList<>();
    private MyListener listener;
    private int currentPosition;

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
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
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
            viewHolder.selectItemButtonDelect.setOnClickListener(onClickListener);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.position = i;
        //取默认值
//        viewHolder.selectItemTvValue.setText(SelectActivity.list.get(i)[0]);
//        viewHolder.selectItemTvCondition.setText(SelectActivity.list.get(i)[1]);
//        viewHolder.selectItemButtonConditon.setText(SelectActivity.list.get(i)[3]);
        return view;
    }

    class ViewHolder{
        TextView selectItemTvValue;
        TextView selectItemTvCondition;
        TextView selectItemEt;
        Button selectItemButtonConditon;
        Button selectItemButtonDelect;
        int position;
    }

    /*
    * 局部刷新
    * */
    public void updateView(View view, int itemIndex) {
        if(view == null){
            return;
        }
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.selectItemTvValue = (TextView) view.findViewById(R.id.select_item_tv_value);
        viewHolder.selectItemTvCondition = (TextView) view.findViewById(R.id.select_item_tv_condition);
        viewHolder.selectItemButtonConditon = (Button) view.findViewById(R.id.select_item_button_conditon);
        setData(viewHolder,itemIndex);
    }
    /*
     *设置数据
     * */
    public void setData(ViewHolder viewHolder,int itemIndex){
        viewHolder.selectItemTvValue.setText(SelectActivity.list.get(itemIndex)[0]);
        viewHolder.selectItemTvCondition.setText(SelectActivity.list.get(itemIndex)[1]);
        viewHolder.selectItemButtonConditon.setText(SelectActivity.list.get(itemIndex)[3]);
    }


    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            JSONArray jsonArray = new JSONArray();
            switch (view.getId()){
                case R.id.select_item_button_delect:
                    Log.d(TAG, "onClick: "+currentPosition);
                    SelectActivity.list.remove(currentPosition);
                    SelectActivity.adapter.notifyDataSetChanged();
                    break;
                case R.id.select_item_tv_value://选择列名
                    jsonArray.add("0");
                    jsonArray.add(currentPosition);
                    listener.sendMessage(jsonArray);
                    break;
                case R.id.select_item_tv_condition://选择运算符
                    jsonArray.add("1");
                    jsonArray.add(currentPosition);
                    listener.sendMessage(jsonArray);
                    break;
                case R.id.select_item_button_conditon://选择
                    jsonArray.add("2");
                    jsonArray.add(currentPosition);
                    listener.sendMessage(jsonArray);
                    break;
            }
            Log.d(TAG, "onClick: "+currentPosition);
        }
    };
}
