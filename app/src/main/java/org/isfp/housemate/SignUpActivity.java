package org.isfp.housemate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference userRef;
    EditText inputEmail;
    EditText inputPW;
    EditText inputName;
    File 


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://housemate-6fa71-default-rtdb.firebaseio.com/");
        userRef = database.getReference("user");
    }

    public void signup(View view){
        inputEmail = (EditText) findViewById(R.id.inputEmail);
        inputPW = (EditText) findViewById(R.id.inputPW);
        inputName = (EditText) findViewById(R.id.inputName);
        final String email = inputEmail.getText().toString();
        final String password = inputPW.getText().toString();
        final String name = inputName.getText().toString();

        if (name.isEmpty()){
            Toast.makeText(this, "이름을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty()){
            Toast.makeText(this, "이메일을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()){
            Toast.makeText(this, "비밀번호를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();
                    FirebaseUser user = auth.getCurrentUser();
                    System.out.println("userUid: " + user.getUid());

                    Intent intent = new Intent(SignUpActivity.this, ConnectActivity.class);
                    startActivity(intent);

//                    String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                    userRef.child(user.getUid()).child("email").setValue(email);
                    userRef.child(user.getUid()).child("password").setValue(password);
                    userRef.child(user.getUid()).child("name").setValue(name);
                }
                else {
                    Toast.makeText(SignUpActivity.this, "이미 등록된 이메일입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}