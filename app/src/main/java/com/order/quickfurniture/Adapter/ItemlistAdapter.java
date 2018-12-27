package com.order.quickfurniture.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.order.quickfurniture.Activity.ItemList;
import com.order.quickfurniture.Pojo.Items;
import com.order.quickfurniture.Pojo.SubCategory;
import com.order.quickfurniture.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemlistAdapter extends BaseAdapter {
    ArrayList<Items> list;
    Context _context;
    Holder holder;
    public ItemlistAdapter(Activity activity, ArrayList<Items> itemsArrayList) {
        this._context=activity;
        this.list=itemsArrayList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class Holder{
        TextView item_name,cost;
        ImageView item_image;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final Items _pos=list.get(i);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.item_list, viewGroup, false);
            holder.item_name=(TextView)convertView.findViewById(R.id.item_name);
            holder.cost=(TextView)convertView.findViewById(R.id.cost);
            holder.item_image=(ImageView) convertView.findViewById(R.id.item_image);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.item_image.setTag(i);
        holder.cost.setTag(i);
        holder.item_name.setTag(i);

        holder.item_name.setText(_pos.getName());
        holder.cost.setText("\u20B9 "+_pos.getActual_price());

        if(_pos.getPhoto().contains(",")) {
            List<String> imageList = Arrays.asList(_pos.getPhoto().split(","));
            if(imageList.get(1)== null || imageList.get(1) == "" || imageList.get(1).contentEquals("") ){
                holder.item_image.setImageResource(R.drawable.error);

            }
            else{
                Picasso.with(_context)
                        .load(imageList.get(1)).into(holder.item_image);
            }
        }


        return convertView;
    }
}
