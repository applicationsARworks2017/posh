package com.order.quickfurniture.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.order.quickfurniture.Activity.CartActivity;
import com.order.quickfurniture.Pojo.Cartlist;
import com.order.quickfurniture.R;
import com.order.quickfurniture.Util.APIManager;
import com.order.quickfurniture.Util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewCartAdapter extends BaseAdapter {
    Context _context;
    ArrayList<Cartlist> c_list;
    Holder holder,vHolder;
    public NewCartAdapter(CartActivity cartActivity, ArrayList<Cartlist> cList) {
        this._context=cartActivity;
        this.c_list=cList;
    }

    @Override
    public int getCount() {
        return c_list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder{
        TextView it_name,tv_price;
        ImageView split_minus,split_plus,it_image,it_remove;
        EditText et_qnty;

    }
    @Override
    public View getView(int i, View view, ViewGroup parent) {
        final Cartlist _pos = c_list.get(i);
        holder = new Holder();
        if (view == null) {
            LayoutInflater mInflater = (LayoutInflater) _context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.activity_cart,parent, false);
            holder. it_name=(TextView)view.findViewById(R.id.it_name);
            holder. tv_price=(TextView)view.findViewById(R.id.tv_price);
            holder.et_qnty=(EditText) view.findViewById(R.id.et_qnty);
            holder.split_minus=(ImageView)view.findViewById(R.id.minus);
            holder.split_plus=(ImageView)view.findViewById(R.id.plus);
            holder.it_image=(ImageView)view.findViewById(R.id.it_image);
            holder.it_remove=(ImageView)view.findViewById(R.id.it_remove);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        holder.it_name.setTag(i);
        holder.tv_price.setTag(i);
        holder.et_qnty.setTag(holder);
        holder.it_remove.setTag(holder);
        holder.split_minus.setTag(holder);
        holder.split_plus.setTag(holder);
        holder.it_image.setTag(i);




        holder.it_name.setText(_pos.getItem_name());
        holder.tv_price.setText(" RS : "+_pos.getPrice());
        holder.split_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vHolder = (Holder)view.getTag();
                Double  qnty=Double.valueOf(vHolder.et_qnty.getText().toString().trim());
                if(qnty==1){
                    qnty=qnty+1;
                }
                else {
                    qnty=qnty+1;
                }
                vHolder.et_qnty.setText(String.valueOf(qnty));

                // qnty=qnty+1;
                //split_qty.setText(String.valueOf(qnty));
            }
        });
        holder.split_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vHolder = (Holder)view.getTag();

                Double qnty = Double.valueOf(vHolder.et_qnty.getText().toString().trim());

                if(qnty==1){
                }
                else {
                    qnty=qnty-1;
                }

                vHolder.et_qnty.setText(String.valueOf(qnty));

                // split_qty.setText(String.valueOf(qnty));
            }
        });
        holder.it_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cart_id = _pos.getC_id();
                delete(cart_id);
            }
        });

        if(_pos.getPhoto().contains(",")) {
            List<String> imageList = Arrays.asList(_pos.getPhoto().split(","));

            if (imageList.get(0) == null || imageList.get(0) == "" || imageList.get(0).contentEquals("")) {
                holder.it_image.setImageResource(R.drawable.error);

            } else {
                Picasso.with(_context).load(imageList.get(0)).into(holder.it_image);
            }
        }


        return view;
    }


    private void delete(String cart_id) {
        final ProgressDialog pd = new ProgressDialog(_context);
        pd.setMessage("Deleting Item. Please wait...");
        pd.show();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id",cart_id );

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIManager().ModifyAPI(Constants.MAINURL+Constants.CART_DELETE,"res",jsonObject, _context,
                new APIManager.APIManagerInterface() {
                    @Override
                    public void onSuccess(Object resultObj) {
                        Toast.makeText(_context,"Item Deleted", Toast.LENGTH_LONG).show();
                        pd.dismiss();
                        //new CartActivity().getCartList();
                    }

                    @Override
                    public void onError(String error) {

                        Toast.makeText(_context,error.toString(), Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                });
    }
}
