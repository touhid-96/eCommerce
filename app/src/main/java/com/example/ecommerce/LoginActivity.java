package com.example.ecommerce;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private Context context;
    private ProgressDialog progressDialog;

    private EditText email, password;
    private TextView needAccount, forgotPassword;
    private CheckBox rememberMeChkBox;
    private Button loginBtn;

    private FirebaseAuth mAuth;

    private String ParentDbName = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = LoginActivity.this;
        progressDialog = new ProgressDialog(context);
        mAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        needAccount = (TextView) findViewById(R.id.needAccount);
        forgotPassword = (TextView) findViewById(R.id.forgotPassword);
        rememberMeChkBox = (CheckBox) findViewById(R.id.remember_me_chkbox);
        loginBtn = (Button) findViewById(R.id.loginButton);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                String Password = password.getText().toString();

                LoginUser(Email, Password);
            }
        });
    }

    private void LoginUser(String Email, String Password)
    {
        if(TextUtils.isEmpty(Email))
        {
            Toast.makeText(context, "Enter email address!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Password))
        {
            Toast.makeText(context, "Enter your password!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Logging in");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            AllowAccessToAccount(Email, Password);
        }
    }

    private void AllowAccessToAccount(final String Email, String Password)
    {
        mAuth.signInWithEmailAndPassword(Email, Password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            if(mAuth.getCurrentUser().isEmailVerified())
                            {
                                Intent homeIntent = new Intent(context, HomeActivity.class);
                                homeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                progressDialog.dismiss();
                                startActivity(homeIntent);
                                finish();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(context, "Please verify your email!", Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Login failed!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
