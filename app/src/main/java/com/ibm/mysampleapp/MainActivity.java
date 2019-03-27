package com.ibm.mysampleapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.push.DeviceRegistrationResult;
import com.google.firebase.FirebaseApp;
import com.ibm.mobilefirstplatform.clientsdk.android.core.api.BMSClient;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPush;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushException;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushResponseListener;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPPushNotificationListener;
import com.ibm.mobilefirstplatform.clientsdk.android.push.api.MFPSimplePushNotification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class MainActivity extends AppCompatActivity implements Tab.OnFragmentInteractionListener, Tab1.OnFragmentInteractionListener, Tab2.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {
    private MFPPush push;
    private MFPPushNotificationListener notificationListener;
    TabLayout tabLayout;
    Context context;
    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_main);
        setPointer();
        navigationBar();


        // Core SDK must be initialized to interact with Bluemix Mobile services.
        BMSClient.getInstance().initialize(getApplicationContext(), BMSClient.REGION_US_SOUTH);



        /*
         * Initialize the Push Notifications client SDK with the App Guid and Client Secret from your Push Notifications service instance on Bluemix.
         * This enables authenticated interactions with your Push Notifications service instance.
         */
        push = MFPPush.getInstance();
        push.initialize(getApplicationContext(), getString(R.string.pushAppGuid), getString(R.string.pushClientSecret));

        /*
         * Attempt to register your Android device with your Bluemix Push Notifications service instance.
         * Developers should put their user ID as the first argument.
         */
        push.registerDeviceWithUserId("YOUR_USER_ID", new MFPPushResponseListener<String>() {

            @Override
            public void onSuccess(String response) {

                // Split response and convert to JSON object to display User ID confirmation from the backend.
                String[] resp = response.split("Text: ");
                String userId = "";
                try {
                    org.json.JSONObject responseJSON = new org.json.JSONObject(resp[1]);
                    userId = responseJSON.getString("userId");
                } catch (org.json.JSONException e) {
                    e.printStackTrace();
                }

                android.util.Log.i("YOUR_TAG_HERE", "Successfully registered for Bluemix Push Notifications with USER ID: " + userId);
            }

            @Override
            public void onFailure(MFPPushException ex) {

                String errLog = "Error registering for Bluemix Push Notifications: ";
                String errMessage = ex.getErrorMessage();
                int statusCode = ex.getStatusCode();

                // Create an error log based on the response code and returned error message.
                if (statusCode == 401) {
                    errLog += "Cannot authenticate successfully with Bluemix Push Notifications service instance. Ensure your CLIENT SECRET is correct.";
                } else if (statusCode == 404 && errMessage.contains("Push GCM Configuration")) {
                    errLog += "Your Bluemix Push Notifications service instance's GCM/FCM Configuration does not exist.\n" +
                            "Ensure you have configured GCM/FCM Push credentials on your Bluemix Push Notifications dashboard correctly.";
                } else if (statusCode == 404) {
                    errLog += "Cannot find Bluemix Push Notifications service instance, ensure your APP GUID is correct.";
                } else if (statusCode >= 500) {
                    errLog += "Bluemix and/or the Bluemix Push Notifications service are having problems. Please try again later.";
                } else if (statusCode == 0) {
                    errLog += "Request to Bluemix Push Notifications service instance timed out. Ensure your device is connected to the Internet.";
                }

                android.util.Log.e("YOUR_TAG_HERE", errLog);
            }
        });

        // A notification listener is needed to handle any incoming push notifications within the Android application.
        notificationListener = new MFPPushNotificationListener() {

            @Override
            public void onReceive(final MFPSimplePushNotification message) {
                // TODO: Process the message and add your logic here.
                android.util.Log.i("YOUR_TAG_HERE", "Received a push notification: " + message.toString());
                runOnUiThread(new Runnable() {
                    public void run() {
                        android.app.DialogFragment fragment = PushReceiverFragment.newInstance("Push notification received", message.getAlert());
                        fragment.show(getFragmentManager(), "dialog");
                    }
                });
            }
        };


    }

    private void navigationBar() {
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new Tab()).commit();
                break;
            case R.id.nav_contact:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Conact_Us_Fragment()).commit();
                break;

            case R.id.nav_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = "Radio etzion";
                String shareSub = "RADIO ETZION";
                intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                intent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(intent,"SHARE ME"));
                break;
            case R.id.nav_we:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new We_Fragment()).commit();

            case R.id.nav_exit:

                final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure you want to exit and miss all the fun?");
                builder.setCancelable(true);
                builder.setNegativeButton("Stay!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Take me out!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setPointer() {
        this.context=this;
        //set url (backendless url)
        Backendless.setUrl("https://api.backendless.com");
        //init the backendless by APP ID, API KEY
        Backendless.initApp( context, "2D5E6DA5-6B22-F84B-FFFD-67F33605D300", "2AE60844-6F42-4417-FFDE-44CA6B050B00" );
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("תוכניות"));
        tabLayout.addTab(tabLayout.newTab().setText("מועדפים"));
        tabLayout.addTab(tabLayout.newTab().setText("עדכונים"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        List<String> channels = new ArrayList<>(); // all channels for sending push notifications
        channels.add( "default" ); // default channel
        Backendless.Messaging.registerDevice(channels, new AsyncCallback<DeviceRegistrationResult>() { // registration of device to push notifications
            @Override
            public void handleResponse(DeviceRegistrationResult response) {
                //TODO - remove after checking if it works perfectly
                Toast.makeText( context, "Device registered!",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                //TODO - remove after checking if it works perfectly
                Toast.makeText( context, "Error registering " + fault.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
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


    @Override
    public void onResume() {
        super.onResume();
        // Enable the Push Notifications client SDK to listen for push notifications using the predefined notification listener.
        if (push != null) {
            push.listen(notificationListener);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        if (push != null) {
            push.hold();
        }
    }
}
