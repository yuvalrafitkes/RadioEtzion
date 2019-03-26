package com.ibm.mysampleapp;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    ImageButton btnPlay,btnNext,btnPre,btnPause,btnFavorits,btnFavoritsOn,btnStop;
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
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnPre = (ImageButton) findViewById(R.id.btnPre);
        btnPause = (ImageButton)findViewById(R.id.btnPause);
        btnStop = (ImageButton)findViewById(R.id.btnStop);

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
        //Bundle bundle = i.getExtras();

        //MyProg = bundle.getParcelableArrayList("programs");


        String url = "http://be.repoai.com:5080/WebRTCAppEE/streams/home/עציון_בית_אקשטיין_לומדים_אחרת.mp4"; // your URL here
        final MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare(); // might take long! (for buffering, etc)
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    mediaPlayer.start();

                    btnPlay.setVisibility(View.INVISIBLE);
                    btnPause.setVisibility(View.VISIBLE);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                btnPause.setVisibility(View.INVISIBLE);
                btnPlay.setVisibility(View.VISIBLE);
            }
        });

    }
}
