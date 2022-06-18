package org.isfp.housemate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
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
    Integer year, month, day, count;

    ListView lvList;
    TextView workname;
    ImageButton profile1;
    ImageButton profile2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listc);


        Intent intent = getIntent();
        year = intent.getIntExtra("년", 0);
        month = intent.getIntExtra("월", 0);
        day = intent.getIntExtra("일", 0);

        lvList=(ListView)findViewById((R.id.lv_list));
        workname=(TextView)findViewById(R.id.workname);
        profile1=(ImageButton)findViewById(R.id.profile1);
        profile2=(ImageButton)findViewById(R.id.profile2);

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
    }



}