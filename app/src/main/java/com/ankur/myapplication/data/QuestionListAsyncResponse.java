package com.ankur.myapplication.data;

import com.ankur.myapplication.model.Question;

import java.util.ArrayList;

public interface QuestionListAsyncResponse {
    void processFinished(ArrayList<Question> questionArrayList);
}
