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

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final AddRessList _pos = ad_list.get(position);
        holder = new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.coadres_list, parent, false);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv_ed_de.setTag(position);
        return convertView;
    }
}
