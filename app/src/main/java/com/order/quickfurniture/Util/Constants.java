package com.order.quickfurniture.Util;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.order.quickfurniture.R;

public class Constants {

    public static  String MAINURL = "http://theposh.online/" ;
    public static  String LOGIN ="users/loginCheck.json" ;
    public static  String SIGNUP ="users/add.json" ;
    public static  String BANNERLIST ="banners/index.json" ;
    public static  String CATEGORY_LIST ="categories/index.json" ;
    public static  String ITEMS ="items/index.json" ;
    public static  String SUB_CATEGORY_LIST ="sub-categories/index.json" ;
    public static  String SHAREDPREFERENCE_KEY ="user_preference" ;
    public static  String USER_ID ="user_id" ;
    public static  String USER_MOBILE ="user_mobile" ;
    public static  String USER_NAME ="user_name" ;
    public static  String USER_PHOTO ="user_photo" ;
    public static  String USER_EMAIL ="user_email" ;
    public static  String USER_PASSWORD ="user_password" ;
    public static  String SHAREDPREFERENCE_KEY_FCM ="furniturefcm" ;
    public static  String FCM_ID ="fcm_id" ;
    public static  String HOME_ITEM ="Items/otherList.json" ;
    public static  String ITEM_DETAILS ="items/view.json" ;
    public static  String ADDRESS_LIST ="Addresses/index.json" ;
    public static  String ADD_ADDRESS ="Addresses/add.json" ;
    public static  String DELETE_ADDRESS ="Addresses/delete.json" ;
    public static  String CHECK_PINCODE ="Pincodes/index.json" ;
    public static  String ADD_CART ="carts/add.json" ;
    public static  String CART_LIST ="carts/index.json" ;
    public static Boolean IS_LOGIN;

    public static void  NointernetDialog(Activity activity){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog);
        final Button d_ok = (Button) dialog.findViewById(R.id.d_ok);
        d_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void  SomethingWrong(Activity activity, String message){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_wrong);
        final Button d_ok = (Button) dialog.findViewById(R.id.d_ok);
        final TextView tv_message = (TextView) dialog.findViewById(R.id.message);
        tv_message.setText(message);
        d_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
