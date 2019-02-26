package com.order.quickfurniture.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.order.quickfurniture.Activity.CreateAccount;
import com.order.quickfurniture.Activity.GetAddress;
import com.order.quickfurniture.Activity.LoginActivity;
import com.order.quickfurniture.Pojo.User;
import com.order.quickfurniture.R;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String user_id,user_name,user_email,user_phone,user_password;
    Button login,signup,bt_edit;
    RelativeLayout profile_lay;
    ScrollView profile_scroll;
    TextView tv_address,tv_chage_password,tv_edit;
    EditText tv_fullname,tv_email,tv_phone;
    Dialog dialog;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        user_id = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);
        user_name = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_NAME, null);
        user_email = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_EMAIL, null);
        user_password = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_PASSWORD, null);
        user_phone = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_MOBILE, null);
        login = (Button)view.findViewById(R.id.login);
        signup = (Button)view.findViewById(R.id.signup);
        bt_edit = (Button)view.findViewById(R.id.bt_edit);
        profile_lay=(RelativeLayout)view.findViewById(R.id.profile_lay);
        profile_scroll=(ScrollView)view.findViewById(R.id.profile_scroll);
        tv_fullname=(EditText)view.findViewById(R.id.tv_fullname);
        tv_email=(EditText)view.findViewById(R.id.tv_email);
        tv_phone=(EditText)view.findViewById(R.id.tv_phone);
        tv_address=(TextView)view.findViewById(R.id.tv_address);
        tv_edit=(TextView)view.findViewById(R.id.tv_edit);
        tv_chage_password=(TextView)view.findViewById(R.id.tv_password);
        tv_fullname.setEnabled(false);
        tv_email.setEnabled(false);
        tv_phone.setEnabled(false);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateAccount.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_fullname.setEnabled(true);
                tv_fullname.setFocusable(true);
                tv_email.setEnabled(true);
                tv_email.setFocusable(true);
                tv_phone.setEnabled(true);
                tv_phone.setFocusable(true);
                String name = tv_fullname.getText().toString().trim();
                String email = tv_email.getText().toString().trim();
                String phone = tv_phone.getText().toString().trim();
                if (tv_fullname.getText().toString().trim().length() <= 0) {
                    tv_fullname.setError("Enter Full Name");
                } else if (tv_email.getText().toString().trim().length() <= 0) {
                    tv_email.setError("Enter Email");
                } else if (tv_phone.getText().toString().trim().length() < 10) {
                    tv_phone.setError("Enter Valid Phone Number");
                }
                else {
                    Edit_asyn signup_asyn = new Edit_asyn();
                    signup_asyn.execute(user_id,user_password, name, email, phone);
                }
            }
        });

        if(user_id!=""||user_id!=null){
            profile_lay.setVisibility(View.INVISIBLE);
            profile_scroll.setVisibility(View.VISIBLE);
        }
        else {
            profile_lay.setVisibility(View.VISIBLE);
            profile_scroll.setVisibility(View.INVISIBLE);
        }
