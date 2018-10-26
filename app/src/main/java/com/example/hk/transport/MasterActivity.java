package com.example.hk.transport;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.hk.transport.Fragments.HomeFragment;
import com.example.hk.transport.Fragments.HomeSubFragments.GoBookingFragment;
import com.example.hk.transport.Fragments.MyPackageFragment;
import com.example.hk.transport.Fragments.NotificationFragment;
import com.example.hk.transport.Fragments.SettingFragment;
import com.example.hk.transport.Fragments.WalletFragment;
import com.example.hk.transport.Utilities.Common;

public class MasterActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    int lastFragItemSelected;
    FragmentManager manager;
    FragmentTransaction transaction;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    NavigationView navigationView;
    private boolean mToolBarNavigationListenerIsRegistered = false;
    TextView nameTV,emailTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.BLACK);

        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back_arrow));

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        manager = getFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.container, HomeFragment.getInstance());
        transaction.addToBackStack(null);
        transaction.commit();
        lastFragItemSelected = 0;

        navigationView.getMenu().getItem(0).setChecked(true);

        nameTV = navigationView.getHeaderView(0).findViewById(R.id.nameTV);
        nameTV.setText(Common.loginPojo.getFirstName()+" "+Common.loginPojo.getLastName());

        emailTV = navigationView.getHeaderView(0).findViewById(R.id.emailTV);
        emailTV.setText(Common.loginPojo.getEmailAddress());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.master, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            replaceFragment(0,HomeFragment.getInstance());
        } else if (id == R.id.nav_my_package) {
            replaceFragment(1, MyPackageFragment.getInstance());
        } else if (id == R.id.nav_notification) {
            replaceFragment(2, NotificationFragment.getInstance());
        } else if (id == R.id.nav_wallet) {
            replaceFragment(3, WalletFragment.getInstance());
        } else if (id == R.id.nav_setting) {
            replaceFragment(4, SettingFragment.getInstance());
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(int fragmentNo,Fragment fragment)
    {
        if(fragmentNo == lastFragItemSelected)
        {
            return;
        }
        else
        {
            enableViews(false);
            transaction = manager.beginTransaction();
            transaction.replace(R.id.container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            lastFragItemSelected = fragmentNo;
        }
    }

    public void changeTitle(String title)
    {
        getSupportActionBar().setTitle(title);
    }

    public void changeFragmentWithBack(Fragment fragment,int fragmentNumber)
    {
        transaction = manager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        lastFragItemSelected = fragmentNumber;
        //6 for GoBookingFragment
        //7 for AddPackageDetailFragment
        enableViews(true);
    }

    private void enableViews(boolean enable) {

        // To keep states of ActionBar and ActionBarDrawerToggle synchronized,
        // when you enable on one, you disable on the other.
        // And as you may notice, the order for this operation is disable first, then enable - VERY VERY IMPORTANT.
        if (enable) {
            //You may not want to open the drawer on swipe from the left in this case
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            // Remove hamburger
            toggle.setDrawerIndicatorEnabled(false);
            // Show back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
            // clicks are disabled i.e. the UP button will not work.
            // We need to add a listener, as in below, so DrawerToggle will forward
            // click events to this listener.
            if (!mToolBarNavigationListenerIsRegistered) {
                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(lastFragItemSelected == 6)
                        {
                            replaceFragment(0,HomeFragment.getInstance());
                        }
                        else if(lastFragItemSelected == 7)
                        {
                            changeFragmentWithBack(GoBookingFragment.getInstance(),6);
                        }
                    }
                });

                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {
            //You must regain the power of swipe for the drawer.
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

            // Remove back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            // Show hamburger
            toggle.setDrawerIndicatorEnabled(true);
            // Remove the/any drawer toggle listener
            toggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1)
        {
            HomeFragment.state = 1;
            replaceFragment(0, HomeFragment.getInstance());
        }
    }

    //Commit Added
}
