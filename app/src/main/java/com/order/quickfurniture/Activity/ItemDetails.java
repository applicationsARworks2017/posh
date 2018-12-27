package com.order.quickfurniture.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.order.quickfurniture.R;

public class ItemDetails extends AppCompatActivity {
    Toolbar detail_tool;
    String _id,_name;
    FloatingActionButton gotocart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            _id = extras.getString("_ID");
            _name = extras.getString("_NAME");
            // and get whatever type user account id is
        }

        detail_tool = (Toolbar)findViewById(R.id.detail_tool);
        detail_tool.setTitleTextColor(Color.WHITE);
        detail_tool.setNavigationIcon(R.mipmap.baseline_arrow_back_white_24);
        detail_tool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        detail_tool.setTitle(_name);
        gotocart = (FloatingActionButton)findViewById(R.id.gotocart);
        gotocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ItemDetails.this,CartActivity.class);
                startActivity(intent);
            }
        });
    }
}
