package com.example.christophe.weatherdemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import Model.Main;
import Model.jsonHead;

import static java.lang.System.load;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{

    private jsonHead WeatherInfo;
    public RecyclerAdapter(jsonHead WeatherInfo ){
        this.WeatherInfo=WeatherInfo;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

            holder.temp.setText(WeatherInfo.getList().get(position).getMain().getTemp()+" °C");
            holder.humd.setText(WeatherInfo.getList().get(position).getMain().getHumidity()+" %");
            holder.wind.setText(WeatherInfo.getList().get(position).getWind().getSpeed()+" Km/h");
            holder.date.setText(WeatherInfo.getList().get(position).getDtTxt());
            holder.description.setText(WeatherInfo.getList().get(position).getWeather().get(0).getDescription());




         String icon  =WeatherInfo.getList().get(position).getWeather().get(0).getIcon();
         String iconUrl = "http://openweathermap.org/img/w/" + icon + ".png";
         Picasso.get().load(iconUrl).into(holder.weathericon);

    }

    @Override
    public int getItemCount() {
        return WeatherInfo.getList().size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView temp,humd,wind,date,description;
        ImageView weathericon;

        public MyViewHolder(View itemView) {
            super(itemView);
            temp=(TextView)itemView.findViewById(R.id.textViewTemp);
            humd=(TextView)itemView.findViewById(R.id.textViewHumidity);
            wind=(TextView)itemView.findViewById(R.id.textViewWind);
            date=(TextView)itemView.findViewById(R.id.textViewDate);
            description=(TextView)itemView.findViewById(R.id.textViewDescription);
            weathericon=(ImageView)itemView.findViewById((R.id.weathericon));
        }
    }
}
