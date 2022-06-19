package org.isfp.housemate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference userRef;
    DatabaseReference dataRoomRef;
    FirebaseUser fuser;

    ListView houseworklistView;
    ArrayList<String> list = new ArrayList<String>();
    Button AddButton;
    Button DelButton;
    Button settingButton;
    ArrayAdapter<String> adapter;
    String tmpDataRoom;
    String userUid;
//    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

//        Intent intent = getIntent();
//        user = (User)intent.getSerializableExtra("user");

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://housemate-6fa71-default-rtdb.firebaseio.com/");
        userRef = database.getReference("user");
        dataRoomRef = database.getReference("dataRoom");
        fuser = auth.getCurrentUser();

        houseworklistView = (ListView) findViewById(R.id.HouseworklistView);
        AddButton = (Button) findViewById(R.id.AddButton);
        DelButton = (Button) findViewById(R.id.DelButton);
        settingButton = (Button) findViewById(R.id.settingButton);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, list);
        houseworklistView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        houseworklistView.setAdapter(adapter);

        userUid = SaveSharedPreference.getStringValue(SettingActivity.this, "id");
        tmpDataRoom = SaveSharedPreference.getStringValue(SettingActivity.this, "dataRoomNumber");
        DatabaseReference myRoomRef = dataRoomRef.child(tmpDataRoom).getRef();
        myRoomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("housework")){
                    Toast.makeText(SettingActivity.this, "상대방이 설정을 완료했습니다.", Toast.LENGTH_SHORT).show();
                    SaveSharedPreference.setValue(SettingActivity.this, "settingState", "yes");
                    Intent intent = new Intent(SettingActivity.this, MainActivity.class);
//                    intent.putExtra("user", user);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        houseworklistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int i, long id) {
                String item = list.get(i);
            }
        });

        final EditText inputHousework = (EditText) findViewById(R.id.inputHousework);

        AddButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int count = adapter.getCount();


                if (count == 7){
                    Toast.makeText(SettingActivity.this,"최대 7개까지 가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

//                intent.putExtra("리스트", count);
                String input = inputHousework.getText().toString();
                list.add(input);
                adapter.notifyDataSetChanged();
                inputHousework.setText("");
            }
        });

        DelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = houseworklistView.getCheckedItemPosition();
                list.remove(position);
                adapter.notifyDataSetChanged();
                houseworklistView.clearChoices();
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Object object : list){
                    String value = (String) object;
                    dataRoomRef.child(tmpDataRoom).child("housework").child(value).setValue(value);
                    userRef.child(userUid).child("settingState").setValue("yes");
                    SaveSharedPreference.setValue(SettingActivity.this, "settingState", "yes");
                    Toast.makeText(SettingActivity.this, "설정 완료", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SettingActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
