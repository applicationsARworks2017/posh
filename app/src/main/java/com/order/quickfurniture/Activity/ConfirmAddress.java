package com.order.quickfurniture.Activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.order.quickfurniture.Adapter.AddressAdapter;
import com.order.quickfurniture.Adapter.ConAddressAdapter;
import com.order.quickfurniture.Pojo.AddRessList;
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

public class ConfirmAddress extends AppCompatActivity {

    ListView add_list;
    ArrayList<AddRessList> addresList;
    String user_id;
    ConAddressAdapter addressAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_address);
        add_list = (ListView)findViewById(R.id.add_list);
        user_id =getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);

        getAddress();
    }

    private void getAddress() {
        addresList = new ArrayList<>();
        if(CheckInternet.getNetworkConnectivityStatus(ConfirmAddress.this)){
            AdressSync adress = new AdressSync();
            adress.execute(user_id);

        }
        else{
            Toast.makeText(ConfirmAddress.this,"No Internet",Toast.LENGTH_LONG).show();
        }
    }
    private class AdressSync extends AsyncTask<String, Void, Void> {

        private static final String TAG = "getAdressList";
        int server_status;
        String server_message;
        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(ConfirmAddress.this, "Loading Items", "Please wait...");
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
                    if (ListArray.length() <= 0) {
                        server_message = "No Items Found";

                    } else {
                        server_status = 1;
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
                            AddRessList list1 = new AddRessList(id, full_name, mobile, pincode_id, address,
                                    locality, landmark, city, email, pincode);
                            addresList.add(list1);
                        }
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
                addressAdapter = new ConAddressAdapter(ConfirmAddress.this, addresList);
                add_list.setAdapter(addressAdapter);
            } else {
                Constants.SomethingWrong(ConfirmAddress.this, server_message);
            }
        }
    }
}
