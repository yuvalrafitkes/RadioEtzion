package com.ibm.mysampleapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;

public class Fragments_list extends AppCompatActivity {
    ProgressBar pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tab);
       // ShowAdapter adapter = new ShowAdapter(this);
        //((ListView)findViewById(R.id.list)).setAdapter(adapter);
    }
}