//        String upperString = user_name.substring(0,1).toUpperCase() + user_name.substring(1);
        tv_fullname.setText(user_name);
        tv_email.setText(user_email);
        tv_phone.setText(user_phone);
        tv_chage_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.dialog_profile_change_password);
                final EditText old_et=(EditText)dialog.findViewById(R.id.et_old_passwsord);
                final EditText new_et=(EditText)dialog.findViewById(R.id.et_new_password);
                Button save=(Button)dialog.findViewById(R.id.btn_password_save);
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String old_pas=old_et.getText().toString().trim();
                        String new_pas=new_et.getText().toString().trim();
                        String name=tv_fullname.getText().toString().trim();
                        String email=tv_email.getText().toString().trim();
                        String phone=tv_phone.getText().toString().trim();
                        if(!old_pas.contentEquals(user_password)){
                            dialog.dismiss();
                            Toast.makeText(getContext(),"Old Password is incorrect",Toast.LENGTH_LONG).show();
                        }
                        else{
                            Edit_asyn signup_asyn = new Edit_asyn();
                            signup_asyn.execute(user_id,new_pas,name,email,phone);
                        }
                       /* if(user_password.contentEquals(old_pas)){
                            SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Constants.USER_PASSWORD, new_pas);
                            editor.commit();
                            //CheckServer();
                        }
                        else{
                            Toast.makeText(getContext(),"Incorrect password",Toast.LENGTH_LONG).show();
                        }*/
                    }
                });
                dialog.show();
            }
        });
        tv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Intent i=new Intent(getContext(), GetAddress.class);
             getContext().startActivity(i);
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    /*
     * Edit Asyntask for profile
     * */

    private class Edit_asyn extends AsyncTask<String, Void, Void> {

        private static final String TAG = "SynchMobnum";
        private ProgressDialog progressDialog = null;
        int server_status;
        String id, mobile, name;
        String server_message;
        String user_type;
        String photo, email_id,password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (progressDialog == null) {
                progressDialog = ProgressDialog.show(getContext(), "Edit account", "Please wait...");
            }
            // onPreExecuteTask();
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                String _id = params[0];
                String _password = params[1];
                String _name = params[2];
                String _email = params[3];
                String _phone= params[4];
                InputStream in = null;
                int resCode = -1;

                String link = Constants.MAINURL + Constants.EDIT_PROFILE;
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
                        .appendQueryParameter("password", _password)
                        .appendQueryParameter("id", _id)
                        .appendQueryParameter("name", _name)
                        .appendQueryParameter("email", _email)
                        .appendQueryParameter("mobile", _phone)
                        .appendQueryParameter("fcm_id", "")
                        .appendQueryParameter("usertype", "user");

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
                 *     "res": {
                 *         "message": "The user has been saved.",
                 *         "status": 1
                 *     },
                 *     "userUpdated": {
                 *         "id": 4,
                 *         "name": "sdfsdfd",
                 *         "password": "1234",
                 *         "email": "avinhjjsh@yahoo.com",
                 *         "mobile": "7205674052",
                 *         "photo": null,
                 *         "created": "2018-12-06T02:20:59+00:00",
                 *         "modified": "2019-02-24T13:08:09+00:00",
                 *         "usertype": "dfdf",
                 *         "fcm_id": "df",
                 *         "ios_fcm_id": "dfdf"
                 *     }
                 * }"
                 * */
                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    JSONObject _object = res.getJSONObject("res");
                    server_status = _object.optInt("status");
                    if (server_status == 1) {
                        JSONObject user_object = res.getJSONObject("userUpdated");
                        id = user_object.optString("id");
                        name = user_object.optString("name");
                        email_id = user_object.optString("email");
                        mobile = user_object.optString("mobile");
                        photo = user_object.optString("photo");
                        user_type = user_object.optString("usertype");
                        password = user_object.optString("password");
                        User ulist = new User();
                        ulist.setId(id);
                        ulist.setName(name);
                        ulist.setEmail(email_id);
                        ulist.setPhoto(photo);
                        ulist.setUser_type(user_type);
                        ulist.setUser_type(password);
                    }

                    else {
                        server_message = "Error While Editing Account";
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
            if (server_status == 1) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.USER_ID, id);
                editor.putString(Constants.USER_MOBILE, mobile);
                editor.putString(Constants.USER_NAME, name);
                editor.putString(Constants.USER_PHOTO, photo);
                editor.putString(Constants.USER_EMAIL, email_id);
                editor.putString(Constants.USER_PASSWORD, password);
                editor.commit();
                tv_fullname.setEnabled(false);
                tv_email.setEnabled(false);
                tv_phone.setEnabled(false);
                //((Activity)getContext()).finish();
            } else {
                Toast.makeText(getContext(),server_message,Toast.LENGTH_LONG).show();
            }
        }
    }


}
