package com.order.quickfurniture.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.order.quickfurniture.Pojo.User;
import com.order.quickfurniture.R;
import com.order.quickfurniture.Util.CheckInternet;
import com.order.quickfurniture.Util.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static com.order.quickfurniture.Util.Constants.ITEM_DETAILS;

public class ItemDetails extends AppCompatActivity {
    Toolbar detail_tool;
    String _id, _name;
    FloatingActionButton gotocart;
    String server_message;
    int server_status;
    ImageView item_image,img1,img2,img3,img4;
    String category_id, sub_category_id, item_type_id, name, price, discount, actual_price, image, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            _id = extras.getString("_ID");
            _name = extras.getString("_NAME");
            // and get whatever type user account id is
        }

        item_image = (ImageView)findViewById(R.id.item_image);
        img1 = (ImageView)findViewById(R.id.img1);
        img2 = (ImageView)findViewById(R.id.img2);
        img3 = (ImageView)findViewById(R.id.img3);
        img4 = (ImageView)findViewById(R.id.img4);

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
        gotocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemDetails.this, CartActivity.class);
                startActivity(intent);
            }
        });
        if (CheckInternet.getNetworkConnectivityStatus(ItemDetails.this)) {
            getItemdetails(_id);
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
                img1.setImageResource(R.drawable.error);

            }
            else {
                Picasso.with(getApplicationContext()).load(imageList.get(0)).into(img1);
            }
            if(imageList.get(1)== null || imageList.get(1) == "" || imageList.get(1).contentEquals("") ){
                img2.setImageResource(R.drawable.error);

            }
            else {
                Picasso.with(getApplicationContext()).load(imageList.get(1)).into(img2);
            }
            if(imageList.get(2)== null || imageList.get(2) == "" || imageList.get(2).contentEquals("") ){
                img3.setImageResource(R.drawable.error);

            }
            else {
                Picasso.with(getApplicationContext()).load(imageList.get(2)).into(img3);
            }
            if(imageList.get(3)== null || imageList.get(3) == "" || imageList.get(3).contentEquals("") ){
                img4.setImageResource(R.drawable.error);

            }
            else {
                Picasso.with(getApplicationContext()).load(imageList.get(3)).into(img4);
            }
        }
    }


}
