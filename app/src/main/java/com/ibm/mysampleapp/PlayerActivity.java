package com.ibm.mysampleapp;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    Button btnPause,btnNext,btnPre;
    String progName;
    TextView txtProgram;
    SeekBar seekBar;
    Context context;

    static MediaPlayer mediaPlayer;
    int position;

    ArrayList MyProg;
    Thread updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        setPointer();
    }

    private void setPointer() {
        this.context = this;
        btnNext = (Button)findViewById(R.id.btnNext);
        btnPause = (Button)findViewById(R.id.btnPause);
        btnPre = (Button) findViewById(R.id.btnPre);

        txtProgram = (TextView)findViewById(R.id.txtProgram);
        seekBar = (SeekBar)findViewById(R.id.seekBar);

        updateSeekBar = new Thread(){
            @Override
            public void run() {
                int totalDuration =  mediaPlayer.getDuration();
                int currentPosition = 0;

                while (currentPosition<totalDuration) {
                try{
                    sleep(500);
                    currentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                }
            }
        };

        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        MyProg = bundle.getParcelableArrayList("programs");


        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v instanceof  ImageButton){
                    ImageButton btnPlay = (ImageButton) v;
                    btnPlay.setImageResource(R.drawable.ic_play);
                }
            }
        });
    }
}
