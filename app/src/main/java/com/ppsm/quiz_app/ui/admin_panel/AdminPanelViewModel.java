package com.ppsm.quiz_app.ui.admin_panel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AdminPanelViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AdminPanelViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}