package com.order.quickfurniture.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.order.quickfurniture.Fragment.CategoryFragment;
import com.order.quickfurniture.Fragment.HomeFragment;
import com.order.quickfurniture.Fragment.OrdersFragment;
import com.order.quickfurniture.Fragment.ProfileFragment;
import com.order.quickfurniture.R;

public class HomeActivity extends AppCompatActivity {
    RelativeLayout notificationCount1,badge_layout2;
    public BottomNavigationView mBottomNav;
    public int mSelectedItem;
    private static final String SELECTED_ITEM = "arg_selected_item";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        notificationCount1 = (RelativeLayout) findViewById(R.id.relative_layout1);
        badge_layout2 = (RelativeLayout) findViewById(R.id.badge_layout2);
        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                selectFragment(item);
                return true;
            }
        });

        MenuItem selectedItem;
        if (savedInstanceState != null) {
            mSelectedItem = savedInstanceState.getInt(SELECTED_ITEM, 0);
            selectedItem = mBottomNav.getMenu().findItem(mSelectedItem);
        } else {
            selectedItem = mBottomNav.getMenu().getItem(0);
        }
        selectFragment(selectedItem);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        final MenuItem item1 = menu.findItem(R.id.actionbar_item);
        MenuItemCompat.setActionView(item1, R.layout.notification_update_count_layout);
        notificationCount1 = (RelativeLayout) MenuItemCompat.getActionView(item1);
        badge_layout2 = (RelativeLayout)item1.getActionView();

        badge_layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(item1);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SELECTED_ITEM, mSelectedItem);
        super.onSaveInstanceState(outState);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item1) {
        switch (item1.getItemId()) {


            case R.id.actionbar_item:
                Intent i = new Intent(HomeActivity.this, CartActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item1);
        }
    }

    private void selectFragment(MenuItem item) {
        Fragment frag = null;

        switch (item.getItemId()) {
            case R.id.home:
                // Visitors Exit
                frag=new HomeFragment();
                //item.setTitle(R.string.home_arabic);
                item.setCheckable(true);
                break;

            case R.id.category:
                frag=new CategoryFragment();
                item.setCheckable(true);
                break;


            case R.id.profile:
                frag=new ProfileFragment();
                item.setCheckable(true);
                break;

                case R.id.track:
                frag=new OrdersFragment();
                item.setCheckable(true);
                break;

            default:
                frag=new HomeFragment();
                item.setCheckable(true);

        }
        mSelectedItem = item.getItemId();

        // uncheck the other items.
        /*for (int i = 0; i< mBottomNav.getMenu().size(); i++) {
            MenuItem menuItem = mBottomNav.getMenu().getItem(i);
            menuItem.setChecked(menuItem.getItemId() == item.getItemId());
        }*/

       // updateToolbarText(item.getTitle());

        if (frag != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, frag, frag.getTag());
            ft.commit();
        }
    }
}
