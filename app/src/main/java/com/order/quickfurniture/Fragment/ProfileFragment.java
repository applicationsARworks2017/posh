package com.order.quickfurniture.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputType;
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
import com.order.quickfurniture.R;
import com.order.quickfurniture.Util.Constants;

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
    String user_id,user_name,user_email,user_phone;
    Button login,signup;
    RelativeLayout profile_lay;
    ScrollView profile_scroll;
    TextView tv_fullname,tv_email,tv_phone,tv_address,tv_chage_password;
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
        user_phone = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_MOBILE, null);
        login = (Button)view.findViewById(R.id.login);
        signup = (Button)view.findViewById(R.id.signup);
        profile_lay=(RelativeLayout)view.findViewById(R.id.profile_lay);
        profile_scroll=(ScrollView)view.findViewById(R.id.profile_scroll);
        tv_fullname=(TextView)view.findViewById(R.id.tv_fullname);
        tv_email=(TextView)view.findViewById(R.id.tv_email);
        tv_phone=(TextView)view.findViewById(R.id.tv_phone);
        tv_address=(TextView)view.findViewById(R.id.tv_address);
        tv_chage_password=(TextView)view.findViewById(R.id.tv_password);
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
                        if(old_pas==new_pas){
                            SharedPreferences sharedPreferences = getContext().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0); // 0 - for private mode
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Constants.USER_PASSWORD, new_pas);
                            editor.commit();
                            //CheckServer();
                        }
                        else{
                            Toast.makeText(getContext(),"Incorrect password",Toast.LENGTH_LONG).show();
                        }
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
}
