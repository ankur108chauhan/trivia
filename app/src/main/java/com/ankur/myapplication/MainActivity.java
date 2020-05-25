package com.ankur.myapplication;

        import androidx.appcompat.app.ActionBar;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.cardview.widget.CardView;

        import android.content.Context;
        import android.content.Intent;
        import android.os.Bundle;
        import android.os.Vibrator;
        import android.util.Log;
        import android.view.View;
        import android.view.animation.AlphaAnimation;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.Button;
        import android.widget.ImageButton;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.ankur.myapplication.R.color;
        import com.ankur.myapplication.data.QuestionBank;
        import com.ankur.myapplication.data.QuestionListAsyncResponse;
        import com.ankur.myapplication.model.Question;
        import com.ankur.myapplication.model.Score;
        import com.ankur.myapplication.util.Preferences;

        import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView questionCounter;
    private TextView questionsTextview;
    private TextView scoreTextview;
    private TextView highScoreTextview;
//    private ImageButton backButton;
//    private ImageButton nextButton;
    private Button trueButton;
    private Button falseButton;
    private Button shareScore;
    private final String Message_ID = "message_Id";
    Vibrator vibrator;
    private int currentQuestionIndex = 0;
    private int currentScore = 0;
    private int highScore = 0;

    private Preferences preferences;


    ArrayList<Question> questionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        questionCounter = findViewById(R.id.counter_textview);
        questionsTextview = findViewById(R.id.question_textview);
//        backButton = findViewById(R.id.back_button);
//        nextButton = findViewById(R.id.next_button);
        trueButton = findViewById(R.id.true_button);
        falseButton = findViewById(R.id.false_button);
        scoreTextview = findViewById(R.id.score_textView);
        shareScore = findViewById(R.id.share_button);
        highScoreTextview = findViewById(R.id.highScore_textView);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

//        backButton.setOnClickListener(this);
//        nextButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        scoreTextview.setOnClickListener(this);
        questionCounter.setOnClickListener(this);
        shareScore.setOnClickListener(this);

        preferences = new Preferences(MainActivity.this);
        highScore = preferences.getHighScore();
        Log.d("receive","onClick " + preferences.getHighScore());
        highScoreTextview.setText("Highest Score: " + String.valueOf(highScore));
        currentScore = preferences.getCurrentScore();
        scoreTextview.setText(String.valueOf("Current Score: " + currentScore));
        currentQuestionIndex = preferences.getCurrentQuestionIndex();

        questionList = new QuestionBank().getQuestionBank(new QuestionListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionsTextview.setText(questionArrayList.get(currentQuestionIndex).getQuestion());
                questionCounter.setText(currentQuestionIndex + "/" + questionArrayList.size());
                Log.d("JSON","onResponse" + questionArrayList);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

//            case R.id.back_button:
//
//                if(currentQuestionIndex > 0) {
//                    currentQuestionIndex = (currentQuestionIndex - 1) % questionList.size();
//                    updateQuestion();
//                }
//                vibrator.vibrate(50);
//                break;
//
//            case R.id.next_button:
//                vibrator.vibrate(50);
//                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
//                updateQuestion();
//                break;

            case R.id.true_button:
                vibrator.vibrate(50);
                checkAnswer(true);
                updateQuestion();
                break;

            case R.id.false_button:
                vibrator.vibrate(50);
                checkAnswer(false);
                updateQuestion();
                break;

            case R.id.counter_textview:
                vibrator.vibrate(50);
                currentQuestionIndex = 0;
                updateQuestion();
                resetTrivia();
                break;

            case R.id.score_textView:
                vibrator.vibrate(50);
                resetTrivia();
                break;

            case R.id.share_button:
                vibrator.vibrate(50);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_SUBJECT,"My Trivia Score");
                intent.putExtra(Intent.EXTRA_TEXT,"My Current score is: "+ currentScore +
                        " and My Highest Score is: " + highScore);
                startActivity(Intent.createChooser(intent,"Sharing Score"));
        }
    }

    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getQuestion();
        questionsTextview.setText(question);
        questionCounter.setText(currentQuestionIndex + "/" + questionList.size());
    }

    public void checkAnswer(boolean userChoice) {

        boolean answer = questionList.get(currentQuestionIndex).getAnswer();

        if(userChoice == answer) {
            addScore();
            fadeView();
            Toast.makeText(MainActivity.this,R.string.correct_answer,Toast.LENGTH_SHORT).show();
        }
        else {
            vibrator.vibrate(250);
            subtractScore();
            shakeAnimation();
            Toast.makeText(MainActivity.this, R.string.incorrect_answer, Toast.LENGTH_SHORT).show();

        }
    }

    private void addScore() {
        currentScore = currentScore + 10;
        scoreTextview.setText("Current Score: " + String.valueOf(currentScore));
    }

    private void subtractScore() {

        if(currentScore > 0) {
            currentScore = currentScore - 10;
            scoreTextview.setText("Current Score: " + String.valueOf(currentScore));
        }
        else {
            scoreTextview.setText("Current Score: " + String.valueOf(currentScore));
        }

    }

    private void resetTrivia() {
        currentScore = 0;
        preferences.setCurrentScore(currentScore);
        currentScore = preferences.getCurrentScore();
        scoreTextview.setText(String.valueOf("Current Score: " + currentScore));
    }

    private void shakeAnimation() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        final CardView cardView = findViewById(R.id.cardview);
        cardView.setAnimation(shake);

        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //cardView.setCardBackgroundColor(Color.RED);
                cardView.setCardBackgroundColor(getResources().getColor(color.false_color));

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //cardView.setCardBackgroundColor(Color.parseColor("#474747"));
                cardView.setCardBackgroundColor(getResources().getColor(color.colorPrimary));
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void fadeView() {

        final CardView cardView = findViewById(R.id.cardview);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);

        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(getResources().getColor(color.true_color));
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(getResources().getColor(color.colorPrimary));
                currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
                updateQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        preferences.setHighScore(currentScore);
        preferences.setCurrentQuestionIndex(currentQuestionIndex);
        preferences.setCurrentScore(currentScore);
    }
}
