package com.egm.magazyn.ui.reminders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.egm.magazyn.R;

public class remindersFragment extends androidx.fragment.app.Fragment {

    private remindersViewModel remindersViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        remindersViewModel =
                new ViewModelProvider(this).get(remindersViewModel.class);
        View root = inflater.inflate(R.layout.reminders_layout, container, false);
        final TextView textView = root.findViewById(R.id.text_reminders);
        remindersViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}