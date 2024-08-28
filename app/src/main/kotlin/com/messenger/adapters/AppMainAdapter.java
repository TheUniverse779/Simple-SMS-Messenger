package com.messenger.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.simplemobiletools.smsmessenger.R;
import com.messenger.helper.OnTouch;
import com.messenger.models.MessApp;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public abstract class AppMainAdapter extends RecyclerView.Adapter<AppMainAdapter.ViewHolder> {

    private Context context;

    private ArrayList<MessApp> messApps;

    protected AppMainAdapter(Context context, ArrayList<MessApp> messApps) {
        this.context = context;
        this.messApps = messApps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_app_main,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        holder.tvAppName.setText(messApps.get(position).getUsename());

        Glide.with(context)
                .load(getIcon(messApps.get(position).getIcon()))
                .into(holder.icon);



    }

    @Override
    public int getItemCount() {
        return messApps.size();
    }

    public void setData(ArrayList<MessApp> messApps) {
        this.messApps = messApps;
        notifyDataSetChanged();
    }

    public void remove(int position){
        this.messApps.remove(position);
        notifyItemRemoved(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvAppName;

        private ImageView icon;

        private View menu;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvAppName = itemView.findViewById(R.id.tvAppName);
            icon = itemView.findViewById(R.id.img_icon);
            menu = itemView.findViewById(R.id.menu_app);
            itemView.getLayoutParams().height = (int) (context.getResources().getDisplayMetrics().widthPixels/3.5);
            itemView.getLayoutParams().width = (int) (context.getResources().getDisplayMetrics().widthPixels/3.5);

            itemView.setOnClickListener(v ->
                    OnItemClick(messApps.get(getAdapterPosition()),getAdapterPosition()));
            itemView.setOnLongClickListener(v -> {
                menu.performClick();
                return true;
            });


            itemView.setOnTouchListener(new OnTouch());
            menu.setOnTouchListener(new OnTouch());

            menu.setOnClickListener(v ->
                    OnMenuClick(messApps.get(getAdapterPosition()),getAdapterPosition()));
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

    public abstract void OnItemClick(MessApp messApp,int position);
    public abstract void OnMenuClick(MessApp messApp,int position);
}
