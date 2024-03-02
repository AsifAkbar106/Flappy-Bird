package com.example.mygame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GameView2 extends View {
    private Bird bird;
    private Handler handler;
    private Runnable r;
    private ArrayList<Pipe> arrPipes;
    private int sumpipe,distance;
    private int score,best_score=0;
    private Context context;
    private boolean start;

    public int level;

    private int soundJump;
    private float volume;
    private boolean loadedsound;
    private SoundPool soundPool;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    String key;

    public GameView2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        score=0;
        this.context=context;

        mAuth = FirebaseAuth.getInstance();
        String key = mAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("users/"+key+"/medium");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {

                    BestScore bs= snapshot.getValue(BestScore.class);
                    if (bs != null) {
                        best_score=bs.getBestscore();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });
        SharedPreferences sharedPreferences=context.getSharedPreferences("gamesetting",Context.MODE_PRIVATE);
        if(sharedPreferences!= null){
            best_score=sharedPreferences.getInt("Previous Best: ",0);
        }
        start=false;



        initBird();
        initPipe();

        handler=new Handler();
        r=new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        if(Build.VERSION.SDK_INT>=21){
            AudioAttributes audioAttributes =new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttributes).setMaxStreams(5);
            this.soundPool=builder.build();
        }
        else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC,0);
        }
        this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loadedsound = true;

            }
        });
        soundJump = this.soundPool.load(context,R.raw.jump_02,1);
    }

    private void initPipe() {

        sumpipe = 6;
        distance = 360 * Constants.SCREEN_HEIGHT / 1920;
        arrPipes = new ArrayList<>();
        for(int i=0; i < sumpipe ; i++){
            if(i<sumpipe/2){
                this.arrPipes.add(new Pipe(Constants.SCREEN_WIDTH+ i*((Constants.SCREEN_WIDTH + 250*Constants.SCREEN_WIDTH/1000)/(sumpipe/2)),0,200*Constants.SCREEN_WIDTH/1080,Constants.SCREEN_HEIGHT/2));
                this.arrPipes.get(this.arrPipes.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(),R.drawable.pipe2));
                this.arrPipes.get(this.arrPipes.size()-1).randomY();
            }
            else{
                this.arrPipes.add(new Pipe(this.arrPipes.get(i-sumpipe/2).getX(),this.arrPipes.get(i-sumpipe/2).getY() + this.arrPipes.get(i-sumpipe/2).getHeight() + this.distance,200*Constants.SCREEN_WIDTH/1080,Constants.SCREEN_HEIGHT/2));
                this.arrPipes.get(this.arrPipes.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(),R.drawable.pipe1));
            }
        }
    }

    private void initBird() {
        bird = new Bird();
        bird.setWidth(100*Constants.SCREEN_WIDTH/1080);
        bird.setHeight(100*Constants.SCREEN_HEIGHT/1920);
        bird.setX(100*Constants.SCREEN_WIDTH/1080);
        bird.setY(Constants.SCREEN_HEIGHT/2-bird.getHeight()/2);
        ArrayList<Bitmap> arrBms = new ArrayList<>();
        arrBms.add(BitmapFactory.decodeResource(this.getResources(),R.drawable.bird1));
        arrBms.add(BitmapFactory.decodeResource(this.getResources(),R.drawable.bird2));
        bird.setArrBms(arrBms);
    }

    public void draw(Canvas canvas){


        super.draw(canvas);
        if(start){
            bird.draw(canvas);
            for(int i =0;i< sumpipe ;i++){
                if(bird.getRect().intersect(arrPipes.get(i).getRect()) || bird.getY()-bird.getHeight()<0 || bird.getY()>Constants.SCREEN_HEIGHT){

                    Pipe.speed=0;
                    MainActivity3.txt_score_over.setText(MainActivity3.txt_score.getText());
                    MainActivity3.txt_best_score.setText("Previous Best: "+best_score);
                    MainActivity3.txt_score.setVisibility(INVISIBLE);
                    MainActivity3.rl_game_over.setVisibility(VISIBLE);

                }
                if(this.bird.getX() + this.bird.getWidth() > arrPipes.get(i).getX() + arrPipes.get(i).getWidth()/2 && this.bird.getX() + this.bird.getWidth() <= arrPipes.get(i).getX() + arrPipes.get(i).getWidth()/2 + Pipe.speed && i<sumpipe/2){

                    score++;
                    if(score>best_score){
                        best_score=score;
                        SharedPreferences sharedPreferences=context.getSharedPreferences("gamesetting",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putInt("Previous Best:",best_score);
                        editor.apply();
                        mAuth=FirebaseAuth.getInstance();
                        String key=mAuth.getCurrentUser().getUid();
                        databaseReference= FirebaseDatabase.getInstance().getReference("users/"+key);
                        BestScore bs=new BestScore(best_score);
                        databaseReference.child("medium").setValue(bs);
                    }
                    MainActivity3.txt_score.setText(""+score);

                }
                if(this.arrPipes.get(i).getX() < -arrPipes.get(i).getWidth()){
                    this.arrPipes.get(i).setX(Constants.SCREEN_WIDTH);
                    if(i< sumpipe/2){
                        arrPipes.get(i).randomY();
                    }
                    else{
                        arrPipes.get(i).setY(this.arrPipes.get(i - sumpipe/2).getY()+this.arrPipes.get(i-sumpipe/2).getHeight()+this.distance);
                    }
                }
                this.arrPipes.get(i).draw(canvas);
            }
        }else{
            if(bird.getY()> Constants.SCREEN_HEIGHT/2){

                bird.setDrop(-15*Constants.SCREEN_HEIGHT/1920);

            }
            bird.draw(canvas);
        }

        handler.postDelayed(r,10);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            bird.setDrop(-15);
            if(loadedsound){
                int streamId = this.soundPool.play(this.soundJump,(float)0.5,(float)0.5,1,0,1f );
            }

        }
        return true;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public void reset() {
        MainActivity3.txt_score.setText("0");
        score=0;

        initPipe();
        initBird();
    }
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}

