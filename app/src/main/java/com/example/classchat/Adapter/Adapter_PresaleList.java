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
import com.example.classchat.Object.Object_Pre_Sale;
import com.example.classchat.Object.Object_TodoList;
import com.example.classchat.R;


import java.text.DecimalFormat;
import java.util.List;

public class Adapter_PresaleList extends BaseAdapter {
    LayoutInflater inflater;
    List<Object_Pre_Sale> ls;
    Context mContext;


    public Adapter_PresaleList(Context context, List<Object_Pre_Sale> objects){
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
        final Object_Pre_Sale item=ls.get(position);
        View v=inflater.inflate(R.layout.item_presale_list,null);
        //商品图片，标题，属性（样式，颜色）
        ImageView img = (ImageView)v.findViewById(R.id.img_preSale) ;
        TextView title = (TextView)v.findViewById(R.id.title_preSale) ;
        TextView attribute= (TextView) v.findViewById(R.id.attribute_preSale);

        //价格×数量  格式setText（￥129.00\n×10）
        TextView price_num = (TextView)v.findViewById(R.id.price_num_preSale) ;
        //数量        格式setText（共10件商品  合计：）
        TextView num = (TextView)v.findViewById(R.id.num_preSale) ;
        //总价  格式setText(￥1290.00)
        TextView sum = (TextView)v.findViewById(R.id.price_preSale) ;

        Glide.with(mContext).load(item.getImgurl())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        // 加载失败后的逻辑处理
                        Toast.makeText(mContext, "图片加载失败", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        // 图片加载完成后的逻辑处理
                        Toast.makeText(mContext, "图片加载成功", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }).error(R.mipmap.ic_launcher_round)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(img);
        title.setText(item.getItemName());
        attribute.setText(item.getParam());
        price_num.setText("￥"+ new DecimalFormat("#0.00").format(item.getPrice()) +"\n×"+item.getNum());
        num.setText("共"+ item.getNum() +"件商品  合计：");
        sum.setText("￥"+ new DecimalFormat("#0.00").format(item.getPrice()*item.getNum()));

        return v;
    }
}