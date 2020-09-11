package com.egm.magazyn.ui.general;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import android.content.Context;

public class myViewModel extends androidx.lifecycle.ViewModel {

    private MutableLiveData<String> mText;
    private Context context;
    private String textToShow="dummy";
    public myViewModel() {

        mText = new MutableLiveData<>();
        mText.setValue("To jest jakiś fragment");
    }
    public void setText(String text){
        textToShow=text;
    }
    public LiveData<String> getText() {
        return mText;
    }
}