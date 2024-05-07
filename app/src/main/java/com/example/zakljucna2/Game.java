package com.example.zakljucna2;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;

import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class Game extends AppCompatActivity {
   private float height;


   private float x;
   private float y;


    private GameView gameView;
    private List<platform> platforms;

    private boolean falling;

    private boolean onPlatform=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        setContentView(R.layout.activity_game);
        gameView=findViewById(R.id.game_view);

        Button left = findViewById(R.id.left);
        Button right = findViewById(R.id.right);
        Button space = findViewById(R.id.jump);
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        height = metrics.heightPixels;
        platforms = new ArrayList<>();
        platforms.add(new platform(100, 500, 300, 50));  // x, y, width, height
        platforms.add(new platform(500, 800, 400, 50));
        platforms.add(new platform(1400,600,400,60));
        platforms.add(new platform(700,500,150,50));
        gameView.setPlatforms(platforms);
        character character=new character(150,80,0,(height-150));
        falling=character.getFalling();
        gameView.setCharacter(character);
       Enemy enemy=new Enemy(920,(height-150),150,80,920,(height-150));





        Handler mHandler=new Handler();



        Runnable r=new Runnable() {
            @Override
            public void run() {
                x=character.getX();
                character.setX(x-15);

                mHandler.postDelayed(this,20);

            }
        };
        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        gameView.setCharacterDir(1);
                        gameView.startLeftAnimation();
                        mHandler.post(r);
                        break;
                    case MotionEvent.ACTION_UP:
                        gameView.stopLeftAnimation();
                        mHandler.removeCallbacks(r);
                        break;


                }



                return true;



            }
        });
        Runnable r1=new Runnable() {
            @Override
            public void run() {
                x=character.getX();
                character.setX(x+15);
                gameView.invalidate();
                mHandler.postDelayed(this,20);
            }
        };
        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()){
                    case MotionEvent.ACTION_DOWN:

                       gameView.setCharacterDir(2);
                       gameView.startRightAnimation();
                        mHandler.post(r1);
                        break;
                    case MotionEvent.ACTION_UP:
                        gameView.stopRightAnimation();
                        mHandler.removeCallbacks(r1);
                        break;
                }


                return true;
            }
        });
        Runnable r2=new Runnable() {
            @Override
            public void run() {


                if (!falling) {
                    Log.d("ss","platform"+collision.platformCollisionAbove(character,platforms));
                    x=character.getX();
                    y=character.getY();

                    character.setjumpVelocity(character.getjumpVelocity()-character.getWeight());

                    float newY=y-character.getjumpVelocity();
                    if (newY >= height - character.getHeight()) {
                        falling = true;
                        onPlatform=false;
                        character.setjumpVelocity(0);  // Reset jump velocity
                        character.setY(height - character.getHeight());  // Adjust Y position to the floor


                    }else if(collision.platformCollisionAbove(character,platforms)){
                        if(!onPlatform) {
                            character.setjumpVelocity(0);
                            falling = true;
                            onPlatform=true;
                            int index1 = platform.getIndexAbove(character, platforms);
                            character.setY(platforms.get(index1).getY() - character.getHeight() );
                        }else{
                            character.setY(newY);
                        }




                       } else if(collision.platformCollisionBelow(character,platforms)){


                    character.setjumpVelocity(0);

                    int index=platform.getIndexBelow(character ,platforms);
                    character.setY(platforms.get(index).getY()+character.getHeight()+1);



                } else {

                        character.setY(newY);  // Otherwise, continue falling
                        onPlatform=false;

                    }

                    gameView.invalidate();  // Redraw the view
                    mHandler.postDelayed(this, 5);  // Continue the jump loop
                    Log.d("GG","FALLING"+falling);
                    Log.d("EE","Jump velocity "+character.getjumpVelocity());
                }


            }
        };
        space.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(falling){

                    falling=false;
                    character.setjumpVelocity(character.getjumpStrenght());

                    mHandler.post(r2);


                }



            }
        });




    }

}
