package org.isfp.housemate;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.LinearLayout;

import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

public class TodayActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference dataRoomRef;
    DatabaseReference houseworkRef;
    DatabaseReference profileRef;
    DatabaseReference dateRef;
    DatabaseReference tmpDateRef;

    TextView dateText;
    Integer year, month, day, checking;


    ListView lvList;
    TextView workname;
    ImageButton profile1;
    ImageButton profile2;
    ImageView check;
    Button back;

    String[] houseworks = new String[7];
    String[] completeState = new String[7];
    String[] tmpDuty = new String[7];
    String dataRoomNumber;
    String url1;
    String url2;
    String myProfileURL=SaveSharedPreference.getStringValue(TodayActivity.this, "profileURL");;
    //String myProfileURL = SaveSharedPreference.getStringValue(TodayActivity.this, "profileURL");
//    String tempURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);
        Intent intent = getIntent();
        year = intent.getIntExtra("년", 0);
        month = intent.getIntExtra("월", 0);
        day = intent.getIntExtra("일", 0);


        checking=intent.getIntExtra("인증완료",0);




        check.setVisibility(View.INVISIBLE);// 숨기기

        if(checking==1){
            check.setVisibility(View.VISIBLE);
        }
        // profile1에 본인 사진


        // profile2에 친구 사진


//        houseworkListView=(ListView)findViewById((R.id.houseworkListView));
//        workname=(TextView)findViewById(R.id.workname);
//        profile1=(ImageButton)findViewById(R.id.profile1);
//        profile2=(ImageButton)findViewById(R.id.profile2);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://housemate-6fa71-default-rtdb.firebaseio.com/");
        houseworkRef = database.getReference("user");
        dataRoomRef = database.getReference("dataRoom");
        dataRoomNumber = SaveSharedPreference.getStringValue(TodayActivity.this, "dataRoomNumber");

        dateText = (TextView) findViewById(R.id.dateText);
        dateText.setText(year + "." + month + "." + day);

        // 현재 날짜 상태를 저장할 Ref 설정 (한 번 세팅하면 다시 세팅하지 않음)
        String date = String.valueOf(year) + String.valueOf(month) + String.valueOf(day);
        dateRef = dataRoomRef.child(dataRoomNumber).child("date").getRef();
        dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(date)) {
                    Integer houseworkCount = SaveSharedPreference.getIntegerValue(TodayActivity.this, "houseworkCount");
                    for (int i=0; i< houseworkCount; i++){
                        dateRef.child(date).child(SaveSharedPreference.getStringValue(TodayActivity.this, "housework"+String.valueOf(i))).child("complete").setValue("none");
                        dateRef.child(date).child(SaveSharedPreference.getStringValue(TodayActivity.this, "housework"+String.valueOf(i))).child("duty").setValue("default");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        profile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //내가 집안일을 선택했을때 친구한테 신호?가 가서 친구의 리스트는 비활성화 되게하기
            }
        });
        //만약 친구의 버튼이 눌렸을때 신호를 받아서 내 리스트가 비활성화 되게하기

        tmpDateRef = dateRef.child(date).getRef();

                // 둘의 사진 다운 링크 불러와
        profileRef = dataRoomRef.child(dataRoomNumber).child("profileURL").getRef();
        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String[] urlArray = new String[2];
                Integer i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                     urlArray[i] = snapshot.getValue(String.class);
                     i++;
