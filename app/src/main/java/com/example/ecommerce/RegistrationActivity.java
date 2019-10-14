package com.example.ecommerce;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    private Context context;

    private Button register;
    private EditText name, phone, email, password, conPassword;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        context = RegistrationActivity.this;
        progressDialog = new ProgressDialog(context);

        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        conPassword = (EditText) findViewById(R.id.conPassword);
        register = (Button) findViewById(R.id.registerButton);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = name.getText().toString().trim();
                String Phone = phone.getText().toString().trim();
                String Email = email.getText().toString().trim();
                String Password = password.getText().toString();
                String ConfPassword = conPassword.getText().toString();

                CreateAccount(Name, Phone, Email, Password, ConfPassword);
            }
        });
    }

    private void CreateAccount(String Name, String Phone, String Email, String Password, String ConfPassword)
    {
        if(TextUtils.isEmpty(Name))
        {
            Toast.makeText(context, "Name required!", Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(Phone))
        {
            Toast.makeText(context, "Phone number required!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Email))
        {
            Toast.makeText(context, "Email address required!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Password))
        {
            Toast.makeText(context, "Password required!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(ConfPassword))
        {
            Toast.makeText(context, "Confirm your password!", Toast.LENGTH_SHORT).show();
        }
        else if(Patterns.EMAIL_ADDRESS.matcher(Email).matches() == false)
        {
            Toast.makeText(context, "Invalid email address!", Toast.LENGTH_SHORT).show();
        }
        else if(!Password.equals(ConfPassword))
        {
            Toast.makeText(context, "Password didn't match!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog.setTitle("Please Wait");
            progressDialog.setMessage("Creating new account");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            ValidatePhoneNumber(Name, Phone, Email, Password);  //already have that account or not
        }
    }

    private void ValidatePhoneNumber(final String Name, final String Phone, final String Email, final String Password)
    {
        final DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(Phone).exists()))  //if that phone not exist
                {
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("Name", Name);
                    userDataMap.put("Phone", Phone);
                    userDataMap.put("Email", Email);
                    userDataMap.put("Password", Password);

                    RootRef.child("Users").child(Phone).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Account creation successful!", Toast.LENGTH_LONG).show();

                                        Intent mainIntent = new Intent(context, MainActivity.class);
                                        startActivity(mainIntent);
                                    }
                                    else
                                    {
                                        progressDialog.dismiss();
                                        Toast.makeText(context, "Operation failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(context, "Account already exist!", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
