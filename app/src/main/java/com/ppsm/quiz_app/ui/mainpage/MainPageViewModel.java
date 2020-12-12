package com.ppsm.quiz_app.ui.mainpage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainPageViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MainPageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}