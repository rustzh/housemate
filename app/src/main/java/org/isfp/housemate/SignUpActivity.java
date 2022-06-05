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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    //String tempKey;

    private DocumentReference userRef = FirebaseFirestore.getInstance().document("sampleData/inspiration");


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference userRef = database.getReference("user");

        //initView();
    }

    public void signup(View view){
        EditText inputEmail = (EditText) findViewById(R.id.inputEmail);
        EditText inputPW = (EditText) findViewById(R.id.inputPW);
        final String email = inputEmail.getText().toString();
        final String password = inputPW.getText().toString();

        if (email.isEmpty() || password.isEmpty()){ return;}
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("email", email);
        dataToSave.put("password", password);
        userRef.set(dataToSave).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();
                    Log.d("signup", "회원가입 성공");
                } else{

                    Toast.makeText(SignUpActivity.this, "회원가입 실패!", Toast.LENGTH_SHORT).show();
                    Log.w("signup", "회원가입 실패", task.getException());
                }
            }
        });

        Intent intent = new Intent(getApplicationContext(), ConnectActivity.class);
        startActivity(intent);
    }
}
