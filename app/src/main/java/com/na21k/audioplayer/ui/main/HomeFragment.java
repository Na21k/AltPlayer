package com.na21k.audioplayer.ui.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.h6ah4i.android.media.IBasicMediaPlayer;
import com.na21k.audioplayer.BlurBuilder;
import com.na21k.audioplayer.MainActivity;
import com.na21k.audioplayer.R;

import java.io.IOException;


public class HomeFragment extends Fragment {

    private ImageView albumArt;
    private ImageButton playPause;
    private ImageButton prev;
    private ImageButton next;
    private SeekBar seekBar;
    private TextView timePassed;
    private TextView timeOverall;
    private TextView title;
    private View view;
    private Handler seekBarUpdateHandler;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        this.view = view;
        albumArt = view.findViewById(R.id.albumArtButton);
        prev = view.findViewById(R.id.previousButton);
        next = view.findViewById(R.id.nextButton);
        playPause = view.findViewById(R.id.playPauseButton);
        if (MainActivity.mediaPlayer.isPlaying()) {
            playPause.setImageResource(R.drawable.pause);
        }
        seekBar = view.findViewById(R.id.songSeekBar);
        timePassed = view.findViewById(R.id.timePassed);
        timeOverall = view.findViewById(R.id.timeOverall);
        title = view.findViewById(R.id.titleTextView);
        seekBarUpdateHandler = new Handler();
        update();

        MainActivity.mediaPlayer.setOnPreparedListener(new IBasicMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IBasicMediaPlayer mp) {
                update();
            }
        });

        //it can have only one OnCompletionListener and its in MainActivity (
        /*MainActivity.mediaPlayer.setOnCompletionListener(new IBasicMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IBasicMediaPlayer mp) {
                playPause.setImageResource(R.drawable.play);
            }
        });*/

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && MainActivity.queueCurrent > -1) {
                    MainActivity.mediaPlayer.seekTo(progress * 1000);
                    timePassed.setText(secToMinAndSec(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.queueCurrent < 0) {
                    return;
                } else if (MainActivity.queueCurrent == 0) {
                    MainActivity.queueCurrent = MainActivity.queue.size() - 1;
                } else {
                    MainActivity.queueCurrent--;
                }
                try {
                    MainActivity.mediaPlayer.reset();
                    MainActivity.mediaPlayer.setDataSource(MainActivity.queue.get(MainActivity.queueCurrent).getPath());
                    MainActivity.mediaPlayer.prepare();
                    MainActivity.mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.queueCurrent < 0) {
                    return;
                } else if (MainActivity.queueCurrent == MainActivity.queue.size() - 1) {
                    MainActivity.queueCurrent = 0;
                } else {
                    MainActivity.queueCurrent++;
                }
                try {
                    MainActivity.mediaPlayer.reset();
                    MainActivity.mediaPlayer.setDataSource(MainActivity.queue.get(MainActivity.queueCurrent).getPath());
                    MainActivity.mediaPlayer.prepare();
                    MainActivity.mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        playPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.mediaPlayer.isPlaying()) {
                    MainActivity.mediaPlayer.pause();
                    playPause.setImageResource(R.drawable.play);
                } else {
                    if (MainActivity.queueCurrent == -1) {
                        Toast.makeText(getContext(), "Queue is empty yet", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    MainActivity.mediaPlayer.start();
                    playPause.setImageResource(R.drawable.pause);
                }
            }
        });

        return view;
    }

    private void update() {
        if (MainActivity.queueCurrent > -1) {
            MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();

            String filePath = MainActivity.queue.get(MainActivity.queueCurrent).getPath();
            metaRetriever.setDataSource(filePath);
            byte[] embeddedPicture = metaRetriever.getEmbeddedPicture();
            if (embeddedPicture != null) {
                Bitmap bitmapPicture = BitmapFactory.decodeByteArray(embeddedPicture, 0, embeddedPicture.length);
                albumArt.setImageBitmap(bitmapPicture);
                Bitmap blurredArt = BlurBuilder.blur(getContext(), bitmapPicture);
                view.setBackground(new BitmapDrawable(getResources(), blurredArt));
            } else {
                albumArt.setImageResource(R.drawable.no_art);
                Drawable background = getResources().getDrawable(R.drawable.no_art_background);
                view.setBackground(background);
            }
            albumArt.setBackgroundColor(Color.TRANSPARENT);
            title.setTextColor(Color.WHITE);
            timePassed.setTextColor(Color.WHITE);
            timeOverall.setTextColor(Color.WHITE);
            seekBar.setMax(MainActivity.mediaPlayer.getDuration() / 1000);
            seekBar.setProgress(0);
            timePassed.setText("0:00");
            timeOverall.setText(secToMinAndSec(MainActivity.mediaPlayer.getDuration() / 1000));
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (MainActivity.mediaPlayer != null) {
                        int currentPos = MainActivity.mediaPlayer.getCurrentPosition() / 1000;
                        seekBar.setProgress(currentPos);
                        timePassed.setText(secToMinAndSec(currentPos));
                    }
                    seekBarUpdateHandler.postDelayed(this, 1000);
                }
            });
            playPause.setImageResource(R.drawable.pause);
            String ttl = MainActivity.queue.get(MainActivity.queueCurrent).getTitle() + " - " +
                    MainActivity.queue.get(MainActivity.queueCurrent).getArtist();
            title.setText(ttl);
        }
    }

    private String secToMinAndSec(int sec) {
        StringBuilder builder = new StringBuilder();
        builder.append(sec / 60);
        builder.append(":");
        builder.append(sec % 60);
        return builder.toString();
    }
}
