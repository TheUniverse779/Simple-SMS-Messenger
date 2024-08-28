package com.messenger.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.simplemobiletools.smsmessenger.R;
import com.messenger.helper.SqDatabase;
import com.messenger.models.MessApp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LimitAdapter extends RecyclerView.Adapter<LimitAdapter.ViewHolder> {

    private Context context;

    private ArrayList<MessApp> messApps;

    public LimitAdapter(Context context, ArrayList<MessApp> messApps) {
        this.context = context;
        this.messApps = messApps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_limit,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.tvAppName.setText(messApps.get(position).getName());

        long timeLimit = messApps.get(position).getTimeLimit();

        holder.tvTimeLimit.setText(timeLimit+" minutes");

        holder.seekBar.setProgress((int) messApps.get(position).getTimeLimit());

        holder.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                holder.tvTimeLimit.setText(progress+" minutes");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                messApps.get(position).setTimeLimit(seekBar.getProgress());
                SqDatabase.getDb().updateTimeLimit(context,messApps.get(position));
            }
        });

        Glide.with(context)
                .load(getIcon(messApps.get(position).getIcon()))
                .into(holder.icon);


    }

    @Override
    public int getItemCount() {
        return messApps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvAppName,tvTimeLimit;

        private SeekBar seekBar;

        private ImageView icon;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAppName = itemView.findViewById(R.id.tv_app_name);
            tvTimeLimit = itemView.findViewById(R.id.tv_time_limit);
            seekBar = itemView.findViewById(R.id.sb_limit);
            icon = itemView.findViewById(R.id.img_app);
        }
    }

    private Bitmap getIcon(String s){
        try {
            InputStream inputStream = context.getAssets().open("oh/"+s+".png");
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
