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
import android.widget.Toast;

import com.example.classchat.Adapter.Adapter_OrderList;
import com.example.classchat.Object.Object_Adress;
import com.example.classchat.Object.Object_Order;
import com.example.classchat.Object.Object_Pre_Sale;
import com.example.classchat.R;
import com.example.library_cache.Cache;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Activity_OrderList extends AppCompatActivity {

    private List<Object_Order> orders = new ArrayList<>();
    private ListView orderList;
    private TextView hint;
    private ImageView back;
    private Context mContext;
    private String format_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        mContext = Activity_OrderList.this;
        orderList = findViewById(R.id.orderList);
        hint = findViewById(R.id.hint_orderList);
        back = findViewById(R.id.back_orderList);
        getOrderList();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 切换界面
            }
        });
    }


    private void getOrderList(){

        orders =  Cache.with(mContext)
                .path(getCacheDir(mContext))
                .getCache("orderList", List.class);
        if(orders == null || orders.size() <= 0){
            //TODO 网络请求orderlist
            orders = new ArrayList<>();
        }
        orderList.setAdapter(new Adapter_OrderList(mContext, orders));
    }
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

//    private void testData(){
//        //从缓存中读出地址
//        Object_Adress address = null;
//        String order_id = null;
//        //购买人id
//        String buyer_id = null;
//        getTime();
//        String generatetime = format_time;
//        String item_title = "一双非常nice的好鞋呀";
//        String item_id = "4008823823";
//        List<String> paramList = new ArrayList<>();
//        paramList.add("45超大码");
//        paramList.add("那个不痛夜夜轻松");
//        paramList.add("灰指甲用亮甲");
//
//        List<Object_Adress> addressList = Cache.with(Activity_OrderList.this)
//                .path(getCacheDir(Activity_OrderList.this))
//                .getCache("AddressList", List.class);
//        for (int i = 0;i < addressList.size(); i++) {
//            if(addressList.get(i).getDefaut()) {
//                address = addressList.get(i);
//                break;
//            }
//        }
//        for(int i = 0 ; i < 2; i++){
//            int num = (i+1)*2;
//            float price = (float) Math.sqrt(num);
//            Object_Pre_Sale item = new Object_Pre_Sale(item_title,item_id,paramList,num,price,"http://106.12.105.160/authentation/18344509682_card.jpg");
//            orders.add(new Object_Order(order_id,buyer_id,item,address,generatetime));
//        }
//        orderList.setAdapter(new Adapter_OrderList(mContext, orders));
//    }

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
}
