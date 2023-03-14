package com.example.loudalarm;

import static com.example.loudalarm.App.ID;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.loudalarm.MathTrainerGame.MathTrainerActivity;
import com.example.loudalarm.Room.AlarmDAO;
import com.example.loudalarm.Room.AlarmEntity;
import com.example.loudalarm.databinding.ActivityRingingBinding;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class RingingActivity extends AppCompatActivity {
    ActivityRingingBinding binding;
    AlarmDAO alarmDatabaseDAO;
    MediaPlayer player;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        binding = ActivityRingingBinding.inflate(getLayoutInflater());

        new Thread(() -> {
            alarmDatabaseDAO = App.getDatabase().alarmDAO();
        }).start();

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int id = getIntent().getIntExtra(ID, -1);
        player = new MediaPlayer();


        if (alarmDatabaseDAO.get(id).equals(null) || id == -1) {
            finish();
        } else {
            AlarmEntity alarm = alarmDatabaseDAO.get(id);
            binding.messageOfAlarm.setText(alarm.getTextMessage());

            startPlayMusic(alarm, player);
        }


        binding.offAlarm.setOnClickListener(off -> {
            stopPlayMusic(player);
            startActivity(new Intent(this, MathTrainerActivity.class));
            finish();
        });

    }


    public void startPlayMusic(AlarmEntity alarm, MediaPlayer player) {
        Uri uri = Uri.parse(alarm.music);
        if (alarm.isVib()) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, alarm.vol, AudioManager.FLAG_VIBRATE);
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, alarm.vol, AudioManager.FLAG_ALLOW_RINGER_MODES);

        if (alarm.isAlarmAdjustVolume) {
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() ->
                    audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND), 0, 5, TimeUnit.SECONDS);
        }
        try {
            player.setDataSource(RingingActivity.this, uri);
            player.setOnPreparedListener(MediaPlayer::start);
            player.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopPlayMusic(MediaPlayer player) {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
        player.stop();
        player.reset();
    }
}