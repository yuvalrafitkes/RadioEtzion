package com.ibm.mysampleapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.backendless.Backendless;

import java.util.ArrayList;
import java.util.List;

import static com.ibm.mysampleapp.Defaults.API_KEY;
import static com.ibm.mysampleapp.Defaults.APPLICATION_ID;


public class MainActivity extends AppCompatActivity implements Tab.OnFragmentInteractionListener, Tab1.OnFragmentInteractionListener, Tab2.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {
    TabLayout tabLayout;
    private DrawerLayout drawerLayout;
    boolean havePermission=false;

    //REQUEST CODES
    final private int WAKE_LOCK_REQCODE=100;

    final private int WRITE_EXTERNAL_STORAGE_REQCODE=102;
    final private int WIFI_REQCODE=103;

    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        checkForPermissions();
        setPointer();
        navigationBar();
    }

    private void checkForPermissions() {
        this.context=getApplicationContext();
        List<String> listPermissionNeeded = new ArrayList<>();
        int WakeLockPerm = ContextCompat.checkSelfPermission(context, Manifest.permission.WAKE_LOCK);
        int getAccountPerm = ContextCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS);
        int useWriteExternalPerm = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int useWifiPerm = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_WIFI_STATE);
        //check if we have permission
        if (WakeLockPerm != PackageManager.PERMISSION_GRANTED) {
            listPermissionNeeded.add(Manifest.permission.WAKE_LOCK);
        }
        if (getAccountPerm != PackageManager.PERMISSION_GRANTED) {
            listPermissionNeeded.add(Manifest.permission.GET_ACCOUNTS);
        }
        if (useWriteExternalPerm != PackageManager.PERMISSION_GRANTED) {
            listPermissionNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (useWifiPerm != PackageManager.PERMISSION_GRANTED) {
            listPermissionNeeded.add(Manifest.permission.ACCESS_WIFI_STATE);
        }

        if (!listPermissionNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]),WAKE_LOCK_REQCODE );
            ActivityCompat.requestPermissions(this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]),WIFI_REQCODE );
            ActivityCompat.requestPermissions(this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]),WRITE_EXTERNAL_STORAGE_REQCODE );
        } else {
            havePermission = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case WAKE_LOCK_REQCODE: //this checks the specific permission explicitly
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, R.string.perm_deny, Toast.LENGTH_SHORT).show();
                    return;
                }
            case WIFI_REQCODE:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, R.string.perm_deny, Toast.LENGTH_SHORT).show();
                    return;
                }
            case WRITE_EXTERNAL_STORAGE_REQCODE:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, R.string.perm_deny, Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }
    }
    

    private void navigationBar() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.bringToFront();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_contact:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Conact_Us_Fragment()).commit();
                break;
            case R.id.nav_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = "Radio etzion";
                String shareSub = "RADIO ETZION";
                intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, "SHARE ME"));
                break;
            case R.id.nav_we:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new We_Fragment()).commit();
                break;
            case R.id.nav_exit:
                alertDialogMassege();
            case R.id.nav_chat:
                Intent chatIntent = new Intent(context,StartChatActivity.class);
                startActivity(chatIntent);
                break;
        }
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }



    private void setPointer() {
        Backendless.initApp(context, APPLICATION_ID, API_KEY);

        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_shows));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_events));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_chat));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = findViewById(R.id.viewPager);
        PagerAdapter adapter = new com.ibm.mysampleapp.PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private void alertDialogMassege() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.AD_txt);
        builder.setCancelable(true);

        builder.setPositiveButton(
                R.string.AD_stay,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder.setNegativeButton(
                R.string.AD_leave,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }



}




