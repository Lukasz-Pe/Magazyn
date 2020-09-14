package com.egm.magazyn.ui.reminders;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class remindersViewModel extends androidx.lifecycle.ViewModel {

    private MutableLiveData<String> mText;
    public remindersViewModel() {

        mText = new MutableLiveData<>();
        mText.setValue("To jest fragment dla przypominajek");
    }
    public LiveData<String> getText() {
        return mText;
    }
}