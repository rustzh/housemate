package org.isfp.housemate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.appsearch.GetByDocumentIdRequest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity  {
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference dataRoomRef;
    DatabaseReference friendProfileRef;
    DatabaseReference houseworkRef;

    TextView monthYearText;//년월 텍스트뷰
    RecyclerView recyclerView;
    LocalDate selectedDate;

    String friendNumber;
    String dataRoomNumber;

//    Integer houseworkCount = SaveSharedPreference.getIntegerValue(MainActivity.this, "houseworkCount");
//    Integer houseworkCount = 2;
//    String[] houseworks = new String[houseworkCount];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://housemate-6fa71-default-rtdb.firebaseio.com/");
        dataRoomRef = database.getReference("dataRoom");

        // 사용자의 설정 확인 후 설정해야 하는 곳으로 이동
        if(SaveSharedPreference.getStringValue(MainActivity.this, "name").length() == 0) {
            Intent intent2 = new Intent(MainActivity.this, SignInAndUpActivity.class);
            startActivity(intent2);
            this.finish();
            return;
        }
        else if(SaveSharedPreference.getStringValue(MainActivity.this, "myNumber").length() == 0) {
            System.out.println("myNumber = " + SaveSharedPreference.getStringValue(MainActivity.this, "myNumber"));
            Intent intent2 = new Intent(MainActivity.this, ConnectActivity.class);
            startActivity(intent2);
            this.finish();
            return;
        }
        else if(SaveSharedPreference.getStringValue(MainActivity.this, "connectState").length() == 0) {
            System.out.println("connectState = " + SaveSharedPreference.getStringValue(MainActivity.this, "connectState"));
            Intent intent2 = new Intent(MainActivity.this, WaitingActivity.class);
            startActivity(intent2);
            this.finish();
            return;
        }
        else if(SaveSharedPreference.getStringValue(MainActivity.this, "settingState").length() == 0) {
            System.out.println("settingState = " + SaveSharedPreference.getStringValue(MainActivity.this, "settingState"));
            Intent intent2 = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent2);
            this.finish();
            return;
        }

//        // 친구 프로필 URL를 받아와서 저장
//        friendNumber = SaveSharedPreference.getStringValue(MainActivity.this, "friendNumber");
//        dataRoomNumber = SaveSharedPreference.getStringValue(MainActivity.this, "dataRoomNumber");
//        System.out.println("friendNumber: " + friendNumber + " and dataRoomNumber: " + dataRoomNumber);
//        friendProfileRef = dataRoomRef.child(dataRoomNumber).getRef();
//        friendProfileRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String url = dataSnapshot.child(friendNumber).getValue(String.class);
//                System.out.println(url);
//                SaveSharedPreference.setValue(MainActivity.this,"friendProfileURL", url);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

        // DB에 저장된 집안일 불러와서 배열로 저장 배열 이름: houseworks[]
        dataRoomNumber = SaveSharedPreference.getStringValue(MainActivity.this, "dataRoomNumber");
        houseworkRef = dataRoomRef.child(dataRoomNumber).child("housework").getRef();
        houseworkRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String str = snapshot.getValue(String.class);
                    SaveSharedPreference.setValue(MainActivity.this, "housework"+String.valueOf(i), str);
                    i++;
                }
                SaveSharedPreference.setValue(MainActivity.this, "houseworkCount", i);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        System.out.println(SaveSharedPreference.getStringValue(MainActivity.this, "housework0"));
        System.out.println(SaveSharedPreference.getStringValue(MainActivity.this, "housework1"));


        monthYearText = findViewById(R.id.monthYearText);
        ImageButton prevBtn = findViewById(R.id.pre_btn);
        ImageButton nextBtn = findViewById(R.id.next_btn);
        recyclerView=findViewById(R.id.recyclerView);

        //현재 날짜
        selectedDate = LocalDate.now();

        setMonthView();//화면설정

        Button logOutButton = (Button) findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                auth.signOut();
                SaveSharedPreference.clearUser(MainActivity.this);
                finish();
                Intent intent = new Intent(getApplicationContext(), SignInAndUpActivity.class);
                startActivity(intent);
            }
        });

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
        ArrayList<LocalDate> dayList=daysInMonthArray(selectedDate);
        CalendarAdapter adapter = new CalendarAdapter(dayList, MainActivity.this);
        RecyclerView.LayoutManager manager=new GridLayoutManager(getApplicationContext(),7);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private ArrayList<LocalDate> daysInMonthArray(LocalDate date) {
        ArrayList<LocalDate> dayList = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        int lastDay = yearMonth.lengthOfMonth();
        LocalDate firstDay = selectedDate.withDayOfMonth(1);
        int dayOfWeek = firstDay.getDayOfWeek().getValue();

        for (int i = 1; i < 42; i++) {
            if (i <= dayOfWeek || i > lastDay + dayOfWeek) {
                dayList.add(null);

            } else {
                dayList.add(LocalDate.of(selectedDate.getYear(),selectedDate.getMonth(),i- dayOfWeek));
            }
        }
        return dayList;
    }
}
    //public void onItemClick(String dayText){
    //    String yearMonDay=monthYearFromDate(CalendarUtil.selectedDate)+" "+dayText+"일";
        //Toast.makeText(this,yearMonDay,Toast.LENGTH_SHORT).show();
    //}



