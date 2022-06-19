package org.isfp.housemate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference userRef;
    EditText loginEmail;
    EditText loginPW;
    Button signInCompleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://housemate-6fa71-default-rtdb.firebaseio.com/");
        userRef = database.getReference("user");

        loginEmail = (EditText) findViewById(R.id.loginEmail);
        loginPW = (EditText) findViewById(R.id.loginPW);
        signInCompleteButton = (Button) findViewById(R.id.signInCompleteButton);
    }

    public void signin(View view){
        final String email = loginEmail.getText().toString();
        final String password = loginPW.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(this, "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // 로그인 시 정보 user에 저장하는 것도 구현해야 함
                    Toast.makeText(SignInActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    FirebaseUser fuser = auth.getCurrentUser();
                    String userUid = fuser.getUid();
                    DatabaseReference userUidRef = userRef.child(userUid).getRef();
                    userUidRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if ("yes".equals(snapshot.getValue(String.class))) {
                                    Toast.makeText(SignInActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                                    // 사용자 정보 불러와서 저장
                                    String dataRoomNumber = dataSnapshot.child("dataRoomNumber").getValue(String.class);
                                    String myNumber = dataSnapshot.child("myNumber").getValue(String.class);
                                    String friendNumber = dataSnapshot.child("friendNumber").getValue(String.class);
                                    String name = dataSnapshot.child("name").getValue(String.class);
                                    String profileURL = dataSnapshot.child("profileImageURL").getValue(String.class);
                                    String connectState = dataSnapshot.child("connectState").getValue(String.class);
                                    SaveSharedPreference.setValue(SignInActivity.this, "id", userUid);
                                    SaveSharedPreference.setValue(SignInActivity.this, "name", name);
                                    SaveSharedPreference.setValue(SignInActivity.this, "profileURL", profileURL);
                                    SaveSharedPreference.setValue(SignInActivity.this, "myNumber", myNumber);
                                    SaveSharedPreference.setValue(SignInActivity.this, "friendNumber", friendNumber);
                                    SaveSharedPreference.setValue(SignInActivity.this, "connectState", connectState);
                                    SaveSharedPreference.setValue(SignInActivity.this, "dataRoomNumber", dataRoomNumber);

                                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
                                else{

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(SignInActivity.this, "존재하지 않는 이메일이거나 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    public interface Callback{
//        void success(String data);
//    }
//
//    public String loadData(final Callback callback, FirebaseUser user){
//        userRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                callback.success(snapshot.child(user.getUid()).child("connect").getValue(String.class));
//                String data = snapshot.child(user.getUid()).child("connect").getValue(String.class);
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        return data;
//    }
}
