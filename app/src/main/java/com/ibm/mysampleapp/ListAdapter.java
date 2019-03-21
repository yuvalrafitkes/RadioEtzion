package com.ibm.mysampleapp;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends BaseAdapter{

    private Context context;
    private List<ClsRadio> radioList;


    public ListAdapter(Context context, List<ClsRadio> radioList) {
        this.context = context;
        this.radioList = radioList;
    }

    @Override
    public int getCount() {
        return radioList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ClsRadio current = radioList.get(position);
        TextView txt = new TextView(context);
        txt.setText(current.vodName);

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PlayerActivity.class);
                i.putExtra("url", current.getFilePath());
                context.startActivity(i);
            }
        });

        return txt;
    }
}
