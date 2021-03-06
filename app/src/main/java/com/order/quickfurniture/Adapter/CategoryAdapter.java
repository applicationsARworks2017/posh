package com.order.quickfurniture.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.order.quickfurniture.Pojo.Category;
import com.order.quickfurniture.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by Amaresh on 11/3/17.
 */

public class CategoryAdapter extends BaseAdapter {
    Context _context;
    ArrayList<Category> categoryLists;
    Holder holder;
    String lang;
    public CategoryAdapter(Activity activity, ArrayList<Category> cList) {
        this._context=activity;
        this.categoryLists=cList;
        this.lang=lang;
    }



    @Override
    public int getCount() {
        return categoryLists.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder{
        TextView cat_name;
        ImageView iv_letterView;
        LinearLayout lin_hr;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Category _pos=categoryLists.get(position);
        holder=new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.cat_list, parent, false);
            holder.cat_name=(TextView)convertView.findViewById(R.id.c_name);
            holder.iv_letterView=(ImageView)convertView.findViewById(R.id.iv_letterView);
            holder.lin_hr=(LinearLayout)convertView.findViewById(R.id.lin_hr);
            convertView.setTag(holder);
        }
        else{
            holder = (Holder) convertView.getTag();
        }
        holder.iv_letterView.setTag(position);
        holder.lin_hr.setTag(position);
        holder.cat_name.setTag(position);
        holder.cat_name.setText(_pos.getTitle());
        if(!_pos.getPhoto().isEmpty()) {
            Picasso.with(_context).load(_pos.getPhoto()).into(holder.iv_letterView);
        }
        return convertView;
    }
}
