package com.example.mygame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity7 extends AppCompatActivity {
    public TextView txt_easy,txt_medium,txt_hard;
    public int easy=0;
    public int medium=0;
    public int hard=0;
    private FirebaseAuth mAuth;
    private MediaPlayer mediaPlayer;
    private DatabaseReference databaseReference1,databaseReference2,databaseReference3;
    public Button go_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        txt_easy=findViewById(R.id.easy_high);
        txt_medium=findViewById(R.id.medium_high);
        txt_hard=findViewById(R.id.high_high);
        go_home=findViewById(R.id.button_back);
        mAuth = FirebaseAuth.getInstance();
        String key = mAuth.getCurrentUser().getUid();

        databaseReference1 = FirebaseDatabase.getInstance().getReference("users/"+key+"/easy");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("users/"+key+"/medium");
        databaseReference3 = FirebaseDatabase.getInstance().getReference("users/"+key+"/hard");

        mediaPlayer =MediaPlayer.create(this,R.raw.sillychipsong);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    BestScore bs= snapshot.getValue(BestScore.class);
                    if (bs != null) {
                        easy=bs.getBestscore();
                        txt_easy.append(String.valueOf(easy));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });
        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    BestScore bs= snapshot.getValue(BestScore.class);
                    if (bs != null) {
                        medium=bs.getBestscore();
                        txt_medium.append(String.valueOf(medium));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });
        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    BestScore bs= snapshot.getValue(BestScore.class);
                    if (bs != null) {
                        hard=bs.getBestscore();
                        txt_hard.append(String.valueOf(hard));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });

        go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent6=new Intent(MainActivity7.this,MainActivity.class);
                startActivity(intent6);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity7.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

}