package com.messenger.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.simplemobiletools.smsmessenger.R;
import com.messenger.models.DeviceApp;

import java.util.ArrayList;
import java.util.Collections;

public class DeviceAppAdapter extends RecyclerView.Adapter<DeviceAppAdapter.ViewHolder> {

    private Context context;

    private ArrayList<DeviceApp> deviceApps;

    public DeviceAppAdapter(Context context, ArrayList<DeviceApp> deviceApps) {
        this.context = context;
        this.deviceApps = deviceApps;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_device_app,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        DeviceApp deviceApp = deviceApps.get(position);

        holder.tvAppName.setText(deviceApps.get(position).getName());
        Glide.with(context)
                .load(deviceApps.get(position).getIcon())
                .into(holder.imgIcon);
        if(deviceApp.isCheck()){
            holder.rdCheck.setChecked(true);
        }else {
            holder.rdCheck.setChecked(false);
        }

        holder.item.setOnClickListener(view -> {
            holder.rdCheck.setChecked(!deviceApp.isCheck());
            deviceApp.setCheck(!deviceApp.isCheck());
            sortList();
        });


    }

    @Override
    public int getItemCount() {
        return deviceApps.size();
    }

    public void setData(ArrayList<DeviceApp> deviceApps) {
        this.deviceApps = deviceApps;
        sortList();
    }

    private void sortList(){
        Collections.sort(deviceApps, (deviceApp, t1) -> Boolean.compare(t1.isCheck(),deviceApp.isCheck()));
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvAppName;
        private ImageView imgIcon;
        private View item,mainItem;
        private RadioButton rdCheck;


        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvAppName = itemView.findViewById(R.id.tv_app_name);
            imgIcon = itemView.findViewById(R.id.img_app);
            item = itemView.findViewById(R.id.item);
            rdCheck = itemView.findViewById(R.id.rd_check);
            mainItem = itemView.findViewById(R.id.main_item);

        }
    }


}
