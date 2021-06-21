package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText mFName, mLName, mEmail, mPhone, mPassword;
    private Button mGo, mLogin;
    private DatabaseReference databaseReference;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFName = findViewById(R.id.fName);
        mLName = findViewById(R.id.lastName);
        mEmail = findViewById(R.id.email);
        mPhone = findViewById(R.id.phone);
        mPassword = findViewById(R.id.password);
        mGo = findViewById(R.id.btnSignup);
        mLogin = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();


        mLogin.setOnClickListener(new OnClickListener() {
            @Override

            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        mGo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String first_name = mFName.getText().toString().trim();
                String email = mEmail.getText().toString().trim();
                String last_name = mLName.getText().toString().trim();
                String phone = mPhone.getText().toString().trim();
                String pwd = mPassword.getText().toString().trim();

                if(TextUtils.isEmpty(first_name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(last_name)
                        || TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd)) {
                    Toast.makeText(Register.this, "ALl fields are required", Toast.LENGTH_SHORT).show();
                }

                else{
                    register(first_name,email,last_name,phone,pwd);
                }
            }
        });


    }

    private void register(String first_name, String email, String last_name, String phone, String pwd) {
       //progressBar.setVisibility(View.VISIBLE);
        //long timestamp = System.currentTimeMillis();
        mAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser rUser = mAuth.getCurrentUser();
                    String user_id = rUser.getUid();

                    HashMap<String,String> hashMap = new HashMap<>();
                    hashMap.put("userId", user_id);
                    hashMap.put("first_name", first_name);
                    hashMap.put("last_name", last_name);
                    hashMap.put("email", email);
                    hashMap.put("phone", phone);
                    hashMap.put("password", pwd);
                    //hashMap.put("timestamp", timestamp);

                    database = FirebaseDatabase.getInstance();
                    databaseReference = database.getReference("Users"); // //put data within hashmap in database
                    databaseReference.child(user_id).setValue(hashMap);

                    Toast.makeText(Register.this, "Registered...\n"+rUser.getEmail(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Register.this, Login.class));
                    finish();

                    //databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);
                    // setValue(hashMap);

//                    databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull  Task<Void> task) {
//
//                            if (task.isSuccessful()){
//                                Intent intent = new Intent(Register.this, Login.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                            }
//                            else{
//                                Toast.makeText(Register.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });

                }else{
                    Toast.makeText(Register.this, "Authentication failed", Toast.LENGTH_SHORT).show();


                }

            }
        });
    }
}