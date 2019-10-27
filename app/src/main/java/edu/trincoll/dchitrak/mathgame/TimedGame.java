package edu.trincoll.dchitrak.mathgame;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TimedGame extends AppCompatActivity {

    protected int counter = 0;
    protected final int MAXTIME = 30000;    // max time in millis
    protected final int SECOND = 1000;      // sec is 1000 millis
    private int numCorrect = 0;

    private NumTrack tracker = new NumTrack();
    private GenerateProblem prob = new GenerateProblem(1, 10);

    protected void clearInputText() {

        EditText inputText = findViewById(R.id.guessInput);
        inputText.setText("");

    }


    protected void displayProblem() {

        // set up layout for problem
        TextView num1Text = findViewById(R.id.num1);
        TextView num2Text =  findViewById(R.id.num2);
        TextView opText =  findViewById(R.id.operator);

        //init vars and screen
        prob.makeProblem();
        num1Text.setText(prob.getNum1()+"");
        num2Text.setText(prob.getNum2()+"");
        opText.setText(prob.getOp()+"");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timed_game);

        final EditText inputText = findViewById(R.id.guessInput);
        final TextView timerText = findViewById(R.id.countdown);

        displayProblem();


        // create and display display a timer
        final CountDownTimer timer = new CountDownTimer(MAXTIME, SECOND){

            @Override
            public void onTick(long millisLeft) {
                timerText.setText(String.valueOf(MAXTIME/SECOND- counter));  // display a countdown timer in secs
                counter++;
            }

            @Override
            // go to results page when time is up
            public void onFinish(){
                Intent startint = new Intent(getApplicationContext(), ResultsPage.class);
                startint.putExtra("Score", tracker.getScore()+"");
                startint.putExtra("Time", tracker.getTime()+"");
                startActivity(startint);
            }


        }.start();

        NumTrack tracker = new NumTrack();



        //set up exit button, which returns to home page
       ImageButton exit = (ImageButton) findViewById(R.id.exitButton);

       exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                Intent startint = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(startint);
            }
        });


       // set up submit button
        Button submitButton = findViewById(R.id.submitButton);

       submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String guessString = inputText.getText().toString();
                int guess = Integer.parseInt(guessString);

                if (guess == prob.getResults()) {  // problem answered correctly
                    numCorrect++;
                    tracker.recalculateScore();
                    displayProblem();           // change problem only if answered correctly
                } else {
                    tracker.resetStreak();
                }

                clearInputText();

            }
        });

    }


}
