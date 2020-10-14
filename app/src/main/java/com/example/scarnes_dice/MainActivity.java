package com.example.scarnes_dice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button rollButton, holdButton, resetButton;
    TextView userScore, computerScore, turnScore;
    ImageView diceImageView;
    Handler h = new Handler(Looper.getMainLooper());
    Runnable r;

    int userOverallScore, userTurnScore, computerOverallScore, computerTurnScore, i;
    boolean userTurn = true;

    public int roll(){
        int r = (int)(Math.random()*6) + 1;
        return r;
    }

    public void update(){

        String s;

        if(userTurn){
            s = getString(R.string.turnScoreUser) + userTurnScore;
            h.removeCallbacks(r);
        }else{
            s = getString(R.string.turnScoreComp) + computerTurnScore;
        }

        turnScore.setText(s);
        userScore.setText(userOverallScore+"");
        computerScore.setText(computerOverallScore+"");

        rollButton.setEnabled(false);
        holdButton.setEnabled(false);

        if(userOverallScore>=100){
            Toast.makeText(this, "You Win!!!", Toast.LENGTH_LONG).show();
            return;
        }else if(computerOverallScore>=100){
            Toast.makeText(this, "You Lose :(", Toast.LENGTH_LONG).show();
            return;
        }

        if(userTurn){
            rollButton.setEnabled(true);
            holdButton.setEnabled(true);
            i=0;
        }
    }

    public void roll(View view){
        int r = roll();

        diceImageView.animate().rotationBy(360f).setDuration(100).start();
        switch (r){
            case 1:
                diceImageView.setImageResource(R.drawable.dice1);
                break;
            case 2: diceImageView.setImageResource(R.drawable.dice2);break;
            case 3: diceImageView.setImageResource(R.drawable.dice3);break;
            case 4: diceImageView.setImageResource(R.drawable.dice4);break;
            case 5: diceImageView.setImageResource(R.drawable.dice5);break;
            case 6: diceImageView.setImageResource(R.drawable.dice6);break;
        }

        if(userTurn){
            if(r == 1){
                userTurnScore = 0;
                userTurn = false;
                Toast.makeText(this, "Oops, you rolled 1", Toast.LENGTH_SHORT).show();
            }else{
                userTurnScore+=r;
            }
        }else{
            if(r == 1){
                computerTurnScore = 0;
                userTurn = true;
                Toast.makeText(this, "Computer rolled 1", Toast.LENGTH_SHORT).show();
            }else{
                computerTurnScore+=r;
            }
        }

        update();

        if(userTurn == false && userTurnScore==0 && i == 0) computerTurn();

    }

    public void hold(View view){
        userOverallScore += userTurnScore;
        userTurnScore = 0;
        userTurn=false;
        update();
        turnScore.setText("User holds");
        computerTurn();

    }

    public void computerTurn(){


        if(!userTurn){

            if(i>=3){

                computerOverallScore += computerTurnScore;
                computerTurnScore = 0;
                userTurn=true;
                update();
                turnScore.setText("Computer holds");
                return;
            }
            i++;

            roll(rollButton);

            h.postDelayed(r, 2000);

        }
    }

    public void reset(View view){
        userOverallScore = 0;
        userTurnScore = 0;
        computerOverallScore = 0;
        computerTurnScore = 0;
        userTurn = true;

        update();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rollButton = findViewById(R.id.rollButton);
        holdButton = findViewById(R.id.holdButton);
        resetButton = findViewById(R.id.resetButton);
        userScore = findViewById(R.id.youScoreTextView);
        turnScore = findViewById(R.id.turnScore);
        computerScore = findViewById(R.id.compScoreTextView);
        diceImageView = findViewById(R.id.diceImageView);

        r = new Runnable() {
            @Override
            public void run() {
                computerTurn();
            }
        };

        reset(resetButton);

    }
}