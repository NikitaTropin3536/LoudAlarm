package com.example.loudalarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loudalarm.Room.AlarmEntity;

import java.util.Comparator;
import java.util.List;


public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.CreateNewAlarmViewHolder> {
    private final static int REQUEST_L = 9876;
    private final List<AlarmEntity> list;
    private final Activity activity;


    public AlarmAdapter(Activity activity, List<AlarmEntity> news) {
        this.activity = activity;
        this.list = news;
    }

    @NonNull
    @Override
    public AlarmAdapter.CreateNewAlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new CreateNewAlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlarmAdapter.CreateNewAlarmViewHolder holder, int position) {
        list.sort(Comparator.comparingLong(o -> o.time));

        final AlarmEntity alarm = list.get(position);
        holder.time.setText(list.get(position).time_on_clock_in_hours_and_minutes);
        holder.OnOff.setChecked(list.get(position).on);
        holder.message.setText(list.get(position).textMessage);
        holder.days.setText(list.get(position).days);
        holder.OnOff.setOnClickListener(b -> {
            alarm.on = holder.OnOff.isChecked();
        });

        holder.itemView.setOnClickListener(v -> {

        });
        holder.itemView.setOnLongClickListener(lon -> {
            int index = list.indexOf(alarm);
            if (index < 0) return false;
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("Удалить будильник?");

            builder.setPositiveButton("ДА", (dialog, id) -> {
                dialog.dismiss();
                list.remove(alarm);
                notifyItemRemoved(index);
            });
            builder.setNegativeButton("НЕТ", (dialog, id) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
            return true;


        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CreateNewAlarmViewHolder extends RecyclerView.ViewHolder {
        TextView time, days, message;
        Switch OnOff;

        public CreateNewAlarmViewHolder(View itemView) {
            super(itemView);
            OnOff = itemView.findViewById(R.id.OnOff);
            time = itemView.findViewById(R.id.time);
            message = itemView.findViewById(R.id.textM);
            days = itemView.findViewById(R.id.days);

        }
    }

}