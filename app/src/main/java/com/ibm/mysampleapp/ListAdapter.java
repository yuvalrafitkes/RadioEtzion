package com.ibm.mysampleapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item,null);
        String showName = radioList.get(position).vodName;
        //set names
        TextView txtShowName = view.findViewById(R.id.itemShow_Name);
        txtShowName.setText(initializeText(showName));
        ;
        //TODO = לקבל שמות שדרנים במידה ויעלו בכלל
//            TextView txtShowReporter = view.findViewById(R.id.itemShow_Reporter);
//        txtShowReporter.setText(radioList.get(position).);



        final ClsRadio current = radioList.get(position);
        TextView txt = new TextView(context);
        txt.setText(current.vodName);


        txtShowName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PlayerActivity.class);
                //URL
                String sendPath="http://be.repoai.com:5080/WebRTCAppEE/"+radioList.get(position).getFilePath();
                String sendName=radioList.get(position).getVodName();
                initializeText(sendName);
                Log.e("fileName", "onClick: "+sendPath);
                i.putExtra("url",sendPath);
                i.putExtra("urlName",initializeText(sendName));
                context.startActivity(i);
            }
        });

        return view;
    }

    public String initializeText (String txt){ // initializes the file's text to normal spaced string
        txt = txt.replace("_"," ");
        txt = txt.replace(".mp4","");
        return txt;
    }
}
