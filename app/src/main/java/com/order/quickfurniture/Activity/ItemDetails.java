package com.order.quickfurniture.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.order.quickfurniture.Pojo.PincodeList;
import com.order.quickfurniture.Pojo.User;
import com.order.quickfurniture.R;
import com.order.quickfurniture.Util.CheckInternet;
import com.order.quickfurniture.Util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
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

import static com.order.quickfurniture.Util.Constants.ITEM_DETAILS;

public class ItemDetails extends AppCompatActivity {
    Toolbar detail_tool;
    String _id, _name,_price;
    FloatingActionButton gotocart;
    String server_message;
    int server_status;
    ImageView item_image,img1,img2,img3,img4,im_close;
    String category_id, sub_category_id, item_type_id, name, price, discount, actual_price, image, description;
    TextView tv_desc,tv_productnm,tv_price,tv_shipng_charge,tv_area,tv_aded_item_name;
    Button et_check_pincode;
    Dialog dialog,dialog1;
    ArrayList<PincodeList> pinlist;
    RelativeLayout rel_pin,rel_add,relative_addcart;
    LinearLayout lin_pin;
    Button btn_change,goto_cart,btn_ctn;
    String ship_charge,area,pin,user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            _id = extras.getString("_ID");
            _name = extras.getString("_NAME");
            _price = extras.getString("_PRICE");
            // and get whatever type user account id is
        }
        user_id =getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);

        item_image = (ImageView)findViewById(R.id.item_image);
        img1 = (ImageView)findViewById(R.id.img1);
        img2 = (ImageView)findViewById(R.id.img2);
        img3 = (ImageView)findViewById(R.id.img3);
        img4 = (ImageView)findViewById(R.id.img4);
        im_close = (ImageView)findViewById(R.id.im_close);
        tv_desc =(TextView)findViewById(R.id.tv_desc);
        tv_productnm =(TextView)findViewById(R.id.tv_productnm);
        tv_price =(TextView)findViewById(R.id.tv_price);
        tv_shipng_charge =(TextView)findViewById(R.id.tv_shipng_charge);
        tv_area =(TextView)findViewById(R.id.tv_pinarea);
        tv_aded_item_name =(TextView)findViewById(R.id.tv_aded_item_name);
        btn_change =(Button)findViewById(R.id.btn_change);
        et_check_pincode =(Button) findViewById(R.id.et_check_pincode);
        rel_pin =(RelativeLayout) findViewById(R.id.rel_pin);
        rel_add =(RelativeLayout) findViewById(R.id.rel_add);
        relative_addcart =(RelativeLayout) findViewById(R.id.relative_addcart);
        lin_pin =(LinearLayout) findViewById(R.id.lin_pin);

        detail_tool = (Toolbar) findViewById(R.id.detail_tool);
        detail_tool.setTitleTextColor(Color.WHITE);
        detail_tool.setNavigationIcon(R.mipmap.baseline_arrow_back_white_24);
        detail_tool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        detail_tool.setTitle(_name);
        gotocart = (FloatingActionButton) findViewById(R.id.gotocart);
        btn_ctn=(Button)findViewById(R.id.btn_cont_shpng);
        goto_cart=(Button)findViewById(R.id.btn_goto_cart);
        btn_ctn.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checkServer(pin);
               /* Intent i=new Intent(ItemDetails.this,HomeActivity.class);
                startActivity(i);*/
               finish();


            }
        });
        gotocart.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user_id==""||user_id==null){
                    Intent i=new Intent(ItemDetails.this,LoginActivity.class);
                    startActivity(i);
                }
                else{
                    checkTocart(user_id,_id,_price);

                }

            }
        });
        goto_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ItemDetails.this, CartActivity.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
        im_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relative_addcart.setVisibility(View.GONE);
            }
        });


        et_check_pincode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog=new Dialog(ItemDetails.this);
                dialog.setContentView(R.layout.check_pincode_dialog);
                final EditText et_pin=(EditText)dialog.findViewById(R.id.et_new_ch_pincode);
                Button save=(Button)dialog.findViewById(R.id.btn_submit);
                save.setOnClickListener(new  View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String pin=et_pin.getText().toString().trim();
                        checkServer(pin);

                    }
                });
                dialog.show();
            }
        });btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog=new Dialog(ItemDetails.this);
                dialog.setContentView(R.layout.check_pincode_dialog);
                final EditText et_pin=(EditText)dialog.findViewById(R.id.et_new_ch_pincode);
                Button save=(Button)dialog.findViewById(R.id.btn_submit);
                save.setOnClickListener(new  View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String pin=et_pin.getText().toString().trim();
                        checkServer(pin);

                    }
                });
                dialog.show();
            }
        });
        if (CheckInternet.getNetworkConnectivityStatus(ItemDetails.this)) {
            getItemdetails(_id);
        } else {
            Constants.NointernetDialog(ItemDetails.this);
        }
    }

    private void checkTocart(String user_id, String id, String price) {
        if (CheckInternet.getNetworkConnectivityStatus(ItemDetails.this)) {
            new addCartdetails().execute(user_id,id,price);
        } else {
            Constants.NointernetDialog(ItemDetails.this);
        }
    }

    private void checkServer(String pin) {
        if (CheckInternet.getNetworkConnectivityStatus(ItemDetails.this)) {
            new getPindetails().execute(pin);
        } else {
            Constants.NointernetDialog(ItemDetails.this);
        }
    }

    private void getItemdetails(String id) {

        final ProgressDialog pd = new ProgressDialog(ItemDetails.this);
        pd.setTitle("Product Details");
        pd.setMessage("Please wait...");
        pd.show();
        RequestQueue requestQueue;
        String url = Constants.MAINURL + ITEM_DETAILS;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(ItemDetails.this);

        JsonObjectRequest mJsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response != null && response.length() > 0) {
                                JSONObject item_object = response.getJSONObject("item");
                                server_status = 1;
                                server_message = "value found";
                                category_id = item_object.optString("category_id");
                                name = item_object.optString("name");
                                sub_category_id = item_object.optString("sub_category_id");
                                item_type_id = item_object.optString("item_type_id");
                                price = item_object.optString("price");
                                discount = item_object.optString("discount");
                                actual_price = item_object.optString("actual_price");
                                image = item_object.optString("image");
                                description = item_object.optString("description");
                                //if(server_status ==1){
                                    setAllValues();


                            } else {
                                server_status = 0;
                                server_message = "Response Not Found";
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            server_message = e.toString();

                        } catch (Exception e) {
                            server_message = e.toString();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                server_message = error.toString();

                // error defining
                /*if (listener != null) {
                    listener.onError(error.toString());
                }*/
            }
        });

        requestQueue.add(mJsonObjectRequest);
        mJsonObjectRequest.setShouldCache(false);
        mJsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        pd.dismiss();


    }

    private void setAllValues() {
        if(image.contains(",")) {
            List<String> imageList = Arrays.asList(image.split(","));
            
            if(imageList.get(0)== null || imageList.get(0) == "" || imageList.get(0).contentEquals("") ){
                item_image.setImageResource(R.drawable.error);

            }
            else {
                Picasso.with(getApplicationContext()).load(imageList.get(0)).into(item_image);
            }
            if(imageList.get(0)== null || imageList.get(0) == "" || imageList.get(0).contentEquals("") ){
                img1.setImageResource(R.drawable.error);

            }
            else {
                Picasso.with(getApplicationContext()).load(imageList.get(0)).into(img1);
                final String img= imageList.get(0);

                img1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Picasso.with(getApplicationContext()).load(img).into(item_image);

                    }
                });
            }
            if(imageList.get(1)== null || imageList.get(1) == "" || imageList.get(1).contentEquals("") ){
                img2.setImageResource(R.drawable.error);

            }
            else {
                Picasso.with(getApplicationContext()).load(imageList.get(1)).into(img2);
               final String img= imageList.get(1);
                img2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Picasso.with(getApplicationContext()).load(img).into(item_image);

                    }
                });
            }
            if(imageList.get(2)== null || imageList.get(2) == "" || imageList.get(2).contentEquals("") ){
                img3.setImageResource(R.drawable.error);

            }
            else {
                Picasso.with(getApplicationContext()).load(imageList.get(2)).into(img3);
                final String img= imageList.get(2);
                img3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Picasso.with(getApplicationContext()).load(img).into(item_image);

                    }
                });
            }
            if(imageList.get(3)== null || imageList.get(3) == "" || imageList.get(3).contentEquals("") ){
                img4.setImageResource(R.drawable.error);

            }
            else {
                Picasso.with(getApplicationContext()).load(imageList.get(3)).into(img4);
                final String img= imageList.get(3);
                img4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Picasso.with(getApplicationContext()).load(img).into(item_image);

                    }
                });
            }
            /*if(imageList.get(4)== null || imageList.get(4) == "" || imageList.get(4).contentEquals("") ){
                img4.setImageResource(R.drawable.error);

            }
            else {
                Picasso.with(getApplicationContext()).load(imageList.get(4)).into(img4);
            }*/
            tv_desc.setText(description);
            tv_productnm.setText(name);
            tv_price.setText(price);
        }
    }


    private class getPindetails extends AsyncTask<String, Void, Void> {
        private static final String TAG = "getpin";
        int server_status;
        String server_message;
        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(ItemDetails.this, "Loading ", "Please wait...");
            }
        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.CHECK_PINCODE;
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
                        .appendQueryParameter("pincode", params[0]);
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
    "pincodes": [
        {
            "id": 1,
            "pin": "751024",
            "charge": 12,
            "area": "fds fds fdsf sdf",
            "created": "2019-01-11T18:33:01+00:00",
            "modified": "2019-01-11T18:33:01+00:00"
        }
    ]
}*/

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    JSONArray ListArray = res.getJSONArray("pincodes");
                    if(ListArray.length()<=0){
                        server_message="No Items Found";

                    }
                    else{
                        server_status=1;
                        server_message="Pincode Checked";
                        for (int i = 0; i < ListArray.length(); i++) {
                            JSONObject o_list_obj = ListArray.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                             ship_charge = o_list_obj.getString("charge");
                             area = o_list_obj.getString("area");
                             pin = o_list_obj.getString("pin");
                            PincodeList list1 = new PincodeList(id,ship_charge,area,pin);
                            pinlist.add(list1);


                        }
                    }
                }
                return null;
            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
                server_message="Network Error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void data) {
            super.onPostExecute(data);
            progressDialog.dismiss();
            if(server_status==1) {
                Toast.makeText(ItemDetails.this,server_message,Toast.LENGTH_SHORT).show();
                lin_pin.setVisibility(View.GONE);
                rel_pin.setVisibility(View.VISIBLE);
                tv_shipng_charge.setText("Delivery charges "+" : "+ship_charge);
                tv_area.setText("Deliver to"+" "+area+" - "+pin);
                dialog.dismiss();
            }
            else{
                Constants.SomethingWrong(ItemDetails.this,server_message);
            }
        }
    }


    private class addCartdetails extends AsyncTask<String,Void,Void>{
        private static final String TAG = "adding cart";
        int server_status;
        String server_message;
        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(ItemDetails.this, "Loading ", "Please wait...");
            }
        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.ADD_CART;
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
                        .appendQueryParameter("user_id", params[0])
                        .appendQueryParameter("item_id", params[1])
                        .appendQueryParameter("price", params[2])
                        .appendQueryParameter("quentity","1");

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

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject _object = res.getJSONObject("res");
                    server_status = _object.optInt("status");
                    if (server_status == 1) {
                        server_message="Item  added to cart";
                    }

                    else {
                        server_message = "Error While item added";
                    }
                }
                return null;
            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
                server_message="Network Error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void data) {
            super.onPostExecute(data);
            progressDialog.dismiss();
            if(server_status==1) {
                String item_name=name.toUpperCase();
                tv_aded_item_name.setText(" '' "+item_name+" '' "+" added to cart ");
                relative_addcart.setVisibility(View.VISIBLE);
                gotocart.setVisibility(View.GONE);
                showSnackBar("ItemAddedToCart");

            }
            else{
                Constants.SomethingWrong(ItemDetails.this,server_message);
            }
        }
    }
    void showSnackBar(String message){
        Snackbar snackbar = Snackbar
                .make(rel_add, "", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#ae0a11"));

        snackbar.show();
    }
}
