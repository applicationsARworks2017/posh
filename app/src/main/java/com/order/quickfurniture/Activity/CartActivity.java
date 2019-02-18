package com.order.quickfurniture.Activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.order.quickfurniture.R;

public class CartActivity extends AppCompatActivity {
    Toolbar cart_tool;
    String name;
    TextView it_name;
    ImageView split_minus,split_plus;
    EditText et_price;

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

        it_name=(TextView)findViewById(R.id.it_name);
        et_price=(EditText) findViewById(R.id.et_price);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            // and get whatever type user account id is
        }
        it_name.setText(name);

         split_minus=(ImageView)findViewById(R.id.minus);
         split_plus=(ImageView)findViewById(R.id.plus);

        split_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Double  qnty=Double.valueOf(et_price.getText().toString().trim());
                        if(qnty==1){
                            qnty=qnty+1;
                        }
                        else {
                            qnty=qnty+1;
                        }
                et_price.setText(String.valueOf(qnty));

               // qnty=qnty+1;
                //split_qty.setText(String.valueOf(qnty));
            }
        });
            split_minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Double qnty = Double.valueOf(et_price.getText().toString().trim());

                    if(qnty==2 || qnty==2.0){
                        qnty=qnty-1;
                    }
                    else if(qnty==1){
                    }
                    else {
                        qnty=qnty-1;
                    }

                    et_price.setText(String.valueOf(qnty));

                    // split_qty.setText(String.valueOf(qnty));
                }
            });
    }
}
