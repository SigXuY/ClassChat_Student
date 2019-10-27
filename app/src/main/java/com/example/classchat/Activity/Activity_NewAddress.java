package com.example.classchat.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.example.classchat.Object.Object_Adress;
import com.example.classchat.R;
import com.example.library_cache.Cache;

import java.util.ArrayList;
import java.util.List;



public class Activity_NewAddress extends AppCompatActivity {

    private Context mContext;
    private AlertDialog.Builder alertBuilder;

    private ImageView back;
    private Button save;
    private EditText name;
    private EditText phone;
    private Spinner detail;
//    private Spinner college;
//    private Spinner partition;
    private Switch switch_default;

    private Boolean default_check = false;

    private String name_;
    private String phone_;
    private String detail_;

    private String [] buildings = {"C12","C1","二饭"};


    private JSONArray det = new JSONArray();
    private ArrayAdapter<String> adapter_det= null;

//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 1:
//                    adapter_det = new ArrayAdapter(mContext, R.layout.item_auth,det.toJavaList(String.class));
//                    detail.setAdapter(adapter_det);
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        back = findViewById(R.id.back_newAddress);
        save = findViewById(R.id.address_save);
        name = findViewById(R.id.address_name);
        phone = findViewById(R.id.address_phone);
        detail = findViewById(R.id.address_detail);
        switch_default = findViewById(R.id.address_defaut_check);

        mContext = Activity_NewAddress.this;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Activity_AddressList.class);
                startActivity(intent);
            }
        });

        switch_default.setChecked(false);
        switch_default.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    //选中状态 可以做一些操作
                    default_check = true;
                }else {
                    //未选中状态 可以做一些操作
                    default_check = false;
                }
            }
        });

        adapter_det = new ArrayAdapter(mContext, R.layout.item_auth,buildings);
        detail.setAdapter(adapter_det);

        //TODO 请求楼栋列表url
//        Util_NetUtil.sendOKHTTPRequest("", new Callback() {
//            @Override
//            public void onFailure(@NotNull Call call, @NotNull IOException e) {
//
//            }
//
//            @Override
//            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
//                String responsedata = response.body().string();
//                System.out.println(responsedata);
//                JSONObject jsonObject = JSON.parseObject(responsedata);
//                //对应键值
//                det = jsonObject.getJSONArray("");
//
//                Message message = new Message();
//                message.what = 1;
//                handler.sendMessage(message);
//            }
//        });
//
        detail.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                detail_ = adapter_det.getItem(pos);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(name.getText())||TextUtils.isEmpty(phone.getText())||detail_ == null||detail_.equals("")){
                    Toast.makeText(mContext, "请填全信息哦~",Toast.LENGTH_SHORT).show();
                }
                else if(name.getText().toString().length()<2||name.getText().toString().length()>12){
                    alertBuilder = new AlertDialog.Builder(mContext);
                    alertBuilder.setTitle("提示");
                    alertBuilder.setMessage("收货人信息长度需在2~12个字符间");
                    alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertBuilder.show();
                }
                else if(phone.getText().toString().length()!=11){
                    Toast.makeText(mContext, "请输入11位手机号码",Toast.LENGTH_SHORT).show();
                }
                else {
                    name_ = name.getText().toString();
                    phone_ = phone.getText().toString();
                    Object_Adress new_add = new Object_Adress(name_,phone_,detail_,default_check);

                    List<Object_Adress> addressList = Cache.with(mContext)
                            .path(getCacheDir(mContext))
                            .getCache("AddressList", List.class);
                    if(addressList == null||addressList.size() <= 0){
                        addressList = new ArrayList<>();
                        new_add.setDefaut(true);
                        addressList.add(new_add);
                    }else {
                        if (default_check) {
                            for(int i =0;i < addressList.size();i++)
                                addressList.get(i).setDefaut(false);
                        }
                        addressList.add(new_add);
                    }
                    Cache.with(v.getContext())
                            .path(getCacheDir(v.getContext()))
                            .saveCache("AddressList", addressList);
                    finish();
                }

            }
        });
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
