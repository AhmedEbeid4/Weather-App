package com.example.weatherapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class weatherAdapter extends RecyclerView.Adapter<weatherAdapter.viewHolder>{

    private ArrayList<Weather> weatherArrayList;
    private Context context;
    private String sender;
    private weatherAdapter.onClickListener onClickListener;

    public weatherAdapter(ArrayList<Weather> weatherArrayList, Context context, String sender) {
        this.weatherArrayList = weatherArrayList;
        this.context = context;
        this.sender = sender;
    }

    public void setWeatherArrayList(ArrayList<Weather> weatherArrayList) {
        this.weatherArrayList = weatherArrayList;
    }

    @Override
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Integer a=R.layout.forecast_report_item_layout;
        View v= LayoutInflater.from(context).inflate(a,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(weatherAdapter.viewHolder holder, int position) {
            if (position==0){
                holder.father.setBackgroundColor(Color.parseColor("#094F99"));
            }
            holder.dayTextView.setText(
                    weatherArrayList.get(position)
                            .getDay()
            );
            holder.dayMonthTextView.setText(
                    weatherArrayList.get(position)
                            .getDayAndMonth()
            );


        holder.tempTextView.setText(""+weatherArrayList.get(position).changeTempToInteger());
        Ion.with(context).load(weatherArrayList.get(position).getIconResource()).intoImageView(holder.icon);

    }

    @Override
    public int getItemCount() {
        return weatherArrayList.size();
    }

    public void setOnClickListener(weatherAdapter.onClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView dayTextView;
        TextView dayMonthTextView;
        RelativeLayout father;

        ImageView icon;
        TextView tempTextView;

        public viewHolder(View itemView) {
            super(itemView);
            if (sender.equals(staticTexts.FROM_FORECAST_REPORT_ACTIVITY)){
                dayTextView=(TextView) itemView.findViewById(R.id.day);
                dayMonthTextView=(TextView) itemView.findViewById(R.id.day_month);
                tempTextView=(TextView) itemView.findViewById(R.id.temp1);
                icon=(ImageView) itemView.findViewById(R.id.icon);
                father=(RelativeLayout) itemView.findViewById(R.id.fatherOfTheItem);
            }
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
