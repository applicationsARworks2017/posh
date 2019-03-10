package com.order.quickfurniture.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.order.quickfurniture.Pojo.AddRessList;
import com.order.quickfurniture.Pojo.MyOrders;
import com.order.quickfurniture.R;

import java.util.ArrayList;

public class MyOrdersAdapter extends BaseAdapter {

    ArrayList<MyOrders> arrayList;
    Context _context;
    Holder holder;

    public MyOrdersAdapter(FragmentActivity activity, ArrayList<MyOrders> arrayList) {
        this._context = activity;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class  Holder{
        TextView order_num,items,price,tax,delivery,total,status;
        Button cancel;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final MyOrders _pos = arrayList.get(position);
        holder = new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.order_list, parent, false);
            holder.order_num = (TextView) convertView.findViewById(R.id.order_number);
            holder.items = (TextView) convertView.findViewById(R.id.item_number);
            holder.total = (TextView) convertView.findViewById(R.id._amount);
            holder.status = (TextView) convertView.findViewById(R.id._status);
            holder.cancel = (Button) convertView.findViewById(R.id.order_cancel);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.order_num.setTag(position);
        holder.items.setTag(position);
        holder.total.setTag(position);
        holder.status.setTag(position);
        holder.cancel.setTag(position);


        holder.total.setText("Rs. "+_pos.getTotal());
        holder.status.setText("Rs. "+_pos.getStatus());
        return convertView;

    }
}
