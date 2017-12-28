package com.example.chongchenlearn901.braintrainer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private ArrayList<Button> answerBtns;

    private ConstraintLayout playLayout;
    private int answer;
    private int rightCount = 0;
    private int totalCount = 0;

    private TextView tvNum1;
    private TextView tvNum2;
    private TextView tvScore;
    private TextView tvTimer;
    private Button btnPlayAgain;
    private int timeLength = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGo = findViewById(R.id.btnGo);
        this.playLayout = findViewById(R.id.playLayout);
        this.tvNum1 = findViewById(R.id.tvNum1);
        this.tvNum2 = findViewById(R.id.tvNum2);
        this.tvScore = findViewById(R.id.tvScore);
        this.tvTimer = findViewById(R.id.tvTimer);
        this.btnPlayAgain = findViewById(R.id.btnPlayAgain);

        btnGo.setOnClickListener(goListener);

        answerBtns = new ArrayList<>(Arrays.asList(
                findViewById(R.id.btn1),
                findViewById(R.id.btn2),
                findViewById(R.id.btn3),
                findViewById(R.id.btn4)
        ));

        for (Button btn : this.answerBtns) {
            btn.setOnClickListener(this.guessListener);
        }

        this.btnPlayAgain.setOnClickListener(playAgainListener);
    }


    private View.OnClickListener goListener = (v) -> {
        v.setVisibility(View.INVISIBLE);
        playLayout.setVisibility(View.VISIBLE);
        newGame();
    };

    private View.OnClickListener guessListener = (v) -> {
        Button btn = (Button) v;
        int guess = Integer.parseInt(btn.getText().toString());
        if (guess == answer) {
            this.rightCount++;
        }
        this.totalCount++;

        setResultText();
        newQuestion();
    };

    private View.OnClickListener playAgainListener = (v) -> newGame();

    private void newGame() {
        if (this.btnPlayAgain != null) {
            this.btnPlayAgain.setVisibility(View.INVISIBLE);
        }
        new NewGameCounter(timeLength * 1000 + 100, 1000).start();
        this.totalCount = 0;
        this.rightCount = 0;
        setResultText();
        setAnswerEnable(true);
        newQuestion();
    }

    private void newQuestion() {
        int range = 100;
        if (this.answerBtns == null || this.answerBtns.size() == 0) {
            return;
        }

        int randIndex = (int) (Math.random() * this.answerBtns.size());
        int num1 = (int) (Math.random() * range);
        int num2 = (int) (Math.random() * range);

        answer = num1 + num2;
        setTvText(tvNum1, String.valueOf(num1));
        setTvText(tvNum2, String.valueOf(num2));

        for (int i = 0; i < this.answerBtns.size(); i++) {
            Button btn = this.answerBtns.get(i);
            if (i == randIndex) {
                btn.setText(String.valueOf(answer));
                continue;
            }
            int random;
            while ((random = (int) (Math.random() * range * 2 + 1)) == answer) {
                randIndex = (int) (Math.random() * range * 2 + 1);
            }
            btn.setText(String.valueOf(random));
        }
    }


    private void setTvText(TextView tv, String text) {
        if (tv == null) {
            return;
        }
        tv.setText(text);
    }

    private class NewGameCounter extends CountDownTimer {
        NewGameCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            tvTimer.setText(String.format("%ds", (int) millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
//            tvTimer.setText("0s");
            if (btnPlayAgain != null) {
                btnPlayAgain.setVisibility(View.VISIBLE);
            }
            setAnswerEnable(false);
        }
    }

    private void setAnswerEnable(boolean enable) {
        for (Button btn : this.answerBtns) {
            btn.setEnabled(enable);
        }
    }

    private void setResultText() {
        this.tvScore.setText(String.format("%d/%d", this.rightCount, this.totalCount));
    }
}
