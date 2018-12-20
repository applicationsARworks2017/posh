package com.order.quickfurniture.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.order.quickfurniture.Pojo.User;
import com.order.quickfurniture.R;
import com.order.quickfurniture.SplashScreen;
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

public class CreateAccount extends AppCompatActivity {
    EditText et_fullname,et_email,et_phone,et_password;
    Button bt_signup;
    String fcm_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        et_fullname= (EditText)findViewById(R.id.et_fullname);
        et_email=(EditText)findViewById(R.id.et_email);
        et_phone=(EditText)findViewById(R.id.et_phone);
        et_password=(EditText)findViewById(R.id.et_password);
        bt_signup=(Button)findViewById(R.id.bt_signup);

        fcm_id = getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.SHAREDPREFERENCE_KEY_FCM, null);

        bt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(et_fullname.getText().toString().trim().length()<=0){
                    et_fullname.setError("Enter Full Name");
                }
                else if(et_email.getText().toString().trim().length()<=0){
                    et_email.setError("Enter Email");
                }
                else if(et_phone.getText().toString().trim().length()<10){
                    et_phone.setError("Enter Valid Phone Number");
                }
                else if(et_password.getText().toString().trim().length()<=0){
                    et_password.setError("Enter Password");
                }
                else {
                        ContinueSignUp(et_fullname.getText().toString().trim(),et_email.getText().toString().trim(),
                                et_phone.getText().toString().trim(),et_password.getText().toString().trim());

                }
            }
        });
    }

    private void ContinueSignUp(String fullname, String email, String phone, String password) {
        if(CheckInternet.getNetworkConnectivityStatus(CreateAccount.this)){
            new Signup_asyn().execute(fullname,email,phone,password);
        }
        else {
            Constants.NointernetDialog(CreateAccount.this);
        }
    }


    /*
     * SignUp Asyntask for security
     * */

    private class Signup_asyn extends AsyncTask<String, Void, Void> {

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
                progressDialog = ProgressDialog.show(CreateAccount.this, "Creating account", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _fullname = params[0];
                String _email = params[1];
                String _phone = params[2];
                String _password = params[3];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.MAINURL + Constants.SIGNUP;
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
                        .appendQueryParameter("name", _fullname)
                        .appendQueryParameter("email", _email)
                        .appendQueryParameter("mobile", _phone)
                        .appendQueryParameter("fcm_id", fcm_id)
                        .appendQueryParameter("usertype", "user")
                        .appendQueryParameter("password", _password);

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
                        JSONObject user_object = res.getJSONObject("users");
                        id = user_object.optString("id");
                        name = user_object.optString("name");
                        email_id = user_object.optString("email_id");
                        mobile = user_object.optString("mobile");
                        photo = user_object.optString("photo");
                        user_type = user_object.optString("usertype");
                        User ulist = new User();
                        ulist.setId(id);
                        ulist.setName(name);
                        ulist.setEmail(email_id);
                        ulist.setPhoto(photo);
                        ulist.setUser_type(user_type);
                    } else {
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
                SharedPreferences sharedPreferences = CreateAccount.this.getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.USER_ID, id);
                editor.putString(Constants.USER_MOBILE, mobile);
                editor.putString(Constants.USER_NAME, name);
                editor.putString(Constants.USER_PHOTO, photo);
                editor.putString(Constants.USER_EMAIL, email_id);
                editor.commit();
                Intent intent = new Intent(CreateAccount.this, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
            else {
                Constants.SomethingWrong(CreateAccount.this,server_message);
            }
        }
    }

}
