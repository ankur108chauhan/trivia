package com.ankur.myapplication.util;

        import android.app.Activity;
        import android.content.Context;
        import android.content.SharedPreferences;
        import android.util.Log;

public class Preferences {

    private SharedPreferences preferences;

    public Preferences(Activity activity) {
        this.preferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public void setHighScore(int currentScore) {

        int prevHighScore = preferences.getInt("highScore",0);

        if(currentScore > prevHighScore) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("highScore",currentScore);
            Log.d("highScore","onResponse " + currentScore);
            editor.apply();
        }
    }

    public int getHighScore() {
        return preferences.getInt("highScore",0);
    }

    public void setCurrentQuestionIndex(int currentQuestionIndex) {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("currentQuestionIndex", currentQuestionIndex);
        Log.d("currentQuestionIndex","onResponse " + currentQuestionIndex);
        editor.apply();
    }

    public int getCurrentQuestionIndex() {
        return preferences.getInt("currentQuestionIndex",0);
    }

    public void setCurrentScore(int currentScore) {

        if(currentScore < getHighScore()) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("currentScore", currentScore);
            Log.d("currentScore","onResponse " + currentScore);
            editor.apply();
        }

        else {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("currentScore", getHighScore());
            Log.d("currentScore","onResponse " + getHighScore());
            editor.apply();
        }

    }

    public int getCurrentScore() {
        return preferences.getInt("currentScore",0);
    }
}
