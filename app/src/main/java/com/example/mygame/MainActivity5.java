package com.example.mygame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity5 extends AppCompatActivity {
    private Button button1,button2;
    private EditText email1;
    private EditText password1;
    private TextView textView1;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        mAuth=FirebaseAuth.getInstance();

        button1=findViewById(R.id.button1);
        email1=findViewById(R.id.editText1);
        password1=findViewById(R.id.password1);
        textView1=findViewById(R.id.textView1);
        button2=findViewById(R.id.button2);

        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6=new Intent(MainActivity5.this, MainActivity6.class);
                startActivity(intent6);
                finish();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();

            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent9=new Intent(MainActivity5.this,MainActivity8.class);
                startActivity(intent9);
                finish();
            }
        });


    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setMessage("Are You Sure?").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton("No",null).show();
    }
    private void userLogin() {
        String email=email1.getText().toString().trim();
        String password=password1.getText().toString().trim();
        if(email.isEmpty()){
            email1.setError("Please input Email");
            email1.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            email1.setError("Enter proper email address");
            email1.requestFocus();
            return;
        }
        if(password.isEmpty()){
            password1.setError("Please input password");
            password1.requestFocus();
            return;
        }
        if(password.length()<6){
            password1.setError("Wrong Password");
            password1.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent3=new Intent(MainActivity5.this, MainActivity.class);
                    intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent3);
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(),"couldnot sign in",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}