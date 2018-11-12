package com.hasee.oracletest.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.hasee.oracletest.R;
import com.hasee.oracletest.fragment.AddDialog;
import com.hasee.oracletest.fragment.UpdateFragment;

import net.sf.json.JSONObject;

import java.util.List;

public class AddMsgAdapter extends BaseAdapter {
    private Context mContext;
    private List<List<String>> stringList;

    public AddMsgAdapter(Context mContext, List<List<String>> stringList){
        this.mContext = mContext;
        this.stringList = stringList;
        AddDialog.strings = new String[stringList.size()];
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
        final AddMsgAdapter.ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(mContext).inflate(R.layout.addmsg_item,viewGroup,false);
            viewHolder = new AddMsgAdapter.ViewHolder();
            viewHolder.textView_attribute = (TextView)view.findViewById(R.id.addmsg_item_tv_attribute);
            viewHolder.textView_key = (TextView)view.findViewById(R.id.addmsg_item_tv_key);
            viewHolder.textView_value = (EditText)view.findViewById(R.id.addmsg_item_et_value);
            view.setTag(viewHolder);
        }else{
            view = convertView;
            viewHolder = (AddMsgAdapter.ViewHolder) view.getTag();
        }
        viewHolder.position = i;
        viewHolder.textView_attribute.setText(stringList.get(i).get(0));
        viewHolder.textView_key.setText(stringList.get(i).get(1));
        viewHolder.textView_key.setHint("必填项");
        if("INT".equalsIgnoreCase(stringList.get(i).get(0))||"DOUBLE".equalsIgnoreCase(stringList.get(i).get(0))||"FLOAT".equalsIgnoreCase(stringList.get(i).get(0))){
            viewHolder.textView_value.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        viewHolder.textView_value.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                AddDialog.strings[viewHolder.position] = editable.toString();
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
