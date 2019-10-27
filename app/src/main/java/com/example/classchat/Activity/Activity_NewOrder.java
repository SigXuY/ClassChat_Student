package com.example.classchat.Activity;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.classchat.Object.Object_Adress;
import com.example.classchat.Object.Object_Order;
import com.example.classchat.Object.Object_Pre_Sale;
import com.example.classchat.R;
import com.example.classchat.Util.DataHolder;
import com.example.library_cache.Cache;

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

public class Activity_NewOrder extends AppCompatActivity {

    private Object_Pre_Sale item;
    private Object_Adress address;
    private Object_Order new_order;

    private TextView name;
    private TextView phone;
    private TextView loctaion;

    private ImageView img;
    private TextView title;
    private TextView attribute;
    private TextView price_num;
    private TextView num;
    private TextView sum;
    private TextView num_;
    private TextView sum_;

    private Button buy;
    private ImageView back;
    private String format_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        //从缓存中读出地址
        List<Object_Adress> addressList = Cache.with(Activity_NewOrder.this)
                .path(getCacheDir(Activity_NewOrder.this))
                .getCache("AddressList", List.class);
        for (int i = 0;i < addressList.size(); i++) {
            if(addressList.get(i).getDefaut()) {
                address = addressList.get(i);
                break;
            }
        }

        item = (Object_Pre_Sale) getIntent().getSerializableExtra("item");

        initView();

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 库存验证

                //TODO 支付逻辑

                //支付完成执行：
                 getTime();
                String buyerid = DataHolder.getUserId();
                String orderid = buyerid + "/" + item.getItemId() + "/" +format_time;
                new_order = new Object_Order(orderid, buyerid, item, address,format_time);
                List<Object_Order> orderList = Cache.with(Activity_NewOrder.this)
                        .path(getCacheDir(Activity_NewOrder.this))
                        .getCache("orderList", List.class);
                if(orderList == null||orderList.size() <= 0){
                    orderList = new ArrayList<>();
                    orderList.add(new_order);
                }else {
                    orderList.add(new_order);
                }
                Cache.with(v.getContext())
                        .path(getCacheDir(v.getContext()))
                        .saveCache("orderList", orderList);
                //TODO 把order打包发url
                List<Object_Order> order_ = new ArrayList<>();
                order_.add(new_order);
                //TODO 切换界面
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              finish();
            }
        });
    }

    private void initView(){
        //商品图片，标题，属性（样式，颜色）
        img = findViewById(R.id.img_newOrder);
        name = findViewById(R.id.name_newOrder);
        attribute = findViewById(R.id.attribute_newOrder);
        //价格×数量  格式setText（￥129.00\n×10）
        price_num = findViewById(R.id.price_num_newOrder) ;
        //数量        格式setText（共10件 合计：）
        num = findViewById(R.id.num_newOrder) ;
        num_ = findViewById(R.id.num_newOrder_) ;
        //总价  格式setText(￥1290.00)
        sum = findViewById(R.id.price_newOrder) ;
        sum_ = findViewById(R.id.price_newOrder_) ;
        buy = findViewById(R.id.buy_newOrder);
        back = findViewById(R.id.back_newOrder);
        phone = findViewById(R.id.phone_newOrder);
        loctaion = findViewById(R.id.location_newOrder);
        title = findViewById(R.id.title_newOrder);

        Glide.with(Activity_NewOrder.this).load(item.getImgurl())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        // 加载失败后的逻辑处理
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        // 图片加载完成后的逻辑处理
                        return false;
                    }
                }).error(R.mipmap.ic_launcher_round)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(70, 70)
                .into(img);
        title.setText(item.getItemName());
        attribute.setText(item.getParam());
        price_num.setText("￥"+ new DecimalFormat("#0.00").format(item.getPrice()) +"\n×"+item.getNum());
        num.setText("共"+ item.getNum() +"件  合计：");
        sum.setText("￥"+ new DecimalFormat("#0.00").format(item.getPrice()*item.getNum()));
        num_.setText("共"+ item.getNum() +"件商品  合计：");
        sum_.setText("￥"+ new DecimalFormat("#0.00").format(item.getPrice()*item.getNum()));
        name.setText(address.getName());
        phone.setText(address.getPhone());
        loctaion.setText(address.totalAddrss());
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
////        List<Object_Pre_Sale> objs = new ArrayList<>();
////        objs.add(item);
////
////    }
}
