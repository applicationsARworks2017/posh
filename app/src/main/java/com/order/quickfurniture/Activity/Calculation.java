package com.order.quickfurniture.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.order.quickfurniture.Adapter.ConAddressAdapter;
import com.order.quickfurniture.Pojo.AddRessList;
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

public class Calculation extends AppCompatActivity {
    String address_id,pincode_id,delivery_charge,user_id;
    Double cgst,sgst,igst;
    TextView tv_cgst,cgst_cost,tv_sgst,sgst_cost,tv_igst,igst_cost,tv_delivery,delivery_cost,
            tv_total,total_cost,tv_item_price,cost_item;
    Button place_order;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            address_id = extras.getString("ADDRESSID");
            pincode_id = extras.getString("PINID");
            delivery_charge = extras.getString("DELIVERY");
        }

        tv_cgst = (TextView) findViewById(R.id.tv_cgst);
        cgst_cost = (TextView) findViewById(R.id.cgst_cost);
        sgst_cost = (TextView) findViewById(R.id.sgst_cost);
        igst_cost = (TextView) findViewById(R.id.igst_cost);
        tv_delivery = (TextView) findViewById(R.id.tv_delivery);
        delivery_cost = (TextView) findViewById(R.id.delivery_cost);
        tv_igst = (TextView) findViewById(R.id.tv_igst);
        tv_sgst = (TextView) findViewById(R.id.tv_sgst);
        tv_total = (TextView) findViewById(R.id.tv_total);
        total_cost = (TextView) findViewById(R.id.total_cost);
        tv_item_price = (TextView) findViewById(R.id.tv_item_price);
        cost_item = (TextView) findViewById(R.id.cost_item);
        place_order = (Button) findViewById(R.id.place_order);
        user_id =getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);


        makeCalculation();


        place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(CheckInternet.getNetworkConnectivityStatus(Calculation.this)){
                 placeOrder(user_id,address_id);
             }
             else{
                 Constants.NointernetDialog(Calculation.this);
             }
            }
        });
    }

    private void makeCalculation() {
        if(CheckInternet.getNetworkConnectivityStatus(Calculation.this)){
            Mytax getTax = new Mytax();
            getTax.execute();
        }
        else{
            Constants.NointernetDialog(Calculation.this);
        }
    }



    private class Mytax extends AsyncTask<String, Void, Void> {

        private static final String TAG = "getAdressList";
        int server_status;
        String server_message;
        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(Calculation.this, "Getting TaxDetails", "Please wait...");
            }
        }

        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.TAX_DETAILS;
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
                        .appendQueryParameter("", "");
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
                * {
    "res": {
        "SGST": 9,
        "CGST": 9
    }
}
                * */


                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    JSONObject jsonObject = res.getJSONObject("res");
                    cgst = jsonObject.getDouble("CGST");
                    sgst = jsonObject.getDouble("SGST");

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
            setAllValue();

        }
    }

    private void setAllValue() {
        tv_item_price.setText("ITEM PRICE");
        cost_item.setText(String.valueOf(CartActivity.total_amount));
        tv_cgst.setText("CGST");
        tv_sgst.setText("SGST");
        tv_total.setText("Total");
        Double cgst_amount = CartActivity.total_amount * (cgst/100);
        Double sgst_amount = CartActivity.total_amount * (sgst/100);
        Double igst_amount = cgst_amount+sgst_amount;
        Double delivery = Double.parseDouble(delivery_charge);
        Double total = CartActivity.total_amount+igst_amount + delivery ;

        cgst_cost.setText(String.valueOf(CartActivity.total_amount * (cgst/100)));
        sgst_cost.setText(String.valueOf(CartActivity.total_amount * (sgst/100)));
        tv_igst.setText("IGST");
        igst_cost.setText(String.valueOf( (CartActivity.total_amount * (cgst/100))+ (CartActivity.total_amount * (sgst/100))) );
        tv_delivery.setText("DELIVEERY CHARGE");
        delivery_cost.setText(delivery_charge);
        total_cost.setText(String.valueOf(total));


    }




    private void placeOrder(String user_id,String address_id) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Placing Order. Please wait...");
        pd.show();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("user_id",user_id );
            jsonObject.put("address_id",address_id );

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new APIManager().ModifyAPI(Constants.MAINURL+Constants.CONFIRM_ORDER,"res",jsonObject, Calculation.this,
                new APIManager.APIManagerInterface() {
                    @Override
                    public void onSuccess(Object resultObj) {
                        Toast.makeText(Calculation.this,"Order Placed", Toast.LENGTH_LONG).show();
                        pd.dismiss();
                        Intent intent = new Intent(Calculation.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                        //new CartActivity().getCartList();
                    }

                    @Override
                    public void onError(String error) {

                        Toast.makeText(Calculation.this,error.toString(), Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                });
    }


}
