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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.order.quickfurniture.Adapter.AddressAdapter;
import com.order.quickfurniture.Adapter.PincodeAdapter;
import com.order.quickfurniture.Pojo.PincodeList;
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

public class Getpincode extends AppCompatActivity {

    SearchView search_pincode;
    ListView lv_pincode;
    String ship_charge,area,pin,user_id;
    ArrayList<PincodeList> pinlist;
    PincodeAdapter adapter;
    Toolbar detail_tool;
    public static String  pin_id;
    String pin_page;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getpincode);

        Bundle extra=getIntent().getExtras();
        if(extra!=null){
            pin_page=extra.getString("PAGE");
        }

        detail_tool = (Toolbar) findViewById(R.id.detail_tool);
        detail_tool.setTitleTextColor(Color.WHITE);
        detail_tool.setNavigationIcon(R.mipmap.baseline_arrow_back_white_24);
        detail_tool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Getpincode.this,GetAddress.class);
                startActivity(i);
            }
        });
        detail_tool.setTitle("PINCODE");

        lv_pincode=(ListView) findViewById(R.id.lv_pincode);
        search_pincode=(SearchView) findViewById(R.id.serch_view);

        getPincode();
        lv_pincode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PincodeList pinlist = (PincodeList) lv_pincode.getItemAtPosition(i);
                 if(pin_page.contentEquals("editpage")){
                     String pincode = pinlist.getPin();
                     pin_id = pinlist.getId();
                     //GetAddress.pin_id=pin_id;
                     EditAddress.et_pin.setText(pincode);
                     finish();
                 }
                 else{
                     String pincode = pinlist.getPin();
                     pin_id = pinlist.getId();
                     //GetAddress.pin_id=pin_id;
                     GetAddress.et_pin.setText(pincode);
                     finish();
                 }

            }
        });
        search_pincode.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setQuestionList(newText);
                return false;
            }
        });

    }

    private void setQuestionList(String filterText) {
            final ArrayList<PincodeList> list_search = new ArrayList<>();
            if (filterText != null && filterText.trim().length() > 0) {
                for (int i = 0; i < pinlist .size(); i++) {
                    String q_title = pinlist.get(i).getPin();
                    if (q_title != null && filterText != null &&
                            q_title.toLowerCase().contains(filterText.toLowerCase())) {
                        list_search.add(pinlist.get(i));
                    }
                }
            }else{
                list_search.addAll(pinlist);
            }
            // create an Object for Adapter
            adapter = new PincodeAdapter(Getpincode.this,list_search );
            lv_pincode.setAdapter(adapter);
            //  mAdapter.notifyDataSetChanged();
            adapter.notifyDataSetChanged();
    }

    private void getPincode() {
        pinlist = new ArrayList<>();
        if(CheckInternet.getNetworkConnectivityStatus(Getpincode.this)){
            new Pincode_asyn().execute();
        }
        else {
            Constants.NointernetDialog(Getpincode.this);
        }
    }

    private class Pincode_asyn extends AsyncTask<String, Void, Void> {
        private static final String TAG = "getPincodeList";
        int server_status;
        String server_message;
        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(Getpincode.this, "Loading ", "Please wait...");
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
                        .appendQueryParameter("pincode", "");
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
                adapter = new PincodeAdapter(Getpincode.this,pinlist );
                lv_pincode.setAdapter(adapter);
            }
            else{
                Constants.SomethingWrong(Getpincode.this,server_message);
            }
        }
    }

}
