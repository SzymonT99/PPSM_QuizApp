package com.ppsm.quiz_app.ui.add_question;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddQuestionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddQuestionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}