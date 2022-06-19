package org.isfp.housemate;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    DatabaseReference userRef;
    StorageReference userStorageRef;
    String userUid;
    EditText inputEmail;
    EditText inputPW;
    EditText inputName;
    ImageView inputProfile;
    Uri profileImageUri;
    String profilePathUri;
    File tempFile;
    public static final int PICK_FROM_ALBUM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://housemate-6fa71-default-rtdb.firebaseio.com/");
        storage = FirebaseStorage.getInstance("gs://housemate-6fa71.appspot.com");
        userRef = database.getReference("user");
        userStorageRef = storage.getReference("user/");

        inputProfile = (ImageView) findViewById(R.id.inputProfile);

        inputProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });
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
        if (inputProfile == null){
            Toast.makeText(this, "프로필 사진을 등록해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();
                    FirebaseUser fuser = auth.getCurrentUser();
                    userUid = fuser.getUid();

                    final Uri file = Uri.fromFile(new File(profilePathUri));

                    userStorageRef.child("uid/"+file.getLastPathSegment()).putFile(profileImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            final Task<Uri> imageUri = task.getResult().getStorage().getDownloadUrl();
                            while (!imageUri.isComplete());

                            org.isfp.housemate.User user = new org.isfp.housemate.User(userUid, name, email, password, imageUri.getResult().toString());
                            userRef.child(userUid).setValue(user);

                            SaveSharedPreference.setValue(SignUpActivity.this, "id", userUid);
                            SaveSharedPreference.setValue(SignUpActivity.this, "name", name);
                            SaveSharedPreference.setValue(SignUpActivity.this, "profileURL", imageUri.getResult().toString());

                            Intent intent = new Intent(SignUpActivity.this, ConnectActivity.class);
                            intent.putExtra("user", user);
                            startActivity(intent);
                        }
                    });
                }
                else {
                    Toast.makeText(SignUpActivity.this, "이미 등록된 이메일입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) { // 코드가 틀릴경우
            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        tempFile = null;
                    }
                }
            }
            return;
        }

        switch (requestCode) {
            case PICK_FROM_ALBUM: { // 코드 일치
                // Uri
                profileImageUri = data.getData();
                profilePathUri = getPath(data.getData());
                inputProfile.setImageURI(profileImageUri);
//                inputProfile.setImageDrawable();// 이미지 띄움
                break;
            }
        }
    }

    public String getPath(Uri uri){
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();
        return cursor.getString(index);
    }
}