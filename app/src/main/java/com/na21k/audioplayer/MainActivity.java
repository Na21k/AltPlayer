package com.na21k.audioplayer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.h6ah4i.android.media.IBasicMediaPlayer;
import com.h6ah4i.android.media.IMediaPlayerFactory;
import com.h6ah4i.android.media.audiofx.IEqualizer;
import com.h6ah4i.android.media.hybrid.HybridMediaPlayerFactory;
import com.na21k.audioplayer.ui.main.SectionsPagerAdapter;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    private final int SELECT_FOLDER_REQUEST_CODE = 2;
    private static IMediaPlayerFactory mediaPlayerFactory;
    public static IBasicMediaPlayer mediaPlayer;
    public static IEqualizer equalizer;
    public static String musicPath;
    private SongViewModel songViewModel;
    public static List<Song> queue;
    public static int queueCurrent = -1;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(2);

        songViewModel = new ViewModelProvider(this).get(SongViewModel.class);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {
            int songsCount = songViewModel.getSongsCount();
            if (songsCount == 0) {
                Intent getPathIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                startActivityForResult(getPathIntent, SELECT_FOLDER_REQUEST_CODE);
            }
        }

        if (mediaPlayerFactory == null || mediaPlayer == null || equalizer == null) {
            mediaPlayerFactory = new HybridMediaPlayerFactory(getApplicationContext());
            mediaPlayer = mediaPlayerFactory.createMediaPlayer();
            equalizer = mediaPlayerFactory.createHQEqualizer();
            equalizer.setEnabled(true);
            equalizer.usePreset((short) 3); //Flat
        }
        mediaPlayer.setOnCompletionListener(new IBasicMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IBasicMediaPlayer mp) {
                queueCurrent++;
                mp.reset();
                if (queueCurrent < queue.size()) {
                    try {
                        mp.setDataSource(queue.get(queueCurrent).getPath());
                        mp.prepare();
                        mp.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    queueCurrent = 0;
                    try {
                        mp.setDataSource(queue.get(queueCurrent).getPath());
                        mp.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getApplicationContext(), "End of the queue", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                // If request is cancelled, the result array is empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    int songsCount = songViewModel.getSongsCount();
                    if (songsCount == 0) {
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                        startActivityForResult(intent, SELECT_FOLDER_REQUEST_CODE);
                    }
                } else {
                    closeNow();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_FOLDER_REQUEST_CODE:
                File file = new File(data.getData().getPath());
                String path = file.getPath();
                /*path = path.replace("document", "storage");*/
                path = path.replace("tree", "storage");
                path = path.replace("primary", "emulated/0");
                path = path.replace(':', '/');

                MainActivity.musicPath = path;

                Toast.makeText(this, path, Toast.LENGTH_LONG).show();

                Intent fetchFilesIntent = new Intent(this, FetchFilesActivity.class);
                startActivity(fetchFilesIntent);
                break;
        }
    }

    private void closeNow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            finishAffinity();
        } else {
            finish();
        }
    }

    /*private void restart() {
        Intent mStartActivity = new Intent(this, MainActivity.class);
        int mPendingIntentId = 123456;
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, mPendingIntentId, mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, mPendingIntent);
        System.exit(0);
    }*/
}