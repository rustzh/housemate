package org.isfp.housemate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TodayActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference houseworkRef;
    String userUid;

    TextView dateText;
    Integer year, month, day, checking;

    ListView lvList;
    TextView workname;
    ImageButton profile1;
    ImageButton profile2;
    ImageView check;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listc);


        Intent intent = getIntent();
        year = intent.getIntExtra("년", 0);
        month = intent.getIntExtra("월", 0);
        day = intent.getIntExtra("일", 0);

        checking=intent.getIntExtra("인증완료",0);

        lvList=(ListView)findViewById((R.id.lv_list));
        workname=(TextView)findViewById(R.id.workname);
        profile1=(ImageButton)findViewById(R.id.profile1);
        profile2=(ImageButton)findViewById(R.id.profile2);
        check=(ImageView)findViewById(R.id.check);


        check.setVisibility(View.INVISIBLE);// 숨기기

        if(checking==1){
            check.setVisibility(View.VISIBLE);
        }
        // profile1에 본인 사진


        // profile2에 친구 사진

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://housemate-6fa71-default-rtdb.firebaseio.com/");
        houseworkRef = database.getReference("user");
        userUid = auth.getCurrentUser().getUid();

        dateText = (TextView) findViewById(R.id.dateText);
        dateText.setText(year + "." + month + "." + day);

        workname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(view.getContext(), CameraActivity.class);
                startActivity(intent);

            }
        });
        profile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //내가 집안일을 선택했을때 친구한테 신호?가 가서 친구의 리스트는 비활성화 되게하기
            }
        });
        //만약 친구의 버튼이 눌렸을때 신호를 받아서 내 리스트가 비활성화 되게하기

    }



}