package com.example.classchat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.classchat.Activity.Activity_ModifyAddress;
import com.example.classchat.Object.Object_Adress;
import com.example.classchat.R;

import java.util.List;

public class Adapter_AddressList extends BaseAdapter {
    LayoutInflater inflater;
    List<Object_Adress> ls;
    Context mContext;


    public Adapter_AddressList(Context context, List<Object_Adress> objects){
        mContext=context;
        this.inflater=LayoutInflater.from(context);
        this.ls=objects;

    }

    public boolean isEnabled(int position) {
        return false;
    }
    @Override
    public int getCount() {
        return ls.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Object_Adress item=ls.get(position);
        View v=inflater.inflate(R.layout.item_address,null);
        TextView name = (TextView)v.findViewById(R.id.name_item_adress);
        TextView phone = (TextView)v.findViewById(R.id.phone_item_address);
        TextView detail = (TextView)v.findViewById(R.id.detail_item_adress);
        TextView default_ = (TextView)v.findViewById(R.id.default_item_address);
        Button modify = (Button)v.findViewById(R.id.modify_item_address);

        name.setText(item.getName());
        phone.setText(item.getPhone());
        detail.setText(item.totalAddrss());
        if (item.getDefaut()){
            default_.setVisibility(View.VISIBLE);
        }else {
            default_.setVisibility(View.GONE);
        }
        modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, Activity_ModifyAddress.class);
                intent.putExtra("address",item);
                mContext.startActivity(intent);
            }
        });
        return v;
    }
}