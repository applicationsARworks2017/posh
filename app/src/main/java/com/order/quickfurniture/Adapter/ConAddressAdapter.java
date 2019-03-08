package com.order.quickfurniture.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.order.quickfurniture.Activity.ConfirmAddress;
import com.order.quickfurniture.Pojo.AddRessList;
import com.order.quickfurniture.R;

import java.util.ArrayList;

public class ConAddressAdapter extends BaseAdapter {
    Context context;
    Holder holder;
    ArrayList< AddRessList> ad_list;
    public ConAddressAdapter(ConfirmAddress confirmAddress, ArrayList<AddRessList> addresList) {
        this.context=confirmAddress;
        this.ad_list=addresList;
    }

    @Override
    public int getCount() {
        return ad_list.size();
    }

    @Override
    public Object getItem(int position) {
        return ad_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class Holder{
        TextView c_name,c_mobile,c_landmark,c_locality,c_address,c_pin;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AddRessList _pos = ad_list.get(position);
        holder = new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.coadres_list, parent, false);
            holder.c_name=convertView.findViewById(R.id.c_name);
            holder.c_mobile=convertView.findViewById(R.id.c_mobile);
            holder.c_landmark=convertView.findViewById(R.id.c_landmark);
            holder.c_locality=convertView.findViewById(R.id.c_locality);
            holder.c_address=convertView.findViewById(R.id.c_address);
            holder.c_pin=convertView.findViewById(R.id.c_pin);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.c_name.setTag(position);
        holder.c_mobile.setTag(position);
        holder.c_landmark.setTag(position);
        holder.c_locality.setTag(position);
        holder.c_address.setTag(position);
        holder.c_pin.setTag(position);

        holder.c_name.setText("Name: "+_pos.getFull_name());
        holder.c_mobile.setText("Mobile: "+_pos.getMobile());
        holder.c_landmark.setText("Landmark: "+_pos.getLandmark());
        holder.c_locality.setText("Locality: "+_pos.getLocality());
        holder.c_address.setText("Address: "+_pos.getAddress());
        holder.c_pin.setText("City: "+_pos.getCity()+ " - "+_pos.getPincode());


        return convertView;
    }
}
