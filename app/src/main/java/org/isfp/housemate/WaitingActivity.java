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

public class WaitingActivity extends AppCompatActivity {
//    EditText myNum_edit;
//    EditText friendNum_edit;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference myNumRef;
    DatabaseReference userRef;
    String userUid;
    String myNumber;
    String friendNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        Intent intent = getIntent();
        myNumber = intent.getStringExtra("myNumber");
        friendNumber = intent.getStringExtra("friendNumber");

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://housemate-6fa71-default-rtdb.firebaseio.com/");
        myNumRef = database.getReference("connect").child(myNumber).child("status");
        userRef = database.getReference("user");

        userUid = auth.getCurrentUser().getUid();
        System.out.println("userUid: " + userUid);
        System.out.println("userUid: " + userUid);
        System.out.println("userUid: " + userUid);
        System.out.println("userUid: " + userUid);


        myNumRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if ("yes".equals(snapshot.getValue(String.class))) {
                        userRef.child(userUid).child("connect").setValue("yes");
                        userRef.child(userUid).child("friendNumber").setValue(friendNumber);
                        Toast.makeText(WaitingActivity.this, "연결되었습니다", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(WaitingActivity.this, SettingActivity.class);
                        intent.putExtra("dataRoom", myNumber+friendNumber);
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
