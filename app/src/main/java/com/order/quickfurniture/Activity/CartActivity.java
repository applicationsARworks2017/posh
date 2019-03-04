package com.order.quickfurniture.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.order.quickfurniture.Adapter.AddressAdapter;
import com.order.quickfurniture.Adapter.CartAdapter;
import com.order.quickfurniture.Pojo.AddRessList;
import com.order.quickfurniture.Pojo.Cartlist;
import com.order.quickfurniture.R;
import com.order.quickfurniture.Util.APIManager;
import com.order.quickfurniture.Util.CheckInternet;
import com.order.quickfurniture.Util.Constants;

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

public class CartActivity extends AppCompatActivity {
    Toolbar cart_tool;
    String name,user_id;
    ListView lv_cart;
    RelativeLayout rel;
    ArrayList<Cartlist> cList;
    CartAdapter c_Adapter;
    SwipeRefreshLayout swipe_order;
    TextView no_cart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);


        user_id =getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
        no_cart=(TextView)findViewById(R.id.no_cart);

        cart_tool = (Toolbar)findViewById(R.id.cart_tool);
        cart_tool.setNavigationIcon(R.mipmap.baseline_arrow_back_white_24);
        cart_tool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        cart_tool.setTitle("My Cart");
        cart_tool.setTitleTextColor(Color.WHITE);
        rel=(RelativeLayout)findViewById(R.id.rel);
        swipe_order=(SwipeRefreshLayout)findViewById(R.id.order_swipe);
        lv_cart=(ListView)findViewById(R.id.lv_cart);

        getCartList();
        swipe_order.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCartList();

            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            // and get whatever type user account id is
        }





    }

    public void getCartList() {
        swipe_order.setRefreshing(false);
        if(CheckInternet.getNetworkConnectivityStatus(CartActivity.this)){
            //calltoAPI(user_id);
            new getCartlist().execute(user_id);

        }
        else{
            showSnackBar("No Internet");
        }

    }

    /*private void calltoAPI(String user_id) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading List. Please wait...");
        pd.show();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("user_id",user_id );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIManager().postJSONArrayAPIModified(Constants.MAINURL+Constants.CART_LIST,"carts","item",jsonObject, Cartlist.class,this,
                new APIManager.APIManagerInterface() {
                    @Override
                    public void onSuccess(Object resultObj) {
                        cList=(ArrayList<Cartlist>) resultObj;
                        c_Adapter = new CartAdapter(CartActivity.this,cList );
                        lv_cart.setAdapter(c_Adapter);
                        pd.dismiss();
                    }

                    @Override
                    public void onError(String error) {
                        swipe_order.setVisibility(View.GONE);
                        no_cart.setVisibility(View.VISIBLE);
                        Toast.makeText(CartActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                });
    }*/

    void showSnackBar(String message){
        Snackbar snackbar = Snackbar
                .make(rel, "", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#ae0a11"));

        snackbar.show();
    }

    private class getCartlist extends AsyncTask<String, Void, Void> {

        private static final String TAG = "getAdressList";
        int server_status;
        String server_message;
        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(CartActivity.this, "Loading Items", "Please wait...");
            }
        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.CART_LIST;
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
                        .appendQueryParameter("user_id", user_id);
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
    "carts": [
        {
            "id": 26,
            "user_id": 1,
            "item_id": 5,
            "quentity": 1,
            "price": 100,
            "created": "2019-02-18T14:00:05+00:00",
            "modified": "2019-02-18T14:00:05+00:00",
            "item": {
                "id": 5,
                "category_id": 3,
                "sub_category_id": 1,
                "item_type_id": 7,
                "brand_id": 6,
                "name": "Walls",
                "price": 100,
                "discount": 10,
                "actual_price": 9.96,
                "image": "http://theposh.online/files/items/file15509313781357158440.jpg,http://theposh.online/files/items/file1550931378563143814.jpg",
                "description": "To compliment your bar table, you'll need one or more stylish wooden bar stools. Urban Ladder's wooden bar stools are built to provide you with the utmost comfort. Explore a wide selection of stylish and ergonomic bar stools online! These amazing bar stools come in excellent finishes of mahogany",
                "created": "2018-12-18T18:58:26+00:00",
                "modified": "2019-02-23T14:16:18+00:00"
            },
}*/

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    cList=new ArrayList<>();
                     JSONArray ListArray = res.getJSONArray("carts");
                    if(ListArray.length()<=0){
                        server_message="No Items Found";

                    }
                    else{
                        server_status=1;
                        for (int i = 0; i < ListArray.length(); i++) {
                            JSONObject o_list_obj = ListArray.getJSONObject(i);
                            String c_id = o_list_obj.getString("id");
                            String user_id = o_list_obj.getString("user_id");
                            String item_id = o_list_obj.getString("item_id");
                            String quentity = o_list_obj.getString("quentity");
                            String price = o_list_obj.getString("price");

                            JSONObject i_list=o_list_obj.getJSONObject("item");

                            String i_id = i_list.getString("id");
                            String category_id = i_list.getString("category_id");
                            String sub_category_id = i_list.getString("sub_category_id");
                            String item_type_id = i_list.getString("item_type_id");
                            String name = i_list.getString("name");
                            String discount = i_list.getString("discount");
                            String actual_price = i_list.getString("actual_price");
                            String image = i_list.getString("image");
                            String description = i_list.getString("description");
                            Cartlist list1 = new Cartlist(c_id,user_id,item_id,quentity,price,i_id,category_id,sub_category_id,item_type_id,
                                    name,discount,actual_price,image,description);
                            cList.add(list1);
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
                c_Adapter = new CartAdapter(CartActivity.this,cList );
                lv_cart.setAdapter(c_Adapter);
            }
            else{
                Constants.SomethingWrong(CartActivity.this,server_message);
            }
        }
    }

}
