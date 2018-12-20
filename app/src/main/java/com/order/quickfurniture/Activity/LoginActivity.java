package com.order.quickfurniture.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.util.Util;
import com.order.quickfurniture.Pojo.User;
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
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    TextView tv_create_acc,tv_forgot_pass;
    Button bt_login;
    EditText et_username,et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tv_create_acc = (TextView) findViewById(R.id.tv_create_acc);
        tv_forgot_pass = (TextView) findViewById(R.id.tv_forgot_pass);
        bt_login = (Button) findViewById(R.id.bt_login);
        et_password=(EditText)findViewById(R.id.et_password);
        et_username=(EditText)findViewById(R.id.et_username);

        tv_create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,CreateAccount.class);
                startActivity(intent);
            }
        });
        tv_forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,Forgotpassword.class);
                startActivity(intent);
            }
        });
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_username.getText().toString().trim().length()<=0){
                    et_username.setError("Enter phone or email");
                }
                else if(et_password.getText().toString().trim().length()<=0){
                    et_password.setError("Enter password");
                }
                else{
                    if(CheckInternet.getNetworkConnectivityStatus(LoginActivity.this)){
                        continueLogin(et_username.getText().toString().trim(),et_password.getText().toString().trim());

                    }
                    else{
                        Constants.NointernetDialog(LoginActivity.this);
                    }
                }
            }
        });
    }

    private void continueLogin(String username, String password) {

        Login_asyn login_asyn = new Login_asyn();
        login_asyn.execute(username,password);
    }

    /*
     * Login Asyntask for security
     * */

    private class Login_asyn extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        private ProgressDialog progressDialog = null;
        int server_status;
        String id,mobile,name;
        String server_message;
        String user_type;
        String photo,email_id;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(LoginActivity.this, "Loggin in", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _userphone = params[0];
                String _userpass=params[1];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.MAINURL+Constants.LOGIN;
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
                        .appendQueryParameter("email", _userphone)
                        .appendQueryParameter("password", _userpass);

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
                if(in == null){
                    return null;
                }
                BufferedReader reader =new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String response = "",data="";

                while ((data = reader.readLine()) != null){
                    response += data + "\n";
                }

                Log.i(TAG, "Response : "+response);

                /**
                 * {
                 "users": {
                 "id": 1,
                 "name": "sdfsdfd",
                 "email": "avinash@yahoo.com",
                 "mobile": "7205674061",
                 "photo": "file1543543632488457718.png",
                 "created": "2018-11-30T02:07:12+00:00",
                 "modified": "2018-11-30T02:07:12+00:00",
                 "usertype": "dfdf",
                 "fcm_id": "df",
                 "ios_fcm_id": "dfdf",
                 "message": "user available.",
                 "status": 1
                 }
                 }
                * */
                if(response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject user_object = res.getJSONObject("users");
                    server_status = user_object.optInt("status");
                    if(server_status==1) {
                        id=user_object.optString("id");
                        name = user_object.optString("name");
                        email_id = user_object.optString("email_id");
                        mobile = user_object.optString("mobile");
                        photo = user_object.optString("photo");
                        user_type = user_object.optString("usertype");
                        server_message=res.optString("message");
                        User ulist=new User();
                        ulist.setId(id);
                        ulist.setName(name);
                        ulist.setEmail(email_id);
                        ulist.setPhoto(photo);
                        ulist.setUser_type(user_type);
                    }
                    else{
                        server_message="Invalid Credentials";
                    }
                }

                return null;

            } catch(SocketTimeoutException exception){
                server_message="Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch(ConnectException exception){
                server_message="Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch(MalformedURLException exception){
                server_message="Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch (IOException exception){
                server_message="Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            } catch(Exception exception){
                server_message="Network Error";
                Log.e(TAG, "SynchMobnum : doInBackground", exception);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void user) {
            super.onPostExecute(user);
            progressDialog.cancel();

            if(server_status==1) {
                SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.USER_ID, id);
                editor.putString(Constants.USER_MOBILE, mobile);
                editor.putString(Constants.USER_NAME, name);
                editor.putString(Constants.USER_PHOTO, photo);
                editor.putString(Constants.USER_EMAIL, email_id);
                editor.commit();
            }
            else {
                // Toast.makeText(Login_Activity.this, server_message, Toast.LENGTH_LONG).show();
                Constants.SomethingWrong(LoginActivity.this,"Incorrect Email,Phone or Password");
            }
        }
    }

}