//                    System.out.println(url);
//                    SaveSharedPreference.setValue(MainActivity.this, "friendProfileURL", url);
                }
                url1 = urlArray[0];
                url2 = urlArray[1];
                System.out.println(url1);
                System.out.println(url2);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // 집안일 받아와서 뷰그룹 생성 (인덱스 잘 생각하기!! 첫 번째는 인덱스 0)
        houseworkRef = dataRoomRef.child(dataRoomNumber).child("housework").getRef();
        houseworkRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Integer i = 0;
                LinearLayout layout = (LinearLayout) findViewById(R.id.listViewLayout);

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    houseworks[i] = snapshot.getValue(String.class);
                    System.out.println(houseworks[i]);
                    SaveSharedPreference.setValue(TodayActivity.this, "housework"+String.valueOf(i), houseworks[i]);

                    RelativeLayout houseworkLayout = new RelativeLayout(TodayActivity.this);
                    final int height1 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                    LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height1);
                    final int top1 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                    param.setMargins (0, top1, 0, 0);
                    houseworkLayout.setLayoutParams(param);
                    houseworkLayout.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_bg));
                    layout.addView(houseworkLayout);

                    // 체크 표시 추가
                    ImageView check = new ImageView(TodayActivity.this);
                    final int checkSize = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics());
                    RelativeLayout.LayoutParams p1 = new RelativeLayout.LayoutParams(checkSize, checkSize);
                    final int top2 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
                    final int left2 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics());
                    p1.setMargins(left2, top2, 0, 0);
                    check.setLayoutParams(p1);
                    check.setId(i);
                    check.setBackgroundDrawable(getResources().getDrawable(R.drawable.check));
                    check.setVisibility(INVISIBLE);
                    houseworkLayout.addView(check);
                    Integer finalI = i;
                    tmpDateRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            completeState[finalI] = dataSnapshot.child(houseworks[finalI]).child("complete").getValue(String.class);
                            if (completeState[finalI].equals("yes")){
                                check.setVisibility(VISIBLE);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    
                    // 집안일 텍스트 추가
                    TextView houseworkName = new TextView(TodayActivity.this);
                    RelativeLayout.LayoutParams p2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    final int top3 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 13, getResources().getDisplayMetrics());
                    final int left3 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics());
                    p2.setMargins(left3, top3, 0, 0);
                    houseworkName.setLayoutParams(p2);
                    houseworkName.setId(i);
                    houseworkName.setText(houseworks[i]);
                    houseworkName.setTextSize(30);
                    houseworkName.setIncludeFontPadding(false);
                    houseworkName.setTextColor(Color.WHITE);
                    houseworkLayout.addView(houseworkName);
                    Integer finalI2 = i;
                    tmpDateRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            completeState[finalI2] = dataSnapshot.child(houseworks[finalI2]).child("complete").getValue(String.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    // 최우선 if문: duty가 default이면 Toast 띄우고 return
                    // 선택된 집안일이 complete 상태가 아닐 때 duty가 profileURL인 애만 누를 수 있음
                    houseworkName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int i = view.getId();
                            if(tmpDuty[i].equals("default")){
                                Toast.makeText(TodayActivity.this, "오른쪽 버튼으로 누가 할지 정해 주세요", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else if(completeState[i].equals("none")){
                                if(tmpDuty[i].equals(myProfileURL)){
                                    // 카메라로 연결
                                    Intent intent = new Intent(TodayActivity.this, CameraActivity.class);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(TodayActivity.this, "이 집안일을 맡은 사람이 아닙니다", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            else { // 집안일이 완료된 상황
                                // 저장된 사진을 확인할 수 있음. 카메라로 연결
                            }
                        }
                    });

                    // 담당을 정할 이미지 버튼 추가
                    ImageButton profileButton = new ImageButton(TodayActivity.this);
                    final int width4 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
                    final int height4 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 55, getResources().getDisplayMetrics());
                    RelativeLayout.LayoutParams p3 = new RelativeLayout.LayoutParams(width4, height4);
                    final int left4 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 330, getResources().getDisplayMetrics());
                    p3.setMargins(left4, 0, 0, 0);
                    profileButton.setLayoutParams(p3);
                    profileButton.setId(i);
                    profileButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.default_profile));
                    houseworkLayout.addView(profileButton);
                    Integer finalI3 = i;
                    tmpDateRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            tmpDuty[finalI3] = dataSnapshot.child(houseworks[finalI3]).child("duty").getValue(String.class);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    profileButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // duty가 default면 url1, url1이면 url2, url2이면 url1
                            int i = view.getId();
                            if(tmpDuty[i].equals("default")){
                                Glide.with(TodayActivity.this).load(url1).into(profileButton);
                                tmpDuty[i] = url1;
                                tmpDateRef.child(houseworks[i]).child("duty").setValue(url1);
                            }
                            else if(tmpDuty[i].equals(url1)){
                                Glide.with(TodayActivity.this).load(url2).into(profileButton);
                                tmpDuty[i] = url2;
                                tmpDateRef.child(houseworks[i]).child("duty").setValue(url2);
                            }
                            else if(tmpDuty[i].equals(url2)){
                                Glide.with(TodayActivity.this).load(url1).into(profileButton);
                                tmpDuty[i] = url1;
                                tmpDateRef.child(houseworks[i]).child("duty").setValue(url1);
                            }
                        }
                    });

                    i++;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // profile1에 본인 사진
//        String profile1_url = SaveSharedPreference.getStringValue(TodayActivity.this,"profileURL");
//        Glide.with(this).load(profile1_url).into(profile1);

        // profile2에 친구 사진
//        String profile2_url = SaveSharedPreference.getStringValue(TodayActivity.this,"friendProfileURL");
//        Glide.with(this).load(profile2_url).into(profile2);

//        workname.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Context context = view.getContext();
//                Intent intent = new Intent(view.getContext(), CameraActivity.class);
//                startActivity(intent);
//
//            }
//        });

    }
}