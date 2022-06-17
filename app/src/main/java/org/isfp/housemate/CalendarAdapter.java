package org.isfp.housemate;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>{
    ArrayList<LocalDate> dayList;

    public CalendarAdapter(ArrayList<LocalDate> dayList){
        this.dayList=dayList;
    }
    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent,int viewType){
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.calendar_cell,parent,false);
        return new CalendarViewHolder(view);

    }
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder,int position){
        LocalDate day=dayList.get(position);
        if(day==null){
            holder.dayText.setText("");
        }
        else{
            holder.dayText.setText(String.valueOf(day.getDayOfMonth()));

        }
        if(position==0||position%7==0){
            holder.dayText.setTextColor(Color.parseColor("#ffc0cb"));
        }//일요일색상바꾸기

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int iYear=day.getYear();
                int iMonth=day.getMonthValue();
                int iDay=day.getDayOfMonth();
                String yearMonDay=iYear+"년"+iMonth+"월"+iDay+"일";
                Toast.makeText(holder.itemView.getContext(), yearMonDay,Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public  int getItemCount(){
        return dayList.size();
    }
    class CalendarViewHolder extends RecyclerView.ViewHolder{
        TextView dayText;
        //View parentView;
        public CalendarViewHolder(@NonNull View itemView){
            super(itemView);
            dayText=itemView.findViewById(R.id.dayText);
            //parentView=itemView.findViewById(R.id.parentView);
        }
    }
}

