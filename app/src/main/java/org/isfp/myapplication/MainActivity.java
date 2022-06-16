package org.isfp.myapplication;


import androidx.appcompat.app.AppCompatActivity;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.zip.DataFormatException;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;




import java.time.format.DateTimeFormatter;
public class MainActivity extends AppCompatActivity {
    TextView monthYearText;//년월 텍스트뷰
    LocalDate selectedDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        monthYearText=findViewById(R.id.monthYearText);
        ImageButton prevBtn=findViewById(R.id.pre_btn);
        ImageButton nextBtn=findViewById(R.id.next_btn);
        //현재 날짜
        selectedDate=LocalDate.now();


        setMonthView();//화면설정

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate=selectedDate.minusMonths(1);
                setMonthView();
            }
        });//이전달 눌렀을때
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate=selectedDate.plusMonths(1);
                setMonthView();
            }
        });//다음달 눌렀을때
    }
    private String monthYearFromDate(LocalDate date){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("MM월 yyyy");
        return date.format(formatter);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
    }

}