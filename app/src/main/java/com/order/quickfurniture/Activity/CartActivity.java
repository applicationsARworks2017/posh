package com.order.quickfurniture.Activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.order.quickfurniture.R;

public class CartActivity extends AppCompatActivity {
    Toolbar cart_tool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
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
    }
}
