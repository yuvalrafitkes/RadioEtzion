package com.ibm.mysampleapp;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class PlayerActivity extends AppCompatActivity {
    ImageButton btnPlay,btnNext,btnPre,btnPause,btnFavorits,btnFavoritsOn,btnStop;
    String progName;
    TextView txtProgram;
    SeekBar seekBar;
    Context context;
    boolean isWorking;
    int length = 0;
    ListAdapter listAdapter;
    String soundPath;
    String namePath;

    static MediaPlayer mediaPlayer;
    int position;

    ArrayList MyProg;
    Thread updateSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Intent intent = getIntent();
        soundPath=intent.getStringExtra("url");
        namePath=intent.getStringExtra("urlName");
        setPointer();
    }

    private void setPointer() {
        this.context = this;
        btnNext =  findViewById(R.id.btnNext);
        btnPlay =  findViewById(R.id.btnPlay);
        btnPre =  findViewById(R.id.btnPre);
        btnPause = findViewById(R.id.btnPause);
        btnStop = findViewById(R.id.btnStop);

        txtProgram = findViewById(R.id.txtProgram);
        seekBar = findViewById(R.id.seekBar);

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
            mediaPlayer.setDataSource(soundPath);
            txtProgram.setText(namePath);

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
                    mediaPlayer.seekTo(length);
                    mediaPlayer.start();
                   btnPlay.setVisibility(View.INVISIBLE);
                    btnPause.setVisibility(View.VISIBLE);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();

                seekBar.getProgress();
                length=mediaPlayer.getCurrentPosition();
                btnPause.setVisibility(View.INVISIBLE);
                btnPlay.setVisibility(View.VISIBLE);
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.pause();
                length = 0;
                btnPause.setVisibility(View.INVISIBLE);
                btnPlay.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaPlayer.stop();
        finish();
    }
}
