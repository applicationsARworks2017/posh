package com.order.quickfurniture.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.order.quickfurniture.Adapter.AddressAdapter;
import com.order.quickfurniture.Adapter.ItemlistAdapter;
import com.order.quickfurniture.Pojo.AddRessList;
import com.order.quickfurniture.Pojo.Items;
import com.order.quickfurniture.Pojo.User;
import com.order.quickfurniture.R;
import com.order.quickfurniture.Util.CheckInternet;
import com.order.quickfurniture.Util.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

public class GetAddress extends AppCompatActivity {
    RelativeLayout profile_add_adress,add_rel;
    ListView lv_address;
    public   static EditText et_name,et_email,et_phone,et_state,et_land,et_adres,et_pin,et_secn;
    TextView tv_save,tv_cancel;
    ArrayList<AddRessList> addresList;
    String user_id;
    AddressAdapter adapter;
    Toolbar detail_tool;
    ScrollView scrol_profile_add_adress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_address);

        user_id =getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);

        detail_tool = (Toolbar) findViewById(R.id.detail_tool);
        detail_tool.setTitleTextColor(Color.WHITE);
        detail_tool.setNavigationIcon(R.mipmap.baseline_arrow_back_white_24);
        detail_tool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        detail_tool.setTitle("Manage Address");

        scrol_profile_add_adress=(ScrollView) findViewById(R.id.scrol_profile_add_adress);
        add_rel=(RelativeLayout)findViewById(R.id.add_rel);
        lv_address=(ListView)findViewById(R.id.lv_address);
        et_email=(EditText)findViewById(R.id.et_email);
        et_phone=(EditText)findViewById(R.id.et_phone);
        et_state=(EditText)findViewById(R.id.et_state);
        et_land=(EditText)findViewById(R.id.et_land);
        et_adres=(EditText)findViewById(R.id.et_address);
        et_pin=(EditText)findViewById(R.id.et_pin);
        et_secn=(EditText)findViewById(R.id.et_alertphn);
        et_name=(EditText)findViewById(R.id.et_fullname);
        tv_save=(TextView)findViewById(R.id.tv_save);
        tv_cancel=(TextView)findViewById(R.id.tv_cancel);

        add_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrol_profile_add_adress.setVisibility(View.VISIBLE);
                add_rel.setVisibility(View.GONE);
            }
        });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrol_profile_add_adress.setVisibility(View.GONE);
                add_rel.setVisibility(View.VISIBLE);
            }
        });
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             checkvalidation();
            }
        });
        et_pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(GetAddress.this,Getpincode.class);
                i.putExtra("PAGE","getaddresspage");

                startActivity(i);
            }
        });

        getAddressList();

    }

    private void checkvalidation() {
        if(et_name.getText().toString().trim().length()<=0){
            et_name.setError("Enter Full Name");
        }
        else if(et_email.getText().toString().trim().length()<=0){
            et_email.setError("Enter Email");
        }
        else if(et_phone.getText().toString().trim().length()<10){
            et_phone.setError("Enter Valid Phone Number");
        }
        else if(et_adres.getText().toString().trim().length()<=0){
            et_adres.setError("Enter Address");
        }else if(et_land.getText().toString().trim().length()<=0){
            et_land.setError("Enter Landmark");
        }else if(et_state.getText().toString().trim().length()<=0){
            et_state.setError("Enter State");
        }else if(et_pin.getText().toString().trim().length()<=0){
            et_pin.setError("Enter Pincode");
        }
        else {
            AddAdress(et_name.getText().toString().trim(),et_email.getText().toString().trim(),
                    et_phone.getText().toString().trim(),et_adres.getText().toString().trim()
                    ,et_land.getText().toString().trim(),et_state.getText().toString().trim(),et_pin.getText().toString().trim());

        }
    }

    private void AddAdress(String fullname, String email, String phone, String adres, String landmark,
                           String state, String pin) {
        if(CheckInternet.getNetworkConnectivityStatus(GetAddress.this)){
            new Addares_asyn().execute(fullname,email,phone,adres,landmark,state,Getpincode.pin_id);
        }
        else {
            Constants.NointernetDialog(GetAddress.this);
        }
    }

    private void getAddressList() {
        addresList = new ArrayList<>();
        if(CheckInternet.getNetworkConnectivityStatus(GetAddress.this)){
            AdressSync adress = new AdressSync();
            adress.execute(user_id);
        }
        else{
            Constants.NointernetDialog(GetAddress.this);
        }
    }

    private class AdressSync extends AsyncTask<String, Void, Void> {

    private static final String TAG = "getAdressList";
    int server_status;
    String server_message;
    ProgressDialog progressDialog = null;

    @Override
    protected void onPreExecute() {
        if(progressDialog == null) {
            progressDialog = ProgressDialog.show(GetAddress.this, "Loading Items", "Please wait...");
        }
    }
    @Override
    protected Void doInBackground(String... params) {


        try {
            InputStream in = null;
            int resCode = -1;
            String link = Constants.MAINURL + Constants.ADDRESS_LIST;
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
                // server_status=res.getInt("status");
                JSONArray ListArray = res.getJSONArray("addresses");
                if(ListArray.length()<=0){
                    server_message="No Items Found";

                }
                else{
                    server_status=1;
                    for (int i = 0; i < ListArray.length(); i++) {
                        JSONObject o_list_obj = ListArray.getJSONObject(i);
                        String id = o_list_obj.getString("id");
                        String full_name = o_list_obj.getString("full_name");
                        String mobile = o_list_obj.getString("mobile");
                        String pincode_id = o_list_obj.getString("pincode_id");
                        String address = o_list_obj.getString("address");
                        String locality = o_list_obj.getString("locality");
                        String landmark = o_list_obj.getString("landmark");
                        String city = o_list_obj.getString("city");
                        String email = o_list_obj.getString("email");
                        JSONObject mypincode = o_list_obj.getJSONObject("pincode");
                        String pincode = mypincode.getString("pin");
                        AddRessList list1 = new AddRessList(id,full_name,mobile,pincode_id,address,
                                locality,landmark,city,email,pincode);
                        addresList.add(list1);
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
            adapter = new AddressAdapter(GetAddress.this,addresList );
            lv_address.setAdapter(adapter);
        }
        else{
            Constants.SomethingWrong(GetAddress.this,server_message);
        }
    }
}

    private class Addares_asyn  extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        private ProgressDialog progressDialog = null;
        int server_status;
        String id, mobile, name;
        String server_message;
        String user_type;
        String photo, email_id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(GetAddress.this, "Adding Address", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _fullname = params[0];
                String _email = params[1];
                String _phone = params[2];
                String pincode_id = params[6];
                String address = params[3];
                String landmark = params[4];
                String city = params[5];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.MAINURL + Constants.ADD_ADDRESS;
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
                        .appendQueryParameter("user_id", user_id)
                        .appendQueryParameter("full_name", _fullname)
                        .appendQueryParameter("email", _email)
                        .appendQueryParameter("mobile", _phone)
                        .appendQueryParameter("pincode_id", pincode_id)
                        .appendQueryParameter("address", address)
                        .appendQueryParameter("landmark", landmark)
                        .appendQueryParameter("locality", landmark)
                        .appendQueryParameter("city", city);

                //.appendQueryParameter("deviceid", deviceid);
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

                /**
                 * "{
                 ""res"": {
                 ""message"": ""The user has been saved."",
                 ""status"": 1
                 },
                 ""users"": {
                 ""id"": 4,
                 ""name"": ""sdfsdfd"",
                 ""email"": ""avinhjjsh@yahoo.com"",
                 ""mobile"": ""7205674052"",
                 ""photo"": null,
                 ""created"": ""2018-12-06T02:20:59+00:00"",
                 ""modified"": ""2018-12-06T02:20:59+00:00"",
                 ""usertype"": ""dfdf"",
                 ""fcm_id"": ""df"",
                 ""ios_fcm_id"": ""dfdf""
                 }
                 }"
                 * */
                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject _object = res.getJSONObject("res");
                    server_status = _object.optInt("status");
                    if (server_status == 1) {
                        server_message="Your Address Added";
                    }

                    else {
                        server_message = "Error While Creating Account";
                    }
                }

                return null;

            } catch (SocketTimeoutException exception) {
                server_message = "Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch (ConnectException exception) {
                server_message = "Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch (MalformedURLException exception) {
                server_message = "Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch (IOException exception) {
                server_message = "Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch (Exception exception) {
                server_message = "Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progressDialog.cancel();
            if(server_status==1) {
                Constants.SomethingWrong(GetAddress.this,server_message);
                Intent i=new Intent(GetAddress.this,HomeActivity.class);
                startActivity(i);
                finish();

            }
            else {
                Constants.SomethingWrong(GetAddress.this,server_message);
            }
        }
    }
}
