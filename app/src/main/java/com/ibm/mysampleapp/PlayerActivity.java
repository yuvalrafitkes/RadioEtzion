package com.ibm.mysampleapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.Formatter;
import java.util.Locale;
import java.util.zip.Inflater;

public class PlayerActivity extends AppCompatActivity {

    private SimpleExoPlayer exoPlayer;
    private ExoPlayer.EventListener eventListener = new ExoPlayer.EventListener() {
        @Override
        public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {
            Log.i("TAG", "onTimelineChanged");
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            Log.i("TAG", "onTracksChanged");
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            Log.i("TAG", "onLoadingChanged");
        }


        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            Log.i("TAG", "onPlayerStateChanged: playWhenReady = " + String.valueOf(playWhenReady)
                    + " playbackState = " + playbackState);
            switch (playbackState) {
                case ExoPlayer.STATE_ENDED:
                    Log.i("TAG", "Playback ended!");
                    //Stop playback and return to start position
                    setPlayPause(true);
                    exoPlayer.seekTo(0);
                    break;
                case ExoPlayer.STATE_READY:
                    Log.i("TAG", "ExoPlayer ready! pos: " + exoPlayer.getCurrentPosition()
                            + " max: " + stringForTime((int) exoPlayer.getDuration()));
                    setProgress();
                    break;
                case ExoPlayer.STATE_BUFFERING:
                    Log.i("TAG", "Playback buffering!");
                    break;
                case ExoPlayer.STATE_IDLE:
                    Log.i("TAG", "ExoPlayer idle!");
                    break;
            }

        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            Log.i("TAG", "onPlaybackError: " + error.getMessage());
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            Log.i("TAG", "onPositionDiscontinuity");
        }

    };

    Handler handler;
    SeekBar seekBar;
    ImageButton btnPlay, btnPause, btnStop, btnNext, btnPre, btnShare, btnInfo,
            btnFavorite, btnFavoriteOn;
    TextView txtStartTime, txtEndTime, txtProgram;
    boolean isPlaying = false,isFav = false;
    String soundPath;
    String namePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);
        Intent intent = getIntent();
        txtProgram = findViewById(R.id.txtProgram);
        soundPath = intent.getStringExtra("url");
        namePath = intent.getStringExtra("urlName");
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        try {
            prepareExoPlayerFromURL(Uri.parse(soundPath));
            txtProgram.setText(namePath);
        } catch (Exception e) {
            Log.e("TAG", "Cant reach to sound Path");
        }

        setPointer();
    }

    private void setPointer() {

        //SHARE BUTTON-----------------------------------------------------------------------
        btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = soundPath;
                String shareSub = namePath;
                intent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, "SHARE ME"));
            }
        });
//INFO DIALOG----------------------------------------------------------------------------------
        btnInfo = findViewById(R.id.btnInfo);
        btnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(PlayerActivity.this).create();
                alertDialog.setTitle("Information ");
                alertDialog.setMessage("\n" +
                        "תכנית עם מרחב מוהר, סגן אלוף העולם באגרוף.\n" +
                        "בגיל 22 עם התואר \"אלוף ישראל באגרוף\" וכרטיס בכיוון אחד לניו יורק יוצא מרחב מוהר לארה\"ב.\n" +
                        "הוא משאיר מאחוריו הורים, חברים ומעריצים ומבקש להגשים את חלום חייו, להשיג את התואר אלוף העולם באגרוף.\n" +
                        "אחרי אימונים מפרכים הוא פורץ לזירה עטוף בדגל ישראל, אבל הקרב הופך להיות נקודת מפנה בחייו האישיים והמקצועיים.\n" +
                        "\n" +
                        "מרחב חושף את הסיפור האישי שלו מעורר השראה בכל הנושאים הקשורים למוטיבציה, מצוינות, לספורט כדרך לניתוב כעסים.\n" +
                        "\n" +
                        "שדרים: אושר מוזס ואורי בן חנן");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();
            }
        });
//FAVORITE BUTTON--------------------------------------------------------------------------------------------------

        btnFavorite = findViewById(R.id.btnFavorits);
        btnFavoriteOn = findViewById(R.id.btnFavoritsOn);

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFavorite.setVisibility(View.INVISIBLE);
                btnFavoriteOn.setVisibility(View.VISIBLE);
            }
        });

        btnFavoriteOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnFavorite.setVisibility(View.VISIBLE);
                btnFavoriteOn.setVisibility(View.INVISIBLE);
            }
        });

//X10 MIN--NEXT BUTTON---------------------------------------------------
        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime =exoPlayer.getCurrentPosition();
                currentTime +=600000;
                exoPlayer.seekTo(currentTime);
            }
        });
//X10 MIN--BACK BUTTON------------------------------------------------------

        btnPre = findViewById(R.id.btnPre);
        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTime =exoPlayer.getCurrentPosition();
                currentTime -=600000;
                exoPlayer.seekTo(currentTime);
            }
        });
    }

    private void prepareExoPlayerFromURL(Uri parse) {
        TrackSelector trackSelector = new DefaultTrackSelector();

        LoadControl loadControl = new DefaultLoadControl();

        exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "RadioEtzion"), null);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource audioSource = new ExtractorMediaSource(parse, dataSourceFactory, extractorsFactory, null, null);
        exoPlayer.addListener(eventListener);

        exoPlayer.prepare(audioSource);
        initMediaControls();
    }

    private void initMediaControls() {
        initPlayButton();
        initSeekBar();
        initTxtTime();
    }

    private void initTxtTime() {
        txtStartTime = findViewById(R.id.startTime);
        txtEndTime = findViewById(R.id.endTime);
    }

    private void initSeekBar() {
        seekBar = findViewById(R.id.seekBar);
        seekBar.requestFocus();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) {
                    return;
                }

                exoPlayer.seekTo(progress * 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekBar.setMax(0);
        seekBar.setMax((int) exoPlayer.getDuration() / 1000);
    }

    private void initPlayButton() {
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnPlay.requestFocus();
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayPause(true);
            }
        });


        btnPause.requestFocus();
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayPause(false);
            }
        });
    }

    private String stringForTime(int timeMs) {
        StringBuilder mFormatBuilder;
        Formatter mFormatter;
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private void setPlayPause(boolean play) {
        isPlaying = play;
        exoPlayer.setPlayWhenReady(play);
        if (!isPlaying) {
            btnPlay.setVisibility(View.VISIBLE);
            btnPause.setVisibility(View.INVISIBLE);
        } else {
            setProgress();
            btnPlay.setVisibility(View.INVISIBLE);
            btnPause.setVisibility(View.VISIBLE);
        }
    }

    private void setProgress() {
        seekBar.setProgress(0);
        seekBar.setMax((int) exoPlayer.getDuration() / 1000);
        txtStartTime.setText(stringForTime((int) exoPlayer.getCurrentPosition()));
        txtEndTime.setText(stringForTime((int) exoPlayer.getDuration()));
        if (handler == null) handler = new Handler();
        //Make sure you update Seekbar on UI thread
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (exoPlayer != null && isPlaying) {
                    seekBar.setMax((int) exoPlayer.getDuration() / 1000);
                    int mCurrentPosition = (int) exoPlayer.getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                    txtStartTime.setText(stringForTime((int) exoPlayer.getCurrentPosition()));
                    txtEndTime.setText(stringForTime((int) exoPlayer.getDuration()));

                    handler.postDelayed(this, 1000);
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        exoPlayer.release();
        super.onBackPressed();
    }

}

