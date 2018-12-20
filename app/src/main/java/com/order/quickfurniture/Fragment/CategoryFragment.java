package com.order.quickfurniture.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.order.quickfurniture.Activity.ItemList;
import com.order.quickfurniture.Adapter.CategoryAdapter;
import com.order.quickfurniture.Adapter.SubCategoryAdapter;
import com.order.quickfurniture.Pojo.Category;
import com.order.quickfurniture.Pojo.SubCategory;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView lv_category,lv_s_category;
    ArrayList<Category> categoryArrayList;
    ArrayList<SubCategory> subcategoryArrayList;
    CategoryAdapter cAdapter;
    SubCategoryAdapter sAdapter;
    ProgressBar loader;
    String select_cat_name,select_cat_id;
    private OnFragmentInteractionListener mListener;

    public CategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryFragment newInstance(String param1, String param2) {
        CategoryFragment fragment = new CategoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        lv_category = (ListView) view.findViewById(R.id.category_list);
        lv_s_category = (ListView) view.findViewById(R.id.s_category_list);
        loader = (ProgressBar) view.findViewById(R.id.loader);
        getCategoryList();
        getSubcategoryList("");


        lv_s_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SubCategory items= (SubCategory) parent.getItemAtPosition(position);
                //  Toast.makeText(AdminUserList.this,users.getUser_name(),Toast.LENGTH_LONG).show();
                String sub_catid = items.getId();
                String sub_cat_name = items.getTitle();
                Intent intent = new Intent(getActivity(),ItemList.class);
                intent.putExtra("SUBCAT_ID",sub_catid);
                intent.putExtra("SUBCAT_NAME",sub_cat_name);
                startActivity(intent);
            }
        });

        lv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Category category= (Category) adapterView.getItemAtPosition(i);
                select_cat_id = category.getId();
                select_cat_name = category.getTitle();

                for (int k = 0; k < lv_category.getChildCount(); k++) {
                    if(i == k ){
                        lv_category.getChildAt(k).setBackgroundColor(getResources().getColor(R.color.bluebg));

                    }else{
                        lv_category.getChildAt(k).setBackgroundColor(getResources().getColor(R.color.colorWhite));
                    }
                }

                /*TextView text=(TextView) adapterView.getChildAt(i).findViewById(R.id.c_name);
                text.setTextColor(Color.parseColor("#00599d"));
                ImageView imageView=(ImageView) adapterView.getChildAt(i).findViewById(R.id.iv_letterView);
                imageView.setBackgroundColor(Color.parseColor("#00599d"));*/


                if(select_cat_name.contentEquals("All")){
                    getSubcategoryList("");

                }
                else {
                    getSubcategoryList(select_cat_id);
                }

            }
        });
        return view;
    }

    private void getCategoryList() {
        if(CheckInternet.getNetworkConnectivityStatus(getContext())) {
            categoryArrayList = new ArrayList<>();
            getcategoryList getcategoryList = new getcategoryList();
            getcategoryList.execute();
        }
        else {
            Constants.NointernetDialog(getActivity());
        }
    }
    private void getSubcategoryList(String category_id) {
        if(CheckInternet.getNetworkConnectivityStatus(getContext())) {
            subcategoryArrayList = new ArrayList<>();
            GetSubcategoryList getSubcategoryList = new GetSubcategoryList();
            getSubcategoryList.execute(category_id);
        }
        else {
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

    /*
     * GET CATEGORY LIST ASYNTASK*/
    private class getcategoryList extends AsyncTask<String, Void, Void> {

        private static final String TAG = "getcategoryList";
        int server_status;
        String server_message;

        @Override
        protected void onPreExecute() {

        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.CATEGORY_LIST;
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
                        .appendQueryParameter("date", "");
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
    {
    "categories": [
        {
            {
            "id": 1,
            "title": "categori 1",
            "photo": "http://applicationworld.net/furniture/admin/files/category/file1543628582872495135.png",
            "arabic_title": null,
            "is_enable": "Y",
            "created": "01-12-2018 01:32 AM",
            "modified": "2018-12-01T01:43:02+00:00"
        }
    ]
                    },*/

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    JSONArray categoryListArray = res.getJSONArray("categories");
                    if(categoryListArray.length()<=0){
                        server_message="No Category Found";

                    }
                    else{
                        server_status=1;
                        for (int i = 0; i < categoryListArray.length(); i++) {
                            JSONObject o_list_obj = categoryListArray.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            String title = o_list_obj.getString("title");
                            String photo = o_list_obj.getString("photo");
                            Category list1 = new Category(id,title,photo);
                            categoryArrayList.add(list1);
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
            if(server_status==1) {
                cAdapter = new CategoryAdapter(getActivity(),categoryArrayList );
                lv_category.setAdapter(cAdapter);
            }
            else{
                Constants.SomethingWrong(getActivity(),server_message);
            }
        }
    }

    /*
     * GET SUB CATEGORY LIST ASYNTASK*/
    private class GetSubcategoryList extends AsyncTask<String, Void, Void> {

        private static final String TAG = "getcategoryList";
        int server_status;
        String server_message;

        @Override
        protected void onPreExecute() {
            loader.setVisibility(View.VISIBLE);
        }
        @Override
        protected Void doInBackground(String... params) {


            try {
                InputStream in = null;
                int resCode = -1;
                String link = Constants.MAINURL + Constants.SUB_CATEGORY_LIST;
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
                        .appendQueryParameter("category_id", params[0]);
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
    {

    "subCategories": [
        {
            "id": 1,
            "category_id": 1,
            "title": " dfgfdgfdg",
            "arabic_title": null,
            "photo": "http://applicationworld.net/furniture/admin/files/category/file1543629326886795867.png",
            "is_enable": "Y",
            "created": "01-12-2018 01:49 AM",
            "modified": "2018-12-01T01:55:26+00:00"
        },
    ]
                    },*/

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    JSONArray categoryListArray = res.getJSONArray("subCategories");
                    if(categoryListArray.length()<=0){
                        server_message="No SubCategory Found";

                    }
                    else{
                        server_status=1;
                        for (int i = 0; i < categoryListArray.length(); i++) {
                            JSONObject o_list_obj = categoryListArray.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            String title = o_list_obj.getString("title");
                            String photo = o_list_obj.getString("photo");
                            SubCategory list1 = new SubCategory(id,title,photo);
                            subcategoryArrayList.add(list1);
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
            loader.setVisibility(View.GONE);
            if(server_status==1) {
                sAdapter = new SubCategoryAdapter(getActivity(),subcategoryArrayList );
                lv_s_category.setAdapter(sAdapter);
            }
            else{

            }
        }
    }


}
