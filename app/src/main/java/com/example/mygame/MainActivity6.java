package com.example.mygame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;

public class MainActivity6 extends AppCompatActivity {
    private Button button2;
    private EditText email2;
    private EditText password2;

    private EditText name2;
    private EditText gamerid2;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        mAuth=FirebaseAuth.getInstance();
        button2=findViewById(R.id.button2);
        email2=findViewById(R.id.editTextEmailAddress2);
        password2=findViewById(R.id.editTextNumberPassword2);
        name2=findViewById(R.id.name2);


        databaseReference= FirebaseDatabase.getInstance().getReference("info");

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userRegister();


            }


        });
    }



    private void userRegister() {

        String email=email2.getText().toString().trim();
        String password=password2.getText().toString().trim();
        if(email.isEmpty()){
            email2.setError("Enter an Email address");
            email2.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email2.setError("Enter a valid email address");
            email2.requestFocus();
            return;
        }
        if(password.isEmpty()){
            password2.setError("Enter a Password");
            password2.requestFocus();
            return;
        }
        if(password.length()<6){
            password2.setError("Minimum Length has to be 6");
            password2.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Toast.makeText(getApplicationContext(),"Register Successful",Toast.LENGTH_SHORT).show();
                    Intent intent5=new Intent(MainActivity6.this,MainActivity.class);
                    startActivity(intent5);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Register Unsuccessful",Toast.LENGTH_SHORT).show();

                }
            }
        });



    }
    public void onBackPressed() {
        Intent intent=new Intent(MainActivity6.this, MainActivity5.class);
        startActivity(intent);
        finish();
    }
}