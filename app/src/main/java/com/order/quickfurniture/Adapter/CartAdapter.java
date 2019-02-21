package com.order.quickfurniture.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.order.quickfurniture.Activity.CartActivity;
import com.order.quickfurniture.Pojo.Cartlist;
import com.order.quickfurniture.Pojo.PincodeList;
import com.order.quickfurniture.R;

import java.util.ArrayList;

public class CartAdapter extends BaseAdapter {

    Context _context;
    ArrayList<Cartlist> c_list;
    Holder holder;

    public CartAdapter(CartActivity cartActivity, ArrayList<Cartlist> cList) {
        this._context=cartActivity;
        this.c_list=cList;
    }

    @Override
    public int getCount() {
        return c_list.size();
    }

    @Override
    public Object getItem(int i) {
        return c_list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class Holder{
        TextView it_name;
        ImageView split_minus,split_plus;
        EditText et_price;

    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Cartlist _pos = c_list.get(i);
        holder = new Holder();
        if (view == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.activity_cart, viewGroup, false);
           holder. it_name=(TextView)view.findViewById(R.id.it_name);
            holder.et_price=(EditText) view.findViewById(R.id.et_price);
            holder.split_minus=(ImageView)view.findViewById(R.id.minus);
            holder.split_plus=(ImageView)view.findViewById(R.id.plus);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.it_name.setTag(i);
        holder.et_price.setTag(i);
        holder.split_minus.setTag(i);
        holder.split_plus.setTag(i);


        holder.it_name.setText(" Pincode : "+_pos.getPrice());
        holder.split_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double  qnty=Double.valueOf(holder.et_price.getText().toString().trim());
                if(qnty==1){
                    qnty=qnty+1;
                }
                else {
                    qnty=qnty+1;
                }
                holder.et_price.setText(String.valueOf(qnty));

                // qnty=qnty+1;
                //split_qty.setText(String.valueOf(qnty));
            }
        });
        holder.split_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Double qnty = Double.valueOf(holder.et_price.getText().toString().trim());

                if(qnty==2 || qnty==2.0){
                    qnty=qnty-1;
                }
                else if(qnty==1){
                }
                else {
                    qnty=qnty-1;
                }

                holder.et_price.setText(String.valueOf(qnty));

                // split_qty.setText(String.valueOf(qnty));
            }
        });


        return view;
    }
}