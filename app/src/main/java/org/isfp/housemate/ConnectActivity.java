package org.isfp.housemate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Ref;

public class ConnectActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference userRef;
    DatabaseReference connectRef;
    EditText inputMyNumber;
    EditText inputFriendNumber;
    String userUid;
    String connectState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        Intent intent = getIntent();
        connectState = intent.getStringExtra("connectState");
        System.out.println("받은 connectState = " + connectState);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://housemate-6fa71-default-rtdb.firebaseio.com/");
        userRef = database.getReference("user");
        connectRef = database.getReference("connect");
        userUid = auth.getCurrentUser().getUid();
        System.out.println("userUid: " + userUid);

        inputMyNumber = (EditText) findViewById(R.id.inputMyNumber);
        inputFriendNumber = (EditText) findViewById(R.id.inputFreindNumber);

//        if (auth.getCurrentUser() != null){
//            System.out.println("if 문 들어가기 전. connectState = " + connectState);
//            if (connectState.equals("yes")){
//                System.out.println("go to TestActivity");
//                finish();
//                Intent intent2 = new Intent(ConnectActivity.this, MainActivity.class);
//                startActivity(intent2);
//            }
//            else {
//            }
//        }

//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            tempkey = bundle.getString("tempkey");
//        }
    }

    public void onConnect(View view) {
        final String myNumber = inputMyNumber.getText().toString();
        final String friendNumber = inputFriendNumber.getText().toString();

        connectRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(friendNumber)) {
                    Toast.makeText(ConnectActivity.this, "연결을 기다립니다.", Toast.LENGTH_LONG).show();
                    dataSnapshot.child(myNumber).child("friendNumber").getRef().setValue(friendNumber);
                    dataSnapshot.child(myNumber).child("status").child("confirm").getRef().setValue("none");
                    userRef.child(userUid).child("myNumber").setValue(myNumber);
                    // 여기서 두 유저의 데이터를 저장할 곳을 만듦

                    Intent intent = new Intent(ConnectActivity.this, WaitingActivity.class);
                    intent.putExtra("myNumber", myNumber);
                    intent.putExtra("friendNumber", friendNumber);
                    startActivity(intent);
                } else {
                    Toast.makeText(ConnectActivity.this, "연결되었습니다.", Toast.LENGTH_LONG).show();
                    dataSnapshot.child(friendNumber).child("status").child("confirm").getRef().setValue("yes");
                    userRef.child(userUid).child("connect").setValue("yes");
                    userRef.child(userUid).child("myNumber").setValue(myNumber);
                    userRef.child(userUid).child("friendNumber").setValue(friendNumber);
                    // 완성된 메인화면으로 이동
                    Intent intent = new Intent(ConnectActivity.this, MainActivity.class);
                    intent.putExtra("myNumber", myNumber);
                    intent.putExtra("friendNumber", friendNumber);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ConnectActivity.this, "오류", Toast.LENGTH_LONG).show();
            }
        });
    }
}
