package com.parasinos.greenvancouver;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.parasinos.greenvancouver.models.User;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private EditText name;
    private EditText email;
    private EditText confirmPw;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();
        mAuth = FirebaseAuth.getInstance();

        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        confirmPw = findViewById(R.id.signup_confirm_password);

        Button signUpBtn = findViewById(R.id.signup_btn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString();
                String passStr = password.getText().toString();
                final String nameStr = name.getText().toString();
                String confPassStr = confirmPw.getText().toString();

                if (TextUtils.isEmpty(emailStr)) {
                    Toast.makeText(getApplicationContext(),
                            "Email field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(passStr)) {
                    Toast.makeText(getApplicationContext(), "Password field is empty",
                            Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(nameStr)) {
                    Toast.makeText(getApplicationContext(), "Please enter your name",
                            Toast.LENGTH_SHORT).show();
                }

                if (TextUtils.isEmpty(confPassStr)) {
                    Toast.makeText(getApplicationContext(), "Please confirm your password",
                            Toast.LENGTH_SHORT).show();
                }

                if (!confPassStr.equals(passStr)) {
                    Toast.makeText(getApplicationContext(),
                            "Passwords do not match. Please try again.", Toast.LENGTH_SHORT).show();
                }

                mAuth.createUserWithEmailAndPassword(emailStr, passStr)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    User newUser = new User(nameStr, "");
                                    String user_id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                                    String path = String.join("/", "users", user_id, "basicInfo");
                                    FirebaseDatabase.getInstance().getReference(path).setValue(newUser);

                                    finish();
                                    Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "E-mail or password is wrong",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        if (mAuth.getCurrentUser() != null) {
            finish();
        }
    }
}
