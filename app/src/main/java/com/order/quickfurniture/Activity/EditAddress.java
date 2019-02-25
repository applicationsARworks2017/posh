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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.order.quickfurniture.R;
import com.order.quickfurniture.Util.CheckInternet;
import com.order.quickfurniture.Util.Constants;

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

public class EditAddress extends AppCompatActivity {
   public static EditText et_name,et_email,et_phone,et_state,et_land,et_adres,et_pin,et_secn;
    TextView tv_save,tv_cancel;
    String id,name,email,phone,state,land,adres,pincode,city;
    RelativeLayout profile_add_adress;
    String user_id;
    Toolbar detail_tool;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);
        user_id =getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);

        detail_tool = (Toolbar) findViewById(R.id.detail_tool);
        detail_tool.setTitleTextColor(Color.WHITE);
        detail_tool.setNavigationIcon(R.mipmap.baseline_arrow_back_white_24);
        detail_tool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(EditAddress.this,HomeActivity.class);
                startActivity(i);
            }
        });
        detail_tool.setTitle(" Edit Address");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            name = extras.getString("name");
            email = extras.getString("email");
            phone = extras.getString("phone");
            adres = extras.getString("adres");
            land = extras.getString("land");
           // pin = extras.getString("pin");
            city = extras.getString("state");
            pincode = extras.getString("pin");
            // and get whatever type user account id is
        }
        profile_add_adress=(RelativeLayout)findViewById(R.id.profile_add_adress);
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

        et_name.setText(name);
        et_email.setText(email);
        et_phone.setText(phone);
        et_adres.setText(adres);
        et_land.setText(land);
        et_pin.setText(pincode);
        et_state.setText(city);
         et_pin.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent i=new Intent(EditAddress.this,Getpincode.class);
                 i.putExtra("PAGE","editpage");
                 startActivity(i);
             }
         });
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(EditAddress.this,HomeActivity.class);
                startActivity(i);
                finish();
            }
        });
        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkvalidation();
            }
        });
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
                    ,et_land.getText().toString().trim(),et_state.getText().toString().trim(),Getpincode.pin_id);

        }
    }

    private void AddAdress(String fullname, String email, String phone, String adres, String landmark,
                           String state, String pin) {
        if (CheckInternet.getNetworkConnectivityStatus(EditAddress.this)) {
            //new Addares_asyn().execute(fullname, email, phone, adres, landmark, state, pin);
        } else {
            Constants.NointernetDialog(EditAddress.this);
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
                progressDialog = ProgressDialog.show(EditAddress.this, "Adding Address", "Please wait...");
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
                        server_message="Successfully Address Added";
                    }

                    else {
                        server_message = "Error While Adding Address";
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
                Constants.SomethingWrong(EditAddress.this,server_message);
                Intent i=new Intent(EditAddress.this,HomeActivity.class);
                startActivity(i);
                finish();

            }
            else {
                Constants.SomethingWrong(EditAddress.this,server_message);
            }
        }
    }

}
