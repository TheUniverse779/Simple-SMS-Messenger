package com.messenger.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.simplemobiletools.smsmessenger.R;
import com.messenger.helper.OnTouch;
import com.messenger.models.MessApp;
import com.messenger.views.checkbox.OneCheck;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SelectServiceAdapter extends RecyclerView.Adapter<SelectServiceAdapter.ViewHolder> {

    private Context context;

    private ArrayList<MessApp> messApps;

    public SelectServiceAdapter(Context context, ArrayList<MessApp> messApps) {
        this.context = context;
        this.messApps = messApps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_select_sevice,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.oneCheck.setText(messApps.get(position).getName());

        Glide.with(context)
                .load(getIcon(messApps.get(position).getIcon()))
                .into(holder.icon);

        holder.itemView.setOnClickListener(v -> holder.oneCheck.setChecked(true));

    }

    @Override
    public int getItemCount() {
        return messApps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private OneCheck oneCheck;

        private ImageView icon;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            oneCheck = itemView.findViewById(R.id.rd_cb);
            icon = itemView.findViewById(R.id.img_icon);
            itemView.getLayoutParams().height = (int) (context.getResources().getDisplayMetrics().widthPixels/3.5);
            itemView.getLayoutParams().width = (int) (context.getResources().getDisplayMetrics().widthPixels/3.5);
            itemView.setOnTouchListener(new OnTouch());
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
