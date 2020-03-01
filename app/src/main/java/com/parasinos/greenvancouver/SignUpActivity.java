package com.parasinos.greenvancouver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private Button signUpBtn;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        
        signUpBtn = findViewById(R.id.signup_btn);
        loginBtn = findViewById(R.id.signup_login_btn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailStr = email.getText().toString();
                String passStr = password.getText().toString();

                if (TextUtils.isEmpty(emailStr)) {
                    Toast.makeText(getApplicationContext(),
                            "Email field is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passStr)) {
                    Toast.makeText(getApplicationContext(), "Password field is empty",
                            Toast.LENGTH_SHORT).show();
                }

                mAuth.createUserWithEmailAndPassword(emailStr,passStr)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                    finish();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),
                                            "E-mail or password is wrong",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

        if(mAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

    }

}
