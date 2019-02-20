package com.order.quickfurniture.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.order.quickfurniture.Activity.EditAddress;
import com.order.quickfurniture.Activity.GetAddress;
import com.order.quickfurniture.Activity.HomeActivity;
import com.order.quickfurniture.Pojo.AddRessList;
import com.order.quickfurniture.Pojo.Items;
import com.order.quickfurniture.Pojo.User;
import com.order.quickfurniture.R;
import com.order.quickfurniture.Util.CheckInternet;
import com.order.quickfurniture.Util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddressAdapter extends BaseAdapter {
    Holder holder,holder1,holder2,holder3,holder4;
    int count;

    Context context;
    ArrayList< AddRessList> ad_list;

    public AddressAdapter(GetAddress getAddress, ArrayList<AddRessList> addresList) {
        this.context=getAddress;
        this.ad_list=addresList;
    }

    @Override
    public int getCount() {
        return ad_list.size();
    }

    @Override
    public Object getItem(int i) {
        return ad_list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public class Holder{
        TextView tv_nm_mobile,tv_adres,tv_edit,tv_delete;
        ImageView tv_ed_de;
        RelativeLayout rel_select,rel_adress,profile_add_adress;
        EditText et_name,et_email,et_phone,et_state,et_land,et_adres,et_pin,et_secn;
        TextView tv_save,tv_cancel;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final AddRessList _pos = ad_list.get(i);
        holder = new Holder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.adres_list, viewGroup, false);
            holder.tv_ed_de = (ImageView) convertView.findViewById(R.id.tv_ed_de);
            holder.tv_nm_mobile = (TextView) convertView.findViewById(R.id.tv_nm_mobile);
            holder.tv_adres = (TextView) convertView.findViewById(R.id.tv_adres);
            holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
            holder.tv_edit = (TextView) convertView.findViewById(R.id.tv_edit);
            holder.rel_select = (RelativeLayout) convertView.findViewById(R.id.rel_select);
            holder.rel_adress = (RelativeLayout) convertView.findViewById(R.id.rel_adress);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.tv_ed_de.setTag(i);
        holder.tv_nm_mobile.setTag(i);
        holder.tv_adres.setTag(i);
        holder.tv_delete.setTag(i);
        holder.tv_edit.setTag(holder);
        holder.rel_select.setTag(i);
        holder.rel_adress.setTag(i);


        holder.tv_nm_mobile.setText(_pos.getFull_name() + "    " + _pos.getMobile());
        holder.tv_adres.setText(_pos.getAddress() + " , " + _pos.getLandmark() + " , " + _pos.getCity() + " , " + _pos.getPincode_id());

        holder.tv_ed_de.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               holder.rel_select.setVisibility(View.VISIBLE);


            }
        });

        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i=new Intent(context, EditAddress.class);
                i.putExtra("name",_pos.getFull_name());
                i.putExtra("id",_pos.getId());
                i.putExtra("email",_pos.getEmail());
                i.putExtra("phone",_pos.getMobile());
                i.putExtra("adres",_pos.getAddress());
                i.putExtra("land",_pos.getLandmark());
                i.putExtra("pin",_pos.getPincode_id());
                i.putExtra("state",_pos.getCity());
                context.startActivity(i);




            }
        });

        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callDelete(_pos.getId());
            }
        });

        holder.tv_nm_mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //int position=(Integer)view.getTag();

                // holder3=(Holder)view.getTag();
                holder.rel_select.setVisibility(View.GONE);

            }
        });holder.tv_adres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // int position=(Integer)view.getTag();

                // holder4=(Holder)view.getTag();
                holder.rel_select.setVisibility(View.GONE);

            }
        });

        return convertView;
    }

    private void callDelete(String id) {
        if(CheckInternet.getNetworkConnectivityStatus(context)){
            new DeleteAddares_asyn().execute(id);
        }
        else {
           Toast.makeText(context,"No Internate", Toast.LENGTH_LONG).show();
        }
    }

    private class DeleteAddares_asyn extends AsyncTask<String, Void, Void> {

        private static final String TAG = "DeleteAdress";
        int server_status;
        String server_message;
        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(context, "Loading Items", "Please wait...");
            }
        }

        @Override
        protected Void doInBackground(String... params) {


            try {
                String id = params[0];
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.DELETE_ADDRESS;
                URL url = new URL(link);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setAllowUserInteraction(false);
                conn.setInstanceFollowRedirects(true);
                conn.setRequestMethod("POST");

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("id", id);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                conn.connect();
                resCode = conn.getResponseCode();
                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = conn.getInputStream();
                }
                if (in == null) {
                    return null;
                }
                BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "", data = "";

                while ((data = reader.readLine()) != null) {
                    response += data + "\n";
                }

                Log.i(TAG, "Response : " + response);

                /*
                *
                * {
    "addresses": [
        {
            "id": 8,
            "user_id": 3,
            "full_name": "Avinash Pathak",
            "mobile": "5465456456",
            "pincode_id": 1,
            "address": "gfdgf",
            "locality": "fdgfd",
            "landmark": "gfdg",
            "city": "dfgfdg",
            "email": "avinash@yahoo.com",
            "is_default": 0,
            "is_trash": 0,
            "created": "2019-02-17T12:16:23+00:00",
            "modified": "2019-02-17T12:16:23+00:00",
            "pincode": {
                "id": 1,
                "pin": "751024",
                "charge": 12,
                "area": "fds fds fdsf sdf",
                "created": "2019-01-11T18:33:01+00:00",
                "modified": "2019-01-11T18:33:01+00:00"
            }
        }
    ]
}*/

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject _object = res.getJSONObject("res");
                    server_status = _object.optInt("status");
                    if (server_status == 1) {
                        server_message = "Deleted";

                    } else {
                        server_message = "Error While Creating Account";
                    }
                }
                return null;
            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
                server_message = "Network Error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void data) {
            super.onPostExecute(data);
            progressDialog.dismiss();
            if (server_status == 1) {
                Toast.makeText(context, server_message,Toast.LENGTH_SHORT).show();
                Intent i=new Intent(context, HomeActivity.class);
                context.startActivity(i);
                ((Activity)context).finish();

            } else {
                Toast.makeText(context, server_message,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
