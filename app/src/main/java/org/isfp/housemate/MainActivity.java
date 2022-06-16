package org.isfp.housemate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.appsearch.GetByDocumentIdRequest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    Button logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://housemate-6fa71-default-rtdb.firebaseio.com/");
        System.out.println("getCurrentUSer = " + auth.getCurrentUser().getUid());

        if (auth.getCurrentUser() == null){
            System.out.println("go to sign in");
            Intent intent = new Intent(MainActivity.this, SignInAndUpActivity.class);
            startActivity(intent);
        }

        Button logOutButton = (Button) findViewById(R.id.logOutButton);
        logOutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(), SignInAndUpActivity.class);
                startActivity(intent);
            }
        });
    }
}