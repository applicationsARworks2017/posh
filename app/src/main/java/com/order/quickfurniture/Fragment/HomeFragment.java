package com.order.quickfurniture.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.order.quickfurniture.Adapter.CategoryAdapter;
import com.order.quickfurniture.Adapter.SlidingImage_Adapter;
import com.order.quickfurniture.Pojo.Category;
import com.order.quickfurniture.Pojo.HomeArrivals;
import com.order.quickfurniture.Pojo.HomeOffers;
import com.order.quickfurniture.Pojo.HomeTrending;
import com.order.quickfurniture.Pojo.ImageModel;
import com.order.quickfurniture.R;
import com.order.quickfurniture.Util.CheckInternet;
import com.order.quickfurniture.Util.Constants;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

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
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;
    CirclePageIndicator indicator;
    LinearLayout lin_offer_horizontal;
    LinearLayout lin_arrivals_horizontal;
    LinearLayout lin_trendings_horizontal;
    int offer_count,trending_count,arrivals_count;
    ArrayList<HomeOffers> offersArrayList;
    ArrayList<HomeTrending> trendingArrayList;
    ArrayList<HomeArrivals> arrivalsArrayList;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mPager = (ViewPager)view.findViewById(R.id.pager);
        indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        lin_offer_horizontal = (LinearLayout)view.findViewById(R.id.lin_offer_horizontal); 
        lin_arrivals_horizontal = (LinearLayout)view.findViewById(R.id.lin_arrivals_horizontal);
        lin_trendings_horizontal = (LinearLayout)view.findViewById(R.id.lin_trending_horizontal);
        getBannerList();
        getHomeList();
      //  gettextviews(100);
        return view;
    }

    private void getHomeList() {
        offersArrayList = new ArrayList<>();
        trendingArrayList = new ArrayList<>();
        arrivalsArrayList = new ArrayList<>();
        if(CheckInternet.getNetworkConnectivityStatus(getActivity())){
            HomeitemList homeitemList = new HomeitemList();
            homeitemList.execute();

        }
        else{
            Constants.NointernetDialog(getActivity());
        }
    }

    /*
    *
    * LinearLayout myRoot = (LinearLayout) findViewById(R.id.my_root);
LinearLayout a = new LinearLayout(this);
a.setOrientation(LinearLayout.HORIZONTAL);
a.addView(view1);
a.addView(view2);
a.addView(view3);
myRoot.addView(a);
    * */

    private void gettextviews(int i) {
        for(int j = 0;j<i;j++) {
            RelativeLayout a = new RelativeLayout(getActivity());
            LinearLayout.LayoutParams relativeParams = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                relativeParams = new LinearLayout.LayoutParams(new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            relativeParams.setMargins(10, 0, 0, 0);  // left, top, right, bottom
            a.setLayoutParams(relativeParams);
            TextView textView = new TextView(getActivity());
            textView.setId(j);
            a.addView(textView);
            textView.setText("Num"+j);
            lin_offer_horizontal.addView(a);
        }
    }

    private void getBannerList() {
        imageModelArrayList = new ArrayList<>();
        //imageModelArrayList = populateList();

        if(CheckInternet.getNetworkConnectivityStatus(getContext())){
            GetbannerList getbannerList = new GetbannerList();
            getbannerList.execute();
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

    /*
     * GET BANNER LIST ASYNTASK*/
    private class GetbannerList extends AsyncTask<String, Void, Void> {

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
                String link = Constants.MAINURL + Constants.BANNERLIST;
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
    "banners": [
        {
            "id": 1,
            "title": "categori 1",
            "photo": "http://applicationworld.net/furniture/files/banner/file15443251471643586084.png",
            "is_enable": "Y",
            "is_trash": 0,
            "created": "07-12-2018 02:05 AM",
            "modified": "2018-12-09T03:12:27+00:00"
        },
        {
            "id": 2,
            "title": "banner2",
            "photo": "http://applicationworld.net/furniture/files/banner/file1544325186363818227.png",
            "is_enable": "Y",
            "is_trash": 0,
            "created": "09-12-2018 03:13 AM",
            "modified": "2018-12-09T03:13:06+00:00"
        },      },*/

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    JSONArray categoryListArray = res.getJSONArray("banners");
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
                            ImageModel list1 = new ImageModel(id,title,photo);
                            imageModelArrayList.add(list1);
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
                init();

            }
            else{
                Constants.SomethingWrong(getActivity(),server_message);
            }
        }
    }

    /*
     * GET home item LIST ASYNTASK*/
    private class HomeitemList extends AsyncTask<String, Void, Void> {

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
                String link = Constants.MAINURL + Constants.HOME_ITEM;
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
                * "finalArr": {
                    "Offers": [
            {
                "id": 1,
                "category_id": 1,
                "sub_category_id": 1,
                "item_type_id": 6,
                "brand_id": null,
                "name": "gdsg fdg",
                "price": 100,
                "discount": 10,
                "actual_price": 1000,
                "image": "http://applicationworld.net/furniture/files/items/file15443360301004903528.jpg",
                "created": "01-12-2018 02:22 AM",
                "modified": "2018-12-09T06:13:50+00:00",
                "item_type": {
                    "id": 6,
                    "title": "Offers",
                    "is_enable": "Y",
                    "created": "2018-12-12T02:24:19+00:00",
                    "modified": "2018-12-19T17:52:50+00:00"
                },
                *
                * ,*/

                if (response != null && response.length() > 0) {
                    JSONObject res = new JSONObject(response.trim());
                    // server_status=res.getInt("status");
                    JSONObject honeJon = res.getJSONObject("finalArr");
                    JSONArray Offersarray = honeJon.getJSONArray("Offers");
                    JSONArray Trendingarray = honeJon.getJSONArray("Trending");
                    JSONArray Arrivalarray = honeJon.getJSONArray("New Arrival");

                    if(Offersarray.length()>0){
                        offer_count = Offersarray.length();
                        for (int i = 0; i < Offersarray.length(); i++) {
                            JSONObject o_list_obj = Offersarray.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            String category_id = o_list_obj.getString("category_id");
                            String sub_category_id = o_list_obj.getString("sub_category_id");
                            String name = o_list_obj.getString("name");
                            String price = o_list_obj.getString("price");
                            String image = o_list_obj.getString("image");
                            HomeOffers list1 = new HomeOffers(id, category_id, sub_category_id,name,price,image);
                            offersArrayList.add(list1);
                        }
                    }
                    if(Trendingarray.length()>0){
                        trending_count = Trendingarray.length();
                        for (int i = 0; i < Trendingarray.length(); i++) {
                            JSONObject o_list_obj = Trendingarray.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            String category_id = o_list_obj.getString("category_id");
                            String sub_category_id = o_list_obj.getString("sub_category_id");
                            String name = o_list_obj.getString("name");
                            String price = o_list_obj.getString("price");
                            String image = o_list_obj.getString("image");
                            HomeTrending list1 = new HomeTrending(id, category_id, sub_category_id,name,price,image);
                            trendingArrayList.add(list1);
                        }
                    }
                    if(Arrivalarray.length()>0){
                        arrivals_count = Arrivalarray.length();
                        for (int i = 0; i < Arrivalarray.length(); i++) {
                            JSONObject o_list_obj = Arrivalarray.getJSONObject(i);
                            String id = o_list_obj.getString("id");
                            String category_id = o_list_obj.getString("category_id");
                            String sub_category_id = o_list_obj.getString("sub_category_id");
                            String name = o_list_obj.getString("name");
                            String price = o_list_obj.getString("price");
                            String image = o_list_obj.getString("image");
                            HomeArrivals list1 = new HomeArrivals(id, category_id, sub_category_id,name,price,image);
                            arrivalsArrayList.add(list1);
                        }
                    }

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
            if(offer_count>0){
                CreateOffersection(offer_count);
            }
            if(trending_count>0){
                CreateTrendingsection(trending_count);
            }
            if(arrivals_count>0){
                CreateArrivalsection(arrivals_count);
            }
        }
    }

    private void CreateArrivalsection(int arrivals_count) {
        for(int j = 0;j<arrivals_count;j++) {
            LinearLayout a = new LinearLayout(getActivity());
            a.setOrientation(LinearLayout.VERTICAL);
            a.setGravity(Gravity.CENTER_HORIZONTAL);
            RelativeLayout.LayoutParams relativeParams = null;
            RelativeLayout.LayoutParams relativeParams_image = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

                relativeParams = new RelativeLayout.LayoutParams(new LinearLayout.LayoutParams(
                        220,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                relativeParams_image = new RelativeLayout.LayoutParams(new LinearLayout.LayoutParams(
                        200,
                        200));
            }
            relativeParams.setMargins(10, 5, 0, 5);  // left, top, right, bottom
            a.setBackgroundColor(Color.WHITE);
            a.setLayoutParams(relativeParams);
            ImageView imageView = new ImageView(getActivity());
            imageView.setBackgroundColor(Color.BLACK);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(relativeParams_image);
            a.addView(imageView);
            if(arrivalsArrayList.get(j).getImage().contains(",")) {
                List<String> imageList = Arrays.asList(arrivalsArrayList.get(j).getImage().split(","));

                if(imageList.get(1)== null || imageList.get(1) == "" || imageList.get(1).contentEquals("") ){
                    imageView.setImageResource(R.drawable.error);
                }
                else {
                    Picasso.with(getActivity()).load(imageList.get(1)).into(imageView);
                }
            }
            else{
                if(arrivalsArrayList.get(j).getImage()== null || arrivalsArrayList.get(j).getImage() == "" ||
                        arrivalsArrayList.get(j).getImage().contentEquals("") ){
                    imageView.setImageResource(R.drawable.error);

                }
                else {
                    Picasso.with(getActivity()).load(arrivalsArrayList.get(j).getImage()).into(imageView);
                }
            }

            TextView textView = new TextView(getActivity());
            textView.setId(j);
            textView.setSingleLine();
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setText(arrivalsArrayList.get(j).getName());
            a.addView(textView);


            TextView textView_cost = new TextView(getActivity());
            textView_cost.setId(j);
            textView_cost.setSingleLine();
            textView_cost.setGravity(Gravity.CENTER_HORIZONTAL);
            textView_cost.setText("\u20B9 "+arrivalsArrayList.get(j).getPrice());
            //textView_cost.setLayoutParams(tvtime_params);
            a.addView(textView_cost);


            lin_arrivals_horizontal.addView(a);
        }
    }

    private void CreateTrendingsection(int trending_count) {
        for(int j = 0;j<trending_count;j++) {
            LinearLayout a = new LinearLayout(getActivity());
            a.setOrientation(LinearLayout.VERTICAL);
            a.setGravity(Gravity.CENTER_HORIZONTAL);
            RelativeLayout.LayoutParams relativeParams = null;
            RelativeLayout.LayoutParams relativeParams_image = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

                relativeParams = new RelativeLayout.LayoutParams(new LinearLayout.LayoutParams(
                        220,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                relativeParams_image = new RelativeLayout.LayoutParams(new LinearLayout.LayoutParams(
                        200,
                        200));
            }
            relativeParams.setMargins(10, 5, 0, 5);  // left, top, right, bottom
            a.setBackgroundColor(Color.WHITE);
            a.setLayoutParams(relativeParams);
            ImageView imageView = new ImageView(getActivity());
            imageView.setBackgroundColor(Color.BLACK);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(relativeParams_image);
            a.addView(imageView);
            if(trendingArrayList.get(j).getImage().contains(",")) {
                List<String> imageList = Arrays.asList(trendingArrayList.get(j).getImage().split(","));
                if(imageList.get(1)== null || imageList.get(1) == "" || imageList.get(1).contentEquals("") ){
                    imageView.setImageResource(R.drawable.error);

                }
                else {
                    Picasso.with(getActivity()).load(imageList.get(1)).into(imageView);
                }
            }
            else{
                if(trendingArrayList.get(j).getImage()== null || trendingArrayList.get(j).getImage() == "" || trendingArrayList.get(j).getImage().contentEquals("") ){
                    imageView.setImageResource(R.drawable.error);

                }
                else {
                    Picasso.with(getActivity()).load(trendingArrayList.get(j).getImage()).into(imageView);
                }
            }

            TextView textView = new TextView(getActivity());
            textView.setId(j);
            textView.setSingleLine();
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setText(trendingArrayList.get(j).getName());
            a.addView(textView);


            TextView textView_cost = new TextView(getActivity());
            textView_cost.setId(j);
            textView_cost.setSingleLine();
            textView_cost.setGravity(Gravity.CENTER_HORIZONTAL);
            textView_cost.setText("\u20B9 "+offersArrayList.get(j).getPrice());
            //textView_cost.setLayoutParams(tvtime_params);
            a.addView(textView_cost);


            lin_trendings_horizontal.addView(a);
        }
    }

    private void CreateOffersection(int offercount) {
        for(int j = 0;j<offercount;j++) {
            LinearLayout a = new LinearLayout(getActivity());
            a.setOrientation(LinearLayout.VERTICAL);
            a.setGravity(Gravity.CENTER_HORIZONTAL);
            RelativeLayout.LayoutParams relativeParams = null;
            RelativeLayout.LayoutParams relativeParams_image = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

                relativeParams = new RelativeLayout.LayoutParams(new LinearLayout.LayoutParams(
                        220,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                relativeParams_image = new RelativeLayout.LayoutParams(new LinearLayout.LayoutParams(
                        200,
                        200));
            }
            relativeParams.setMargins(10, 5, 0, 5);  // left, top, right, bottom
            a.setBackgroundColor(Color.WHITE);
            a.setLayoutParams(relativeParams);
            ImageView imageView = new ImageView(getActivity());
            imageView.setBackgroundColor(Color.BLACK);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setLayoutParams(relativeParams_image);
            a.addView(imageView);
            if(offersArrayList.get(j).getImage().contains(",")) {
                List<String> imageList = Arrays.asList(offersArrayList.get(j).getImage().split(","));
                if(imageList.get(1)== null || imageList.get(1) == "" || imageList.get(1).contentEquals("") ){
                    imageView.setImageResource(R.drawable.error);

                }
                else{
                Picasso.with(getActivity())
                        .load(imageList.get(1)).into(imageView);
                }
            }
            else{
                if(offersArrayList.get(j).getImage()== null || offersArrayList.get(j).getImage() == "" ||
                        offersArrayList.get(j).getImage().contentEquals("") ){
                    imageView.setImageResource(R.drawable.error);

                }
                else {
                    Picasso.with(getActivity()).load(offersArrayList.get(j).getImage()).into(imageView);
                }
            }

            TextView textView = new TextView(getActivity());
            textView.setId(j);
            textView.setSingleLine();
            textView.setTextColor(Color.BLACK);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setText(offersArrayList.get(j).getName());
            a.addView(textView);


            TextView textView_cost = new TextView(getActivity());
            textView_cost.setId(j);
            textView_cost.setSingleLine();
            textView_cost.setGravity(Gravity.CENTER_HORIZONTAL);
            textView_cost.setText("\u20B9 "+offersArrayList.get(j).getPrice());
            //textView_cost.setLayoutParams(tvtime_params);
            a.addView(textView_cost);


            lin_offer_horizontal.addView(a);
        }
    }

    private void init() {

        mPager.setAdapter(new SlidingImage_Adapter(getActivity(),imageModelArrayList));

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
}
