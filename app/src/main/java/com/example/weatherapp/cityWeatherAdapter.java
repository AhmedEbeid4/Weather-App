package com.example.weatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class cityWeatherAdapter extends RecyclerView.Adapter<cityWeatherAdapter.viewHolder>{
    private ArrayList<Weather> weatherArrayList ;
    private Context context;
    private onClickListener onClickListener;

    public cityWeatherAdapter(ArrayList<Weather> weatherArrayList, Context context) {
        this.weatherArrayList = weatherArrayList;
        this.context = context;
    }

    public void setWeatherArrayList(ArrayList<Weather> weatherArrayList) {
        this.weatherArrayList = weatherArrayList;
    }

    public void setOnClickListener(cityWeatherAdapter.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.searched_item_layout,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(cityWeatherAdapter.viewHolder holder, int position) {
        Weather w=weatherArrayList.get(position);
        holder.tempTextView.setText(""+w.changeTempToInteger());
        holder.desc.setText(w.getDesc());
        holder.city.setText(w.getCityName());
        Ion.with(context).load(w.getIconResource()).intoImageView(holder.icon);
        holder.Date.setText(w.getDayAndMonth());
    }

    @Override
    public int getItemCount() {
        return weatherArrayList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView tempTextView;
        TextView desc;
        TextView city;
        ImageView icon;
        TextView Date;
        public viewHolder(View itemView) {
            super(itemView);
            tempTextView=(TextView) itemView.findViewById(R.id.searchedTemp);
            desc=(TextView) itemView.findViewById(R.id.searchedDesc);
            city=(TextView) itemView.findViewById(R.id.Searched_city);
            icon=(ImageView) itemView.findViewById(R.id.Searched_icon);
            Date=(TextView) itemView.findViewById(R.id.searchedDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null){
                        int position=getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            onClickListener.onClick(position);
                        }
                    }
                }
            });
        }
    }
    interface onClickListener{
        void onClick(int position);
    }
}
