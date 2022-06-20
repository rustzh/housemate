package org.isfp.housemate;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.file.Watchable;

public class WaitingActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myNumRef;
    DatabaseReference userRef;
    String userUid;
    String myNumber;
    String friendNumber;
    String tmpDataRoom;
    String connectState;
    String profileURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        myNumber = SaveSharedPreference.getStringValue(WaitingActivity.this, "myNumber");
        friendNumber = SaveSharedPreference.getStringValue(WaitingActivity.this, "friendNumber");
        tmpDataRoom = SaveSharedPreference.getStringValue(WaitingActivity.this, "dataRoomNumber");
        profileURL = SaveSharedPreference.getStringValue(WaitingActivity.this, "profileURL");

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://housemate-6fa71-default-rtdb.firebaseio.com/");
        myNumRef = database.getReference("connect").child(myNumber).child("status");
        userRef = database.getReference("user");

        userUid = auth.getCurrentUser().getUid();


        myNumRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if ("yes".equals(snapshot.getValue(String.class))) {
                        connectState = "yes";
                        userRef.child(userUid).child("connectState").setValue("yes");
                        Toast.makeText(WaitingActivity.this, "연결되었습니다", Toast.LENGTH_SHORT).show();
                        SaveSharedPreference.setValue(WaitingActivity.this, "connectState", "yes");

                        finish();
                        Intent intent = new Intent(WaitingActivity.this, SettingActivity.class);
                        startActivity(intent);
                    }
                    Toast.makeText(WaitingActivity.this, snapshot.getValue(String.class), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
