package com.example.loudalarm.Fragments;

import static com.example.loudalarm.App.SAVE_URI;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.loudalarm.App;
import com.example.loudalarm.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {


    Uri uriOfMusic;
    private FragmentSettingsBinding binding;

    public SettingsFragment(Uri uriOfMusic) {
        this.uriOfMusic = uriOfMusic;
    }

    public static SettingsFragment newInstance(Uri uriOfMusic) {
        return new SettingsFragment(uriOfMusic);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        binding.nameOfCheckedMusic.setText(uriOfMusic.getPath());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        binding.ringtone.setOnClickListener(b -> {
            Intent intent_upload = new Intent();
            intent_upload.setType("audio/*");
            intent_upload.setAction(Intent.ACTION_GET_CONTENT);
            uriOfMusic = intent_upload.getData();
            startActivityForResult(intent_upload, SAVE_URI);
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == SAVE_URI) {
            uriOfMusic = intent.getData();
            binding.nameOfCheckedMusic.setText(uriOfMusic.getLastPathSegment());

        }
        App.setDefaultMusicUri(uriOfMusic);
    }
}