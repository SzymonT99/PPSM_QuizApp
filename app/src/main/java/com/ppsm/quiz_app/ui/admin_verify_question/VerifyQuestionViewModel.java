package com.ppsm.quiz_app.ui.admin_verify_question;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VerifyQuestionViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public VerifyQuestionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}