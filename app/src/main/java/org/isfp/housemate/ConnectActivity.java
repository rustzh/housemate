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
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("user");

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://housemate-6fa71-default-rtdb.firebaseio.com/");
        userRef = database.getReference("user");
        connectRef = database.getReference("connect");
        userUid = auth.getCurrentUser().getUid();
        System.out.println("userUid: " + userUid);

        inputMyNumber = (EditText) findViewById(R.id.inputMyNumber);
        inputFriendNumber = (EditText) findViewById(R.id.inputFreindNumber);
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
                    user.dataRoomNumber = myNumber+friendNumber;
                    user.number = myNumber;
                    user.friendNumber = friendNumber;
                    userRef.child(userUid).setValue(user);
                    SaveSharedPreference.setValue(ConnectActivity.this, "myNumber", myNumber);
                    SaveSharedPreference.setValue(ConnectActivity.this, "friendNumber", user.friendNumber);
                    SaveSharedPreference.setValue(ConnectActivity.this, "dataRoomNumber", user.dataRoomNumber);

                    finish();
                    Intent intent = new Intent(ConnectActivity.this, WaitingActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                } else {
                    Toast.makeText(ConnectActivity.this, "연결되었습니다.", Toast.LENGTH_LONG).show();
                    dataSnapshot.child(friendNumber).child("status").child("confirm").getRef().setValue("yes");
                    user.dataRoomNumber = friendNumber+myNumber;
                    user.number = myNumber;
                    user.friendNumber = friendNumber;
                    user.connectState = "yes";
                    userRef.child(userUid).setValue(user);
                    String profileURL = SaveSharedPreference.getStringValue(ConnectActivity.this, "profileURL");
                    database.getReference().child("dataRoom").child(friendNumber+myNumber).child(myNumber).setValue(profileURL);
                    SaveSharedPreference.setValue(ConnectActivity.this, "connectState", "yes");
                    SaveSharedPreference.setValue(ConnectActivity.this, "myNumber", myNumber);
                    SaveSharedPreference.setValue(ConnectActivity.this, "friendNumber", friendNumber);
                    SaveSharedPreference.setValue(ConnectActivity.this, "dataRoomNumber", friendNumber+myNumber);
                    // 완성된 메인화면으로 이동
                    finish();
                    Intent intent = new Intent(ConnectActivity.this, SettingActivity.class);
                    intent.putExtra("user", user);
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
