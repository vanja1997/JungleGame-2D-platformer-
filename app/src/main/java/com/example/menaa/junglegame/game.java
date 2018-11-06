package com.example.menaa.junglegame;

import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.lang.reflect.AnnotatedElement;
import java.util.Timer;
import java.util.TimerTask;

public class game extends AppCompatActivity {
    AnimationDrawable run;
    private ImageView img;

    private int screenWidth;
    private int screenHeight;

    private  int cmp = 0;
    private  float manJSpeed = 10;
    private  int obSpeed = 5;
    private int flag;
    private TextView score;
    private int scr;
    private Button jump;
    private Button brk;
    private ImageView map;
    private ImageView map2;
    private ImageView obstacle;
    private ImageView man;
    private ImageView floor;
    private int base = screenHeight/2+100;
    private int state = 1;

    private  float mapLeftX;
    private  float mapLeftY;
    private float map2LeftX;
    private float map2LeftY;
    private float obstacleLeftX;
    private float obstacleLeftY;
    private float manLeftX;
    private float manLeftY;

    private Handler handler = new Handler();
    private Timer timer = new Timer();
    private Timer timer2 = new Timer();
    private Timer timer3 = new Timer();
    private Timer timer4 = new Timer();
    private Rect rc1 = new Rect();
    private Rect rc2 = new Rect();
    private MediaPlayer mySong;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        map = (ImageView) findViewById(R.id.map);
        score = (TextView) findViewById(R.id.score);
        map2 = (ImageView) findViewById(R.id.map2);
        jump = (Button) findViewById(R.id.jump);
        brk = (Button) findViewById(R.id.brk);
        obstacle = (ImageView) findViewById(R.id.obstacle);
        man = (ImageView) findViewById(R.id.man);
        floor = (ImageView) findViewById(R.id.floor);
        mySong = MediaPlayer.create(game.this,R.raw.platformer);
        //map2.setX(-80.0f);
        //map2.setY(-80.0f);
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(man.getY() == screenHeight/2){flag = 0;}
                man.setBackgroundResource(R.drawable.r2);
            }
        });

        brk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (state == 1) {
                    state = 0;
                    if (flag == 2)
                        man.setBackgroundResource(R.drawable.r3);
                }
                else{
                    state = 1;
                    if (flag == 2) {
                        man.setBackgroundResource(R.drawable.animation);
                        run = (AnimationDrawable) man.getBackground();
                        run.start();
                    }
                }
            }
        });

        man = (ImageView) findViewById(R.id.man);
        man.setBackgroundResource(R.drawable.animation);
        run = (AnimationDrawable) man.getBackground();
        init();
        run.start();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (state == 1) {
                            changePos();
                            changePos2();
                            changePos3();
                            changePos4();
                        }
                    }
                });
            }
        }, 0, 10);

    }



    public void changePos(){

        mapLeftX -= 1;
        if (map.getX() + map.getWidth() < 0){
            mapLeftX = screenWidth-15;
        }


        map2LeftX -= 1;
        if (map2.getX() + map2.getWidth() < 0){
            map2LeftX = screenWidth;
        }
        map.setX(mapLeftX);
        map.setY(mapLeftY);
        map2.setX(map2LeftX);
        map2.setY(map2LeftY);

    }

    public void changePos2(){
        scr++;
        score.setText("SCORE : "+Integer.toString(scr));
        if (scr%1000 == 0){
            obSpeed += 1;
        }
        obstacleLeftX -= obSpeed;
        if (obstacle.getX() + obstacle.getWidth() < 0){
            obstacleLeftX = screenWidth;
        }
        obstacle.setX(obstacleLeftX);
        obstacle.setY(obstacleLeftY);
    }

    public void changePos3(){

        if (flag == 0){
            if (manJSpeed - 0.1 > 2)  // La vitesse va descendre jusqu'à 2
                manJSpeed -= 0.1;     // Décrementation de la vitesse
            if (man.getY() > screenHeight/2 - 350)
                manLeftY -= manJSpeed;
            else {
                flag = 1;
                man.setBackgroundResource(R.drawable.r1);
            }
        }
        obstacle.getHitRect(rc1);
        man.getHitRect(rc2);
        if (Rect.intersects(rc1, rc2)) {
            finish();
        }

        man.setX(manLeftX);
        man.setY(manLeftY);
    }

    public void changePos4(){
        if (flag == 1) {
            if (man.getY() < screenHeight / 2) {
                manJSpeed += 0.1;    //Incrémentation de la vitesse (retombé)
                manLeftY += manJSpeed;
            }
            else {
                flag = 2;
                man.setBackgroundResource(R.drawable.animation);
                run = (AnimationDrawable) man.getBackground();
                run.start();
                manLeftY = screenHeight / 2;
                if (manLeftY == screenHeight / 2)
                    manJSpeed = 10;     //Réinitialisation de la vitesse
            }
        }

        man.setX(manLeftX);
        man.setY(manLeftY);
    }

    private void init () {
        mySong.start();
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        map2LeftX = screenWidth;
        obstacleLeftX = screenWidth;
        obstacleLeftY = screenHeight/2;
        manLeftY = screenHeight/2;
        manLeftX = 50;
        flag = 2;
        man.setY(manLeftY);
        man.setX(manLeftX);

        floor.setY(base);
    }


}
