package com.order.quickfurniture.Activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.order.quickfurniture.Adapter.CartAdapter;
import com.order.quickfurniture.Pojo.Cartlist;
import com.order.quickfurniture.R;
import com.order.quickfurniture.Util.APIManager;
import com.order.quickfurniture.Util.CheckInternet;
import com.order.quickfurniture.Util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

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

        getCartList();
        swipe_order.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe_order.setRefreshing(false);
                getCartList();

            }
        });


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            // and get whatever type user account id is
        }





    }

    private void getCartList() {
        if(CheckInternet.getNetworkConnectivityStatus(CartActivity.this)){
            calltoAPI(user_id);

        }
        else{
            showSnackBar("No Internet");
        }

    }

    private void calltoAPI(String user_id) {
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading List. Please wait...");
        pd.show();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("user_id",user_id );

        } catch (JSONException e) {
            e.printStackTrace();
        }
        new APIManager().postJSONArrayAPI(Constants.MAINURL+Constants.CART_LIST,"carts",jsonObject, Cartlist.class,this,
                new APIManager.APIManagerInterface() {
                    @Override
                    public void onSuccess(Object resultObj) {
                        cList=(ArrayList<Cartlist>) resultObj;
                        c_Adapter = new CartAdapter(CartActivity.this,cList );
                        lv_cart.setAdapter(c_Adapter);
                        pd.cancel();
                    }

                    @Override
                    public void onError(String error) {
                        swipe_order.setVisibility(View.GONE);
                        no_cart.setVisibility(View.VISIBLE);
                        Toast.makeText(CartActivity.this,error.toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();
                    }
                });
    }

    void showSnackBar(String message){
        Snackbar snackbar = Snackbar
                .make(rel, "", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#ae0a11"));

        snackbar.show();
    }
}
