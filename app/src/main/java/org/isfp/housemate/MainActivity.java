package org.isfp.housemate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.appsearch.GetByDocumentIdRequest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
//    Button logOutButton;

    TextView monthYearText;//년월 텍스트뷰
    LocalDate selectedDate;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        monthYearText = findViewById(R.id.monthYearText);
        ImageButton prevBtn = findViewById(R.id.pre_btn);
        ImageButton nextBtn = findViewById(R.id.next_btn);
        recyclerView=findViewById(R.id.recyclerView);
        //현재 날짜
        selectedDate = LocalDate.now();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://housemate-6fa71-default-rtdb.firebaseio.com/");
        System.out.println("getCurrentUSer = " + auth.getCurrentUser().getUid());

        if (auth.getCurrentUser() == null) {
            System.out.println("go to sign in");
            Intent intent = new Intent(MainActivity.this, SignInAndUpActivity.class);
            startActivity(intent);
        }

        setMonthView();//화면설정

        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.minusMonths(1);
                setMonthView();
            }
        });//이전달 눌렀을때
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDate = selectedDate.plusMonths(1);
                setMonthView();
            }
        });//다음달 눌렀을때
    }

    private String monthYearFromDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM월 yyyy");
        return date.format(formatter);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(selectedDate));
        ArrayList<String> dayList=daysInMonthList(selectedDate);
        CalendarAdapter adapter=new CalendarAdapter(dayList);
        RecyclerView.LayoutManager manager=new GridLayoutManager(getApplicationContext(),7);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<String> daysInMonthList(LocalDate date) {
        ArrayList<String> dayList = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int lastDay = yearMonth.lengthOfMonth();
        LocalDate firstDay = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstDay.getDayOfWeek().getValue();

        for (int i = 1; i < 42; i++) {
            if (i <= dayOfWeek || i > lastDay + dayOfWeek) {
                dayList.add("");

            } else {
                dayList.add(String.valueOf(i - dayOfWeek));
            }
        }
        return dayList;

    }

}
// 로그아웃 버튼
//    Button logOutButton = (Button) findViewById(R.id.logOutButton);
//        logOutButton.setOnClickListener(new View.OnClickListener(){
//@Override
//public void onClick(View view){
//        auth.signOut();
//        Intent intent = new Intent(getApplicationContext(), SignInAndUpActivity.class);
//        startActivity(intent);
//        }
//        });