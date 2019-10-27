package com.example.classchat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.classchat.Activity.Activity_SearchAddCourse;
import com.example.classchat.Object.MySubject;
import com.example.classchat.Object.Object_Order;
import com.example.classchat.Object.Object_TodoList;
import com.example.classchat.R;


import java.text.DecimalFormat;
import java.util.List;

public class Adapter_OrderList extends BaseAdapter {
    LayoutInflater inflater;
    List<Object_Order> ls;
    Context mContext;


    public Adapter_OrderList(Context context, List<Object_Order> objects){
        mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.ls = objects;

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
        final Object_Order item=ls.get(position);
        View v=inflater.inflate(R.layout.item_orderlist,null);
        //商品图片，标题，属性（样式，颜色）
        ImageView img = v.findViewById(R.id.img_orderList);
        TextView title = v.findViewById(R.id.title_orderList);
        TextView attribute= v.findViewById(R.id.attribute_orderList);

        //价格×数量  格式setText（￥129.00\n×10）
        TextView price_num = v.findViewById(R.id.price_num_orderList) ;
        //数量        格式setText（共10件商品  合计：）
        TextView num = v.findViewById(R.id.num_orderList) ;
        //总价  格式setText(￥1290.00)
        TextView sum = v.findViewById(R.id.price_orderList) ;

        Glide.with(mContext).load(item.getItem().getImgurl())
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
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(70, 70)
                .into(img);
        title.setText(item.getItem().getItemName());
        attribute.setText(item.getItem().getParam());
        price_num.setText("￥"+ new DecimalFormat("#0.00").format(item.getItem().getPrice()) +"\n×"+item.getItem().getNum());
        num.setText("共"+ item.getItem().getNum() +"件商品  合计：");
        sum.setText("￥"+ new DecimalFormat("#0.00").format(item.getSumprice()));

        //状态完成提示：已评价/成功退货退款/已取消订单
        TextView hint = v.findViewById(R.id.order_done_message);
        //TODO 根据状态位改变hint
        switch(item.getState()){
            default :
                break;
        }
        //TODO 两个按钮事件
        //取消订单，退货退款，评价
        Button drawback = v.findViewById(R.id.order_drawback);
        Button evaluate = v.findViewById(R.id.order_evaluate);
        drawback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return v;
    }
}