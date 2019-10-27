package com.example.classchat.Activity;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.classchat.Adapter.Adapter_PresaleList;
import com.example.classchat.Object.Object_Adress;
import com.example.classchat.Object.Object_Order;
import com.example.classchat.Object.Object_Pre_Sale;
import com.example.classchat.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.example.classchat.R;
import com.example.classchat.Util.DataHolder;
import com.example.library_cache.Cache;

public class Activity_NewOrderGroup extends AppCompatActivity {
    private List<Object_Pre_Sale> items;
    private List<Object_Order>orders;
    private Object_Adress address;
    private ListView itemList;
    private TextView name;
    private TextView phone;
    private TextView loctaion;
    private TextView num;
    private TextView sum;
    private Button buy;
    private Button back;
    private Context mContext;
    private String format_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__new_order_group);
        mContext = Activity_NewOrderGroup.this;

        //从缓存中读出地址
        List<Object_Adress> addressList = Cache.with(Activity_NewOrderGroup.this)
                .path(getCacheDir(Activity_NewOrderGroup.this))
                .getCache("AddressList", List.class);
        for (int i = 0;i < addressList.size(); i++) {
            if(addressList.get(i).getDefaut()) {
                address = addressList.get(i);
                break;
            }
        }

        items = (List<Object_Pre_Sale>) getIntent().getSerializableExtra("itemList");

        initView();

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 判断库存


                //TODO 支付逻辑
                //支付完成执行：
                getTime();
                String buyerid = DataHolder.getUserId();
                List<Object_Order> orderList = Cache.with(Activity_NewOrderGroup.this)
                        .path(getCacheDir(Activity_NewOrderGroup.this))
                        .getCache("orderList", List.class);
                if(orderList == null||orderList.size() <= 0){
                    orderList = new ArrayList<>();
                }
                for(int i = 0; i < items.size();i++) {
                    String orderid = buyerid + "/" + items.get(i).getItemId() + "/" +format_time;
                    Object_Order order = new Object_Order(orderid, buyerid, items.get(i), address, format_time);
                    orderList.add(order);
                    orders.add(order);
                }
                Cache.with(v.getContext())
                        .path(getCacheDir(v.getContext()))
                        .saveCache("orderList", orderList);

                //TODO 把orders打包发url
                //TODO 切换界面
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();            }
        });
    }
    private void initView(){

        itemList = findViewById(R.id.list_preSale);
        name = findViewById(R.id.name_newOrder_group);
        //数量        格式setText（共10件 合计：）
        num = findViewById(R.id.numOfitem_newOrder_group) ;
        //总价  格式setText(￥1290.00)
        sum = findViewById(R.id.price_newOrder_group) ;
        buy = findViewById(R.id.buy_newOrder_group);
        back = findViewById(R.id.back_newOrder_group);
        phone = findViewById(R.id.phone_newOrder_group);
        loctaion = findViewById(R.id.location_newOrder_group);

        num.setText("结算"+ items.size() +"单  合计：");
        float sumprice = 0;
        for(int i = 0; i < items.size(); i++){
            sumprice += items.get(i).getPrice()*items.get(i).getNum();
        }
        sum.setText("￥"+ new DecimalFormat("#0.00").format(sumprice));
        name.setText(address.getName());
        phone.setText(address.getPhone());
        loctaion.setText(address.getDetail());
        itemList.setAdapter(new Adapter_PresaleList(mContext,items));
    }

    private void getTime(){
        URL url = null;//取得资源对象

        try {
            url = new URL("http://www.baidu.com");
            //url = new URL("http://www.ntsc.ac.cn");//中国科学院国家授时中心
            //url = new URL("http://www.bjtime.cn");
            URLConnection uc = url.openConnection();//生成连接对象
            uc.connect(); //发出连接
            long ld = uc.getDate(); //取得网站日期时间
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ld);
            format_time = formatter.format(calendar.getTime());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
            return ;
        } catch (Exception e) {
            e.printStackTrace();
        }

        //若请求网络时间失败，返回系统时间
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        format_time = formatter.format(currentTime);

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

////    TODO 验证库存函数
////    private boolean checkednum(){
////        items.....
////
////    }
}
