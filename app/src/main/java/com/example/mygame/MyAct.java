package com.example.mygame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyAct extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    Button btn,btn1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        btn=findViewById(R.id.btn_re);
        btn1=findViewById(R.id.btn_up);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth=FirebaseAuth.getInstance();
                String key=mAuth.getCurrentUser().getUid();
                databaseReference= FirebaseDatabase.getInstance().getReference("users/"+key);
                BestScore bs=new BestScore(10);
                databaseReference.child("easy").setValue(bs);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance();
                String key = mAuth.getCurrentUser().getUid();
                databaseReference = FirebaseDatabase.getInstance().getReference("users/"+key);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot val : snapshot.getChildren()) {
                                BestScore bs= val.getValue(BestScore.class);
                                if (bs != null) {
                                    // Convert the integer to a String before displaying in Toast
                                    String bestScoreString = String.valueOf(bs.getBestscore());
                                    System.out.println(bs.getBestscore());
                                    Toast.makeText(MyAct.this, bestScoreString,Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle errors here
                    }
                });
            }
        });


    }
}