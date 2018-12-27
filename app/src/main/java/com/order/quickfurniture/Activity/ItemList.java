package com.order.quickfurniture.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;

import com.order.quickfurniture.Adapter.CategoryAdapter;
import com.order.quickfurniture.Adapter.ItemlistAdapter;
import com.order.quickfurniture.Pojo.Category;
import com.order.quickfurniture.Pojo.Items;
import com.order.quickfurniture.R;
import com.order.quickfurniture.Util.CheckInternet;
import com.order.quickfurniture.Util.Constants;

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

public class ItemList extends AppCompatActivity {
    String sub_cat_id,sub_cat_name;
    Toolbar itemlist_tool;
    GridView itemgridview;
    SearchView searchView;
    ArrayList<Items> itemsArrayList;
    ItemlistAdapter adapter;
    String _id,_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        itemlist_tool = (Toolbar)findViewById(R.id.itemlist_tool);
        itemgridview = (GridView)findViewById(R.id.itemgridview);
        searchView = (SearchView)findViewById(R.id.item_search);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sub_cat_id = extras.getString("SUBCAT_ID");
            sub_cat_name = extras.getString("SUBCAT_NAME");
            // and get whatever type user account id is
        }
        itemlist_tool.setTitle(sub_cat_name);
        itemlist_tool.setTitleTextColor(Color.WHITE);
        itemlist_tool.setNavigationIcon(R.mipmap.baseline_arrow_back_white_24);
        itemlist_tool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        itemgridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Items item = (Items) adapterView.getItemAtPosition(i);
                _id = item.getId();
                _name = item.getName();
                Intent intent = new Intent(getApplication(),ItemDetails.class);
                intent.putExtra("_ID",_id);
                intent.putExtra("_NAME",_name);
                startActivity(intent);
            }
        });

        getItemList(sub_cat_id);


    }

    private void getItemList(String sub_cat_id) {
        itemsArrayList = new ArrayList<>();
        if(CheckInternet.getNetworkConnectivityStatus(ItemList.this)){
            ItemListAsyn itemListAsyn = new ItemListAsyn();
            itemListAsyn.execute(sub_cat_id);
        }
        else{
            Constants.NointernetDialog(ItemList.this);
        }
    }


    /*
     * GET ITEM LIST ASYNTASK*/
    private class ItemListAsyn extends AsyncTask<String, Void, Void> {

        private static final String TAG = "getcategoryList";
        int server_status;
        String server_message;
        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(ItemList.this, "Loading Items", "Please wait...");
            }
        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.ITEMS;
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
                        .appendQueryParameter("sub_category_id", params[0]);
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
                * "items": [
        {
            "id": 2,
            "category_id": 2,
            "sub_category_id": 2,
            "item_type_id": 6,
            "brand_id": null,
            "name": "gdsg fdg",
            "price": 200,
            "discount": null,
            "actual_price": 100,
            "image": "http://applicationworld.net/furniture/files/items/file15443360601604370631.jpg",
            "created": "09-12-2018 06:14 AM",
            "modified": "2018-12-09T06:14:20+00:00",
            "sub_category": {
                "id": 2,
                "category_id": 2,
                "title": "Study Table",
                "arabic_title": null,
                "photo": "file1544289701980043927.png",
                "is_enable": "Y",
                "created": "2018-12-01T01:55:44+00:00",
                "modified": "2018-12-08T17:21:41+00:00"
            },
            "category": {
                "id": 2,
                "title": "Table",
                "photo": "file1544288183605749763.png",
                "arabic_title": null,
                "is_enable": "Y",
                "created": "2018-12-08T16:56:23+00:00",
                "modified": "2018-12-08T16:56:23+00:00"
            },*/

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    JSONArray ListArray = res.getJSONArray("items");
                    if(ListArray.length()<=0){
                        server_message="No Items Found";

                    }
                    else{
                        server_status=1;
                        for (int i = 0; i < ListArray.length(); i++) {
                            JSONObject o_list_obj = ListArray.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            String category_id = o_list_obj.getString("category_id");
                            String sub_category_id = o_list_obj.getString("sub_category_id");
                            String name = o_list_obj.getString("name");
                            String price = o_list_obj.getString("price");
                            String actual_price = o_list_obj.getString("actual_price");
                            String discount = o_list_obj.getString("discount");
                            String photo = o_list_obj.getString("image");
                            Items list1 = new Items(id,category_id,sub_category_id,name,price,actual_price,discount,photo);
                            itemsArrayList.add(list1);
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
                adapter = new ItemlistAdapter(ItemList.this,itemsArrayList );
                itemgridview.setAdapter(adapter);
            }
            else{
            Constants.SomethingWrong(ItemList.this,server_message);
            }
        }
    }

}
