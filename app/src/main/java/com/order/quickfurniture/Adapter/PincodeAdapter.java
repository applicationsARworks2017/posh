package com.order.quickfurniture.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.order.quickfurniture.Activity.EditAddress;
import com.order.quickfurniture.Activity.Getpincode;
import com.order.quickfurniture.Pojo.AddRessList;
import com.order.quickfurniture.Pojo.PincodeList;
import com.order.quickfurniture.R;
import com.order.quickfurniture.Util.Constants;

import java.util.ArrayList;

public class PincodeAdapter extends BaseAdapter {

    Holder holder;
    Context _context;
    ArrayList<PincodeList> pin_l;
    public PincodeAdapter(Getpincode getpincode, ArrayList<PincodeList> pinlist) {
       this. _context=getpincode;
        this.pin_l=pinlist;
    }

    @Override
    public int getCount() {
        return pin_l.size();
    }

    @Override
    public Object getItem(int i) {
        return pin_l.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class Holder{
        TextView tv_pin;

    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final PincodeList _pos = pin_l.get(i);
        holder = new Holder();
        if (view == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.pin_list, viewGroup, false);

            holder.tv_pin = (TextView) view.findViewById(R.id.tv_pin);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.tv_pin.setTag(i);


        holder.tv_pin.setText(" Pincode : "+_pos.getPin());


        return view;
    }
}
