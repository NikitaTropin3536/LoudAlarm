package com.example.loudalarm.Fragments;

import static android.app.Activity.RESULT_OK;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loudalarm.App;
import com.example.loudalarm.R;
import com.example.loudalarm.RingingActivity;
import com.example.loudalarm.Room.AlarmDAO;
import com.example.loudalarm.Room.AlarmEntity;
import com.example.loudalarm.databinding.FragmentAddBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class AddFragment extends Fragment {
    private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
    public String checkedMusicStringNameUri;
    public boolean canPlay;
    // todo or think about how to provide getContext()
    public String days = "";
    public AudioManager audioManager;
    AlarmDAO alarmDatabaseDAO;
    public AlarmManager alarmManager;
    AlarmEntity alarm;
    List<AlarmEntity> alarms = new ArrayList<>();
    private MaterialTimePicker materialTimePicker;
    private Calendar calendar;
    private FragmentAddBinding binding;

    public AddFragment(AlarmEntity alarm) {
        this.alarm = alarm;
    }

    public static AddFragment newInstance(AlarmEntity alarm) {
        return new AddFragment(alarm);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add, container, false);


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentAddBinding.bind(view);
        binding.alarmButton.setText(alarm.time_on_clock_in_hours_and_minutes);

        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarm.hours);
        calendar.set(Calendar.MINUTE, alarm.minutes);


        binding.volumeControl.setProgress(alarm.vol);
        binding.moreLoud.setChecked(alarm.isAlarmAdjustVolume);
        binding.minuteInt.setProgress(alarm.minute);


        canPlay = alarm.alarmCanPlay;
        binding.vibration.setChecked(alarm.vib);
        binding.setOnToday.setChecked(alarm.today);

        binding.textMessage.setText(alarm.textMessage);
        binding.nameOfCheckedMusic.setText(alarm.music);

        binding.monday.setChecked(alarm.monday);
        binding.tuesday.setChecked(alarm.tuesday);
        binding.wednesday.setChecked(alarm.wednesday);
        binding.thuesday.setChecked(alarm.thursday);
        binding.friday.setChecked(alarm.friday);
        binding.saturday.setChecked(alarm.saturday);
        binding.sunday.setChecked(alarm.sunday);


        final int id = alarm.id;
        checkedMusicStringNameUri = alarm.music;


        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        binding.musicButton.setOnClickListener(h -> {
            Intent intent_upload = new Intent();
            intent_upload.setType("audio/*");
            intent_upload.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent_upload, 1);
        });


        binding.setOnToday.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (binding.setOnToday.isChecked()) {
                binding.repeat.setVisibility(View.GONE);
                binding.mtrlCalendarDaysOfWeek.setVisibility(View.GONE);
                setAllDaysWidgetsChecked(false, binding.friday, binding.monday, binding.thuesday, binding.saturday, binding.tuesday, binding.wednesday, binding.sunday);

            } else {
                binding.repeat.setVisibility(View.VISIBLE);
                binding.mtrlCalendarDaysOfWeek.setVisibility(View.VISIBLE);
            }
        });

        binding.volumeControl.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        binding.volumeControl.setProgress(alarm.vol);
        binding.volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.volumeControl.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.moreLoud.setChecked(alarm.isAlarmAdjustVolume);

        canPlay = alarm.alarmCanPlay;
        binding.vibration.setChecked(alarm.vib);


        binding.alarmButton.setOnClickListener(n -> {
            materialTimePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(alarm.hours)
                    .setMinute(alarm.minutes)
                    .setTitleText("Выберите время для будильника")
                    .build();

            materialTimePicker.addOnPositiveButtonClickListener(v -> {

                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.MINUTE, materialTimePicker.getMinute());
                calendar.set(Calendar.HOUR_OF_DAY, materialTimePicker.getHour());

                alarm.hours = materialTimePicker.getHour();
                alarm.minutes = materialTimePicker.getMinute();

                binding.alarmButton.setTextSize(90);
                binding.alarmButton.setText(sdf.format(calendar.getTime()));
                alarm.time_on_clock_in_hours_and_minutes = binding.alarmButton.getText().toString();
            });
            materialTimePicker.show(getActivity().getSupportFragmentManager(), "tag_picker");
        });

        binding.monday.setOnClickListener(c -> {
            alarm.monday = binding.monday.isChecked();
            setVisibilityToWeekDayToggles();
        });
        binding.tuesday.setOnClickListener(c -> {
            alarm.tuesday = binding.tuesday.isChecked();
            setVisibilityToWeekDayToggles();
        });
        binding.wednesday.setOnClickListener(c -> {
            alarm.wednesday = binding.wednesday.isChecked();
            setVisibilityToWeekDayToggles();
        });
        binding.thuesday.setOnClickListener(c -> {
            alarm.thursday = binding.thuesday.isChecked();
            setVisibilityToWeekDayToggles();
        });
        binding.friday.setOnClickListener(c -> {
            alarm.friday = binding.friday.isChecked();
            setVisibilityToWeekDayToggles();
        });
        binding.saturday.setOnClickListener(c -> {
            alarm.saturday = binding.saturday.isChecked();
            setVisibilityToWeekDayToggles();
        });
        binding.sunday.setOnClickListener(c -> {
            alarm.sunday = binding.sunday.isChecked();
            setVisibilityToWeekDayToggles();
        });

        binding.createNewAlarm.setOnClickListener(v -> {
            if (binding.monday.isChecked() && !days.contains("M ")) days += "M ";
            if (binding.tuesday.isChecked() && !days.contains("TU ")) days += "TU ";
            if (binding.wednesday.isChecked() && !days.contains("W ")) days += "W ";
            if (binding.thuesday.isChecked() && !days.contains("TH ")) days += "TH ";
            if (binding.friday.isChecked() && !days.contains("F ")) days += "F ";
            if (binding.saturday.isChecked() && !days.contains("SA ")) days += "SA ";
            if (binding.sunday.isChecked() && !days.contains("SU ")) days += "SU ";
            if (days.isEmpty()) days = binding.setOnToday.isChecked() ? "Today" : "Tomorrow";


            alarm.setAlarmCanPlay(canPlay);
            alarm.setDays(days);
            alarm.setMinute(binding.minuteInt.getProgress());
            alarm.setVol(binding.volumeControl.getProgress());
            alarm.setVib(binding.vibration.isChecked());
            alarm.setId(id);
            alarm.setToday(binding.setOnToday.isChecked());
            alarm.setAlarmAdjustVolume(binding.moreLoud.isChecked());
            alarm.setTextMessage(binding.textMessage.getText().toString());
            alarm.setMusic(checkedMusicStringNameUri);
            alarm.setTime(calendar.getTimeInMillis());

            new Thread(() -> {
                alarmDatabaseDAO = App.getDatabase().alarmDAO();
                alarms = alarmDatabaseDAO.getAll();
                Log.i("QWERTY123456thread", alarms.toString());
                int foundedAlarmIndex = -1;
                for (int i = 0; i < alarms.size(); i++) {
                    if (alarms.get(i).id == alarm.id) {
                        foundedAlarmIndex = i;
                        break;
                    }
                }
                if (foundedAlarmIndex == -1) {
                    alarms.add(alarm);
                    foundedAlarmIndex = alarms.size() - 1;
                } else {
                    alarms.set(foundedAlarmIndex, alarm);
                }
                alarms.sort(Comparator.comparingLong(o -> o.time));
                alarmDatabaseDAO.updateAll(alarms);
                Log.i("QWERTY123456threadUpdated", alarms.toString());

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fl_content, HomeFragment.newInstance(alarms))
                        .commit();
            }).start();


            if (!binding.monday.isChecked() && !binding.tuesday.isChecked()
                    && !binding.wednesday.isChecked() && !binding.thuesday.isChecked()
                    && !binding.friday.isChecked() && !binding.saturday.isChecked()
                    && !binding.sunday.isChecked()) {
                if (!binding.setOnToday.isChecked()) {
                    setAlarm(id, alarm.time + 1000 * 60 * 60 * 24);
                } else if (binding.setOnToday.isChecked() && calendar.getTime().before(Calendar.getInstance().getTime())) {
                    Toast.makeText(getContext(), "Это время уже прошло", Toast.LENGTH_SHORT).show();
                } else if (binding.setOnToday.isChecked() && !calendar.getTime().before(Calendar.getInstance().getTime())) {
                    setAlarm(id, alarm.time);
                }
            } else {

                if (binding.monday.isChecked()) {
                    setRepeatingAlarm(id, Calendar.MONDAY, alarm.time);
                }
                if (binding.tuesday.isChecked()) {
                    setRepeatingAlarm(id, Calendar.TUESDAY, alarm.time);
                }
                if (binding.wednesday.isChecked()) {
                    setRepeatingAlarm(id, Calendar.WEDNESDAY, alarm.time);
                }
                if (binding.thuesday.isChecked()) {
                    setRepeatingAlarm(id, Calendar.THURSDAY, alarm.time);
                }
                if (binding.friday.isChecked()) {
                    setRepeatingAlarm(id, Calendar.FRIDAY, alarm.time);
                }
                if (binding.saturday.isChecked()) {
                    setRepeatingAlarm(id, Calendar.SATURDAY, alarm.time);
                }
                if (binding.sunday.isChecked()) {
                    setRepeatingAlarm(id, Calendar.SUNDAY, alarm.time);
                }
            }
            Log.i("QWERTY12alarms", alarms.toString());


        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private PendingIntent getAlarmActionPendingIntent(int id) {
        Intent intent = new Intent(getContext(), RingingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(getContext(), -id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public void setAlarm(int id, long start_time) {

//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Intent go_to_ring = new Intent(getContext(), RingingActivity.class);
//                go_to_ring.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                go_to_ring.putExtra(ID, id);
//                Log.i("QWERTY!@#", "LKLKLK");
//                startActivity(go_to_ring);
//            }
//        }, delay);
        alarmManager.set(AlarmManager.RTC_WAKEUP, start_time, getAlarmActionPendingIntent(id));
    }

    public void setRepeatingAlarm(int id, int number_of_week_day, long start_time) {
        int difference = calendar.getTime().getDay() - number_of_week_day;
        long period = 7 * 24 * 60 * 60 * 1000L;
        Calendar add_calendar = Calendar.getInstance();
        add_calendar.set(Calendar.HOUR_OF_DAY, alarm.hours);
        add_calendar.set(Calendar.MINUTE, alarm.minute);
        add_calendar.set(Calendar.WEEK_OF_MONTH, difference < 0 ? calendar.get(Calendar.WEEK_OF_MONTH) + 1 : calendar.get(Calendar.WEEK_OF_MONTH));
        add_calendar.set(Calendar.DAY_OF_WEEK, number_of_week_day);
//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Intent go_to_ring = new Intent(getContext(), RingingActivity.class);
//                go_to_ring.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                go_to_ring.putExtra(ID, id);
//
//                startActivity(go_to_ring);
//            }
//        }, delay, period);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, add_calendar.getTimeInMillis(), period, getAlarmActionPendingIntent(id));

    }

    public void setVisibilityToWeekDayToggles() {

        if (alarm.sunday || alarm.saturday || alarm.friday || alarm.thursday || alarm.wednesday || alarm.tuesday || alarm.monday) {
            binding.setOnToday.setVisibility(View.GONE);
            alarm.today = false;
        } else {
            binding.setOnToday.setVisibility(View.VISIBLE);
        }
    }


    private void setAllDaysWidgetsChecked(boolean b, ToggleButton... views) {
        for (ToggleButton v : views) {
            v.setChecked(b);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = intent.getData();
                checkedMusicStringNameUri = uri.toString();
                binding.nameOfCheckedMusic.setText(checkedMusicStringNameUri);
            }
        } else {
            binding.nameOfCheckedMusic.setText(checkedMusicStringNameUri);

        }
    }
}