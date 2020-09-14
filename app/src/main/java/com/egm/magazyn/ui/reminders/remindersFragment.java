package com.egm.magazyn.ui.reminders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.content.Context;
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
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        remindersViewModel =
                new ViewModelProvider(this).get(remindersViewModel.class);
        View root = inflater.inflate(R.layout.reminders_layout, container, false);
        final TextView textView = root.findViewById(R.id.rem_txt);
        remindersViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        FloatingActionButton rem_fab = root.findViewById(R.id.reminders_fab);
        rem_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return root;
    }
}