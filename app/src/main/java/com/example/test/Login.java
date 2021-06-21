package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
     EditText mEmail, mPassword;
     Button mRegister, mMainLogin;
     TextView mForgot;
     FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.Email);
        mPassword = findViewById(R.id.password);
        mRegister = findViewById(R.id.btnRegister);
        mMainLogin = findViewById(R.id.btnMainLogin);
        mForgot = findViewById(R.id.btnForgot);

        mAuth = FirebaseAuth.getInstance();

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        mMainLogin.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                String tex_email = mEmail.getText().toString();
                String tex_pwd = mPassword.getText().toString();
                if(TextUtils.isEmpty(tex_email) || TextUtils.isEmpty(tex_pwd)) {
                    Toast.makeText(Login.this, "ALl fields are required", Toast.LENGTH_SHORT).show();
                }
                else{
                    login(tex_email,tex_pwd);
                }
            }
        });

    }

    private void login(String tex_email, String tex_pwd) {
        mAuth.signInWithEmailAndPassword(tex_email,tex_pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent = new Intent(Login.this, Menu.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}