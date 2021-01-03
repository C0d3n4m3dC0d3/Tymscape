package com.asas.tymscape;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogIn extends AppCompatActivity {
    private static final String TAG = "LogIn";
    private com.google.android.material.textfield.TextInputLayout e, pd;
    private Button login_btn, signup_btn;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();
        login_btn = (Button)findViewById(R.id.login_btn1);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e = findViewById(R.id.emailid);
                pd = findViewById(R.id.password);
                String email = e.getEditText().getText().toString();
                String password = pd.getEditText().getText().toString();
                logIn(email, password);
            }
        });

        signup_btn = (Button)findViewById(R.id.elsesignup);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, SignUp.class));
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //if user is already signed in
        if (currentUser != null) {
            Toast.makeText(LogIn.this, "Signed in as: " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LogIn.this, Home.class));
            finish();
        }
    }

    private void logIn(String email, String password) {
        if (isValidEmail(email) & isValidPassword(password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "logInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent i = new Intent(LogIn.this, Home.class);
                                startActivity(i);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "logInWithEmail:failure", task.getException());
                                Toast.makeText(LogIn.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    private Boolean isValidEmail(String email){
        int flag = 0;
        if(email.equals("")){
            e.setError("Field cannot be empty!");
            flag = -1;
        }
        else{
            e.setError(null);
        }
        return flag==0? true : false;
    }
    private Boolean isValidPassword(String password){
        int flag = 0;
        if(password.equals("")){
            pd.setError("Field cannot be empty!");
            flag = -1;
        }
        else if(password.length()<8){
            pd.setError("Password too short!");
            flag = -1;
        }
        else{
            pd.setError(null);
        }
        return flag == 0? true: false;
    }
}