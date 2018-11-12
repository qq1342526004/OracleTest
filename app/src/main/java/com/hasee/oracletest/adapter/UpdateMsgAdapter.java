package com.hasee.oracletest.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.hasee.oracletest.R;
import com.hasee.oracletest.fragment.UpdateFragment;

import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
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
        final UpdateMsgAdapter.ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.updatemsg_item,viewGroup,false);
            viewHolder = new UpdateMsgAdapter.ViewHolder();
            viewHolder.textView_attribute = (TextView)view.findViewById(R.id.updatemsg_item_tv_attribute);
            viewHolder.textView_key = (TextView)view.findViewById(R.id.updatemsg_item_tv_key);
            viewHolder.textView_value = (EditText)view.findViewById(R.id.updatemsg_item_et_value);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (UpdateMsgAdapter.ViewHolder) view.getTag();
        }
        viewHolder.position = i;
        viewHolder.textView_attribute.setText(stringList.get(i).get(0));
        viewHolder.textView_key.setText(stringList.get(i).get(1));
        viewHolder.textView_value.setHint(stringList.get(i).get(2));
        viewHolder.textView_value.addTextChangedListener(new TextWatcher() {
            JSONObject jsonObject = new JSONObject();
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!"".equals(editable.toString().trim())){
                    UpdateFragment.hashMap.put(viewHolder.position,editable.toString());
                }else{
                    UpdateFragment.hashMap.remove(viewHolder.position);
                }
            }
        });
        return view;
    }

    class ViewHolder{
        TextView textView_attribute;
        TextView textView_key;
        EditText textView_value;
        int position;
    }
}
