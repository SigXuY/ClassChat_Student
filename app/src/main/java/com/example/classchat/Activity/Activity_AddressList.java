package com.example.classchat.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.classchat.Adapter.Adapter_AddressList;
import com.example.classchat.Object.Object_Adress;
import com.example.classchat.R;
import com.example.library_cache.Cache;

import java.util.ArrayList;
import java.util.List;

public class Activity_AddressList extends AppCompatActivity {

    private ImageView back;
    private Button add;
    private ListView addressList;
    private List<Object_Adress> aList = new ArrayList<>();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        mContext = Activity_AddressList.this;

        back = findViewById(R.id.back_addressList);
        add = findViewById(R.id.newaddress);
        addressList = findViewById(R.id.address_list);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Activity_NewAddress.class);
                startActivity(intent);
            }
        });
    }
    private void setList(){
        aList =  Cache.with(mContext)
                .path(getCacheDir(mContext))
                .getCache("AddressList", List.class);
        if(aList == null || aList.size() <= 0){
            aList = new ArrayList<>();
        }
        addressList.setAdapter(new Adapter_AddressList(mContext,aList));
    }
    @Override
    protected void onResume() {
        super.onResume();
        setList();
    }

    /*
     * 获得缓存地址
     * */
    public String getCacheDir(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }
}
