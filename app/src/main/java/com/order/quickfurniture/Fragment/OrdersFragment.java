package com.order.quickfurniture.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.order.quickfurniture.Activity.CartActivity;
import com.order.quickfurniture.Adapter.MyOrdersAdapter;
import com.order.quickfurniture.Adapter.NewCartAdapter;
import com.order.quickfurniture.Pojo.Cartlist;
import com.order.quickfurniture.Pojo.MyOrders;
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
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrdersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrdersFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;
    String user_id;
    ArrayList<MyOrders> arrayList;
    MyOrdersAdapter c_Adapter;
    private OnFragmentInteractionListener mListener;

    public OrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
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
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.order_swipe);
        listView = (ListView) view.findViewById(R.id.list_order);
        user_id =getActivity().getSharedPreferences(Constants.SHAREDPREFERENCE_KEY, 0).getString(Constants.USER_ID, null);

        getOrderDetails();
        return view;
    }

    private void getOrderDetails() {

        arrayList = new ArrayList<>();
        if(CheckInternet.getNetworkConnectivityStatus(getContext())){
            GetOrderList getOrderList = new GetOrderList();
            getOrderList.execute(user_id);
        }
        else{
            Constants.NointernetDialog(getActivity());
        }
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



    private class GetOrderList extends AsyncTask<String, Void, Void> {

        private static final String TAG = "getAdressList";
        int server_status;
        String server_message;
        ProgressDialog progressDialog = null;

        @Override
        protected void onPreExecute() {
            if(progressDialog == null) {
                progressDialog = ProgressDialog.show(getActivity(), "Loading Items", "Please wait...");
            }
        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.ORDER_LIST;
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
                *  "cartSummaries": [
        {
            "id": 17,
            "user_id": 9,
            "address_id": 9,
            "sub_total": 32099.97,
            "total_tax": 5777.99,
            "shipping_cost": 12,
            "total": 37889.96,
            "status": "Ordered",
            "created": "2019-03-10T07:58:54+00:00",
            "modified": "2019-03-10T07:58:54+00:00",
            "tax_values": [
                {
                    "id": 32,
                    "cart_summary_id": 17,
                    "tax_id": 1,
                    "tax_value": 2889,
                    "created": null,
                    "modified": null
                },
                {
                    "id": 33,
                    "cart_summary_id": 17,
                    "tax_id": 2,
                    "tax_value": 2889,
                    "created": null,
                    "modified": null
                }
            ],
            "carts": [
                {
                    "id": 76,
                    "cart_summary_id": 17,
                    "user_id": 9,
                    "item_id": 7,
                    "quentity": 1,
                    "price": 9999.99,
                    "created": "2019-03-03T07:19:09+00:00",
                    "modified": "2019-03-03T07:19:09+00:00"
                },
                {
                    "id": 37,
                    "cart_summary_id": 17,
                    "user_id": 9,
                    "item_id": 7,
                    "quentity": 1,
                    "price": 9999.99,
                    "created": "2019-02-21T04:12:20+00:00",
                    "modified": "2019-02-21T04:12:20+00:00"
                },
                {
                    "id": 38,
                    "cart_summary_id": 17,
                    "user_id": 9,
                    "item_id": 7,
                    "quentity": 1,
                    "price": 9999.99,
                    "created": "2019-02-21T04:19:34+00:00",
                    "modified": "2019-02-21T04:19:34+00:00"
                },
                {
                    "id": 62,
                    "cart_summary_id": 17,
                    "user_id": 9,
                    "item_id": 4,
                    "quentity": 1,
                    "price": 100,
                    "created": "2019-02-27T04:40:12+00:00",
                    "modified": "2019-02-27T04:40:12+00:00"
                },
                {
                    "id": 71,
                    "cart_summary_id": 17,
                    "user_id": 9,
                    "item_id": 8,
                    "quentity": 1,
                    "price": 2000,
                    "created": "2019-02-27T08:59:33+00:00",
                    "modified": "2019-02-27T08:59:33+00:00"
                },
                {
                    "id": 82,
                    "cart_summary_id": 17,
                    "user_id": 9,
                    "item_id": 2,
                    "quentity": 1,
                    "price": 0,
                    "created": "2019-03-04T15:38:09+00:00",
                    "modified": "2019-03-04T15:38:09+00:00"
                }
            ],
            "address": {
                "id": 9,
                "user_id": 9,
                "full_name": "Amaresh Samantaray",
                "mobile": "9090403051",
                "pincode_id": 1,
                "address": "231",
                "locality": "hennur",
                "landmark": "basappa complex",
                "city": "Bangalore",
                "email": "amaresh@gmail.com",
                "is_default": 0,
                "is_trash": 0,
                "created": "2019-02-18T14:04:59+00:00",
                "modified": "2019-02-18T14:04:59+00:00"
            },
            "user": {
                "id": 9,
                "name": "amaresh",
                "password": "1234",
                "email": "avinashpathak8@gmail.com",
                "mobile": "9090403051",
                "photo": null,
                "created": "2019-02-18T13:39:16+00:00",
                "modified": "2019-03-03T07:38:58+00:00",
                "usertype": "user",
                "fcm_id": "",
                "ios_fcm_id": null
            }
        },*/

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    JSONArray ListArray = res.getJSONArray("cartSummaries");
                    if(ListArray.length()<=0){
                        server_message="No Items Found";

                    }
                    else{
                        server_status=1;
                        for (int i = 0; i < ListArray.length(); i++) {
                            JSONObject o_list_obj = ListArray.getJSONObject(i);
                            String c_id = o_list_obj.getString("id");
                            String user_id = o_list_obj.getString("user_id");
                            String sub_total = o_list_obj.getString("sub_total");
                            String total_tax = o_list_obj.getString("total_tax");
                            String shipping_cost = o_list_obj.getString("shipping_cost");
                            String total = o_list_obj.getString("total");
                            String status = o_list_obj.getString("status");
                            String _id = null,item_id = null,price = null,item_name=null,item_photo=null;

                           /* JSONArray i_list=o_list_obj.getJSONArray("carts");
                            for(int j = 0; j<i_list.length(); j++){
                                JSONObject b_list_obj = ListArray.getJSONObject(j);
                                // _id = b_list_obj.getString("id");
                             //    item_id = b_list_obj.getString("item_id");
                               //  price = b_list_obj.getString("price");
                            }*/

                            MyOrders list1 = new MyOrders(c_id,sub_total,total_tax,shipping_cost,total,status,
                                    _id,item_id,price,item_name,item_photo);
                            arrayList.add(list1);
                        }
                    }
                }
                return null;
            } catch (Exception exception) {
                Log.e(TAG, "LoginAsync : doInBackground", exception);
                server_message="Network Error";
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void data) {
            super.onPostExecute(data);
            progressDialog.dismiss();
            if(server_status==1) {
                c_Adapter = new MyOrdersAdapter(getActivity(),arrayList );
                listView.setAdapter(c_Adapter);
            }
            else{
                Constants.SomethingWrong(getActivity(),server_message);
            }
        }
    }

}
