package com.egm.magazyn.ui.reminders;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class remindersViewModel extends androidx.lifecycle.ViewModel {

    private MutableLiveData<String> mText;
    private Context context;
    private String textToShow="dummy";
    public remindersViewModel() {

        mText = new MutableLiveData<>();
        mText.setValue("To jest fragment dla przypominajek");
    }
    public void setText(String text){
        textToShow=text;
    }
    public LiveData<String> getText() {
        return mText;
    }
}