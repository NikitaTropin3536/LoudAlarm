package com.example.loudalarm.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loudalarm.AlarmAdapter;
import com.example.loudalarm.App;
import com.example.loudalarm.Room.AlarmDAO;
import com.example.loudalarm.Room.AlarmEntity;
import com.example.loudalarm.databinding.FragmentHomeBinding;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    AlarmDAO alarmDatabaseDAO;

    List<AlarmEntity> alarms;

    public HomeFragment(List<AlarmEntity> alarms) {
        this.alarms = alarms;
    }

    public static HomeFragment newInstance(List<AlarmEntity> alarms) {
        return new HomeFragment(alarms);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        new Thread(() -> {
            alarmDatabaseDAO = App.getDatabase().alarmDAO();
            Log.i("NOT_UPDATED", alarmDatabaseDAO.getAll().toString());
            if (alarms.size() <= alarmDatabaseDAO.getAll().size()) {
                alarmDatabaseDAO.updateAll(alarms);

            } else {
                alarmDatabaseDAO.deleteAll();
                alarmDatabaseDAO.saveAll(alarms);
            }
            Log.i("YET_UPDATED", alarmDatabaseDAO.getAll().toString());

        }).start();

        return binding.getRoot();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding.recyclerView.setAdapter(new AlarmAdapter(getActivity(), alarms));

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}