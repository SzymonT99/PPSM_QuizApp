package com.ppsm.quiz_app.ui.ranking;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RankingViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public RankingViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}