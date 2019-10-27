package com.example.classchat.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.example.classchat.Object.Object_Adress;
import com.example.classchat.R;
import com.example.library_cache.Cache;

import java.util.List;


public class Activity_ModifyAddress extends AppCompatActivity {

    private Object_Adress formal_address;
    private Context mContext;
    private AlertDialog.Builder alertBuilder;

    private ImageView back;
    private Button save;
    private RelativeLayout delete;
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

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    adapter_det = new ArrayAdapter(mContext, R.layout.item_auth,det.toJavaList(String.class));
                    detail.setAdapter(adapter_det);
                    int size = adapter_det.getCount();
                    for (int i = 0; i < size; i++) {
                        if (TextUtils.equals(detail_, adapter_det.getItem(i).toString())) {
                            detail.setSelection(i,true);
                            break;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_address);

        mContext = Activity_ModifyAddress.this;

        initView();

        switch_default.setChecked(default_check);
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
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Object_Adress> aList =  Cache.with(mContext)
                        .path(getCacheDir(mContext))
                        .getCache("AddressList", List.class);
                for(int i = 0; i < aList.size(); i++){
                    if(aList.get(i).getId() == formal_address.getId()){
                        aList.remove(i);
                    }
                }
                Cache.with(v.getContext())
                        .path(getCacheDir(v.getContext()))
                        .saveCache("AddressList", aList);
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(name.getText())||TextUtils.isEmpty(phone.getText())||detail_ == ""||detail_.equals("")){
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

                    List<Object_Adress> aList =  Cache.with(mContext)
                            .path(getCacheDir(mContext))
                            .getCache("AddressList", List.class);
                    for(int i = 0; i < aList.size(); i++){
                        if(aList.get(i).getId() == formal_address.getId()){
                            aList.remove(i);
                        }
                    }

                    if(default_check){
                        for(int i = 0; i < aList.size(); i++)
                            aList.get(i).setDefaut(false);
                    }

                    aList.add(new_add);

                    Cache.with(v.getContext())
                            .path(getCacheDir(v.getContext()))
                            .saveCache("AddressList", aList);
                    finish();
                }

            }
        });
    }

    private void initView(){
        back = findViewById(R.id.back_modify_address);
        save = findViewById(R.id.modify_address_save);
        delete = findViewById(R.id.delete_address);
        name = findViewById(R.id.modify_address_name);
        detail = findViewById(R.id.modify_address_detail);
        switch_default = findViewById(R.id.modify_address_defaut_check);
        phone = findViewById(R.id.modify_address_phone);
        formal_address = (Object_Adress) getIntent().getSerializableExtra("address");
        name.setText(formal_address.getName());
        phone.setText(formal_address.getPhone());
        detail_ = formal_address.getDetail();
        default_check = formal_address.getDefaut();

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
