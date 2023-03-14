package com.example.loudalarm.MathTrainerGame;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loudalarm.MainActivity;
import com.example.loudalarm.R;
import com.example.loudalarm.databinding.ActivityMathTrainerBinding;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class MathTrainerActivity extends AppCompatActivity {
    private final Problem problem = new Problem();
    private static int howManyGener = 0;
    MediaPlayer musicPlay;
    ActivityMathTrainerBinding binding;
    boolean fl = false;
    int counter = 2;
    long timeOfPass = System.nanoTime();
    long stopTime;
    long startTime = System.nanoTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        fl = false;
        super.onCreate(savedInstanceState);
        binding = ActivityMathTrainerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gener();
        click click = new click();
        binding.text.setOnClickListener(click);
        binding.text1.setOnClickListener(click);
        binding.text2.setOnClickListener(click);
        //todo uri not null
        musicPlay = MediaPlayer.create(this, null);
        AtomicBoolean isMusicPlay = new AtomicBoolean(false);
        AtomicBoolean d = new AtomicBoolean(true);
        AtomicInteger howManyPassed = new AtomicInteger();

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            try {
                if (System.nanoTime() - timeOfPass >= 20 * 1_000_000_000L) {
                    if (!isMusicPlay.get() && d.get()) {
                        musicPlay.start();
                        isMusicPlay.set(true);
                        howManyPassed.set(howManyGener);
                        d.set(false);
                    } else if ((howManyGener - howManyPassed.get()) > 2) {
                        musicPlay.stop();
                        isMusicPlay.set(false);
                        d.set(true);
                    }
                }
            } catch (Exception e) {
            }
        }, 0, 1, TimeUnit.SECONDS);

        binding.next.setOnClickListener(v -> {
            timeOfPass = System.nanoTime();

            stopTime = System.nanoTime();
            long deltaTime = stopTime - startTime;
            if (fl) {
                if (deltaTime < 120_000_000_000L || howManyGener < 30) {
                    binding.text.setBackground(getDrawable(R.drawable.buttons));
                    binding.text1.setBackground(getDrawable(R.drawable.buttons));
                    binding.text2.setBackground(getDrawable(R.drawable.buttons));
                    gener();
                    counter = 3;
                } else {
                    new AlertDialog.Builder(this)
                            .setMessage("Уже прошло две минуты. Вы можете выйти из игры. Хотите?")
                            .setPositiveButton("ДА", (dialog, id) -> {
                                startActivity(new Intent(this, MainActivity.class));
                                finish();
                            })
                            .setNegativeButton("НЕТ", (dialog, id) -> {
                                dialog.dismiss();
                                gener();
                            })
                            .create().show();
                }
            } else {
                new AlertDialog.Builder(this)
                        .setMessage("Вы не можете нажать эту кнопку!")
                        .setNeutralButton("OK", (dialog, id) -> dialog.dismiss())
                        .create().show();
            }
        });
    }

    //
//    НЕ ПОРТЬ ЖИЗНЬ ПОКА! ПОТОМ РАЗБЛОКИРУЮ
//
//    public void onBackPressed() {
//        stopTime = System.nanoTime();
//        long deltaTime = stopTime - startTime;
//        startTime = System.nanoTime();
//        if (deltaTime > 120_000_000_000L && howManyGener>=30){
//            finish();
//        }
//    }
    private void gener() {
        unblock(findViewById(R.id.text1), findViewById(R.id.text), findViewById(R.id.text2));
        howManyGener += 1;
        int pos = problem.getRandom(1, 4);
        binding.problem.setText(problem.getProblem());
        float a = problem.getNotResult();
        float b = problem.getNotResult();
        if (a != problem.getResult() && a == b) {
            b = a + problem.getRandom(-10, 0);
            if (b == problem.getResult()) b = b + problem.getRandom(12, 15);
        }
        if (a == problem.getResult() && a == b) {
            a = a + problem.getRandom(-10, -1);
            b = b + problem.getRandom(12, 15);
        }

        switch (pos) {
            case 1:
                binding.text1.setText(String.format("%.2f", problem.getResult()));
                binding.text.setText(String.format("%.2f", a));
                binding.text2.setText(String.format("%.2f", b));
                break;
            case 2:

                binding.text2.setText(String.format("%.2f", problem.getResult()));
                binding.text1.setText(String.format("%.2f", b));
                binding.text.setText(String.format("%.2f", a));
                break;
            case 3:
                binding.text.setText(String.format("%.2f", problem.getResult()));
                binding.text1.setText(String.format("%.2f", a));
                binding.text2.setText(String.format("%.2f", b));
                break;
        }


    }


    class click implements View.OnClickListener {

        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.text:
                case R.id.text1:
                case R.id.text2:
                    String text = ((TextView) view).getText().toString();
                    if (text.equals(String.format("%.2f", problem.getResult()))) {
                        view.setBackground(getDrawable(R.drawable.buttons_true));
                        block(findViewById(R.id.text1), findViewById(R.id.text), findViewById(R.id.text2));
                        fl = true;
                    } else {
                        view.setBackground(getDrawable(R.drawable.buttons_false));
                        fl = false;

                        counter--;
                        if (counter > 0) {
                            Toast.makeText(getApplicationContext(), "У Вас осталось " + counter + " попыток", Toast.LENGTH_SHORT).show();
                        }
                        if (counter == 0) {
                            new AlertDialog.Builder(MathTrainerActivity.this)
                                    .setMessage("Упс! Кажется, жизни кончились((")
                                    .setNeutralButton("OK", (dialog, id) -> {
                                        dialog.dismiss();
                                        gener();
                                    })
                                    .create().show();
                            gener();

                        }
                        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
                            binding.text.setBackground(getDrawable(R.drawable.buttons));
                            binding.text1.setBackground(getDrawable(R.drawable.buttons));
                            binding.text2.setBackground(getDrawable(R.drawable.buttons));
                        }, 1, TimeUnit.SECONDS);
                    }
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {

    }

    public void block(View... view) {
        for (View v : view) {
            v.setClickable(false);
        }

    }
    public void unblock(View... view) {
        for (View v : view) {
            v.setClickable(true);
        }

    }
}