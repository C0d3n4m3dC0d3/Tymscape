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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private com.google.android.material.textfield.TextInputLayout un, e, pd;
    private Button signup_btn, login_btn;

    private static final String TAG = "SignUp";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        signup_btn = (Button) findViewById((R.id.signup_btn1));
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                un = findViewById(R.id.username);
                String username = un.getEditText().getText().toString();
                e = findViewById(R.id.emailid);
                String email = e.getEditText().getText().toString();
                pd = findViewById(R.id.password);
                String password = pd.getEditText().getText().toString();

                createAccount(email, password, username);
            }
        });

        login_btn = (Button) findViewById(R.id.elsesignin);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, LogIn.class));
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
            Toast.makeText(SignUp.this, "Signed in as: " + currentUser.getEmail(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(SignUp.this, Home.class));
            finish();
        }
    }

    private void createAccount(String email, String password, String username) {
        if (isValidPasswd(password) & isValidEmail(email) & isValidUsername(username)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                addUserToDatabase(email, password, username);
                                Toast.makeText(SignUp.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void addUserToDatabase(String email, String password, String username) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference();
        DBHelper user = new DBHelper();
        String id = mAuth.getCurrentUser().getUid();

        //set Values
        user.setID(id);
        user.setEmail(email);
        user.setPassword(password);
        user.setUname(username);

        //Insert value into database
        dbRef.child("user").child(id).setValue(user);

        //if successful, then move to user home
        startActivity(new Intent(SignUp.this, Home.class));
    }

    private Boolean isValidUsername(String uname) {
        String whiteSpace = "\\A\\w{4,20}\\z";
        if (uname.isEmpty()) {
            un.setError("Field cannot be empty");
            return false;
        } else if (!uname.matches(whiteSpace)) {
            un.setError("Whitespaces are not allowed");
            return false;
        } else if (uname.length() > 15) {
            un.setError("Username too long");
            return false;
        } else {
            un.setError(null);
            return true;
        }
    }

    private Boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.isEmpty()) {
            e.setError("Field cannot be empty");
            return false;
        } else if (!email.matches(emailPattern)) {
            e.setError("Invalid email address");
            return false;
        } else {
            e.setError(null);
            return true;
        }
    }

    private Boolean isValidPasswd(String pass) {
        String pwdPattern = "^" + "(?=.*[a-zA-Z])" + "(?=.*[0-9])" + "(?=\\S+$)" + ".{8,}" + "$";
        if (pass.isEmpty()) {
            pd.setError("Field cannot be empty");
            return false;
        } else if (!pass.matches(pwdPattern)) {
            pd.setError("Password is too weak");
            return false;
        } else {
            pd.setError(null);
            return true;
        }
    }
}