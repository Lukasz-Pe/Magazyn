package com.egm.magazyn.ui.reminders;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import com.egm.magazyn.R;
import com.egm.magazyn.data.dbproviders.reminders.remindersDBHelper;
import com.egm.magazyn.ui.reminders.remindersCursorAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.egm.magazyn.data.dbproviders.reminders.remindersContract.remindersEntry.*;

import com.google.android.material.snackbar.Snackbar;

public class remindersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private remindersDBHelper dbHelper;
    private remindersCursorAdapter cursorAdapter;

    private static final int REMINDERS_LOADER=0;
    static final String[] REMINDERS_PROJECTION=new String[]{
            _ID,
            COLUMN_EQUIPMENT_NAME,
            COLUMN_NEXT_INSPECTION_DATE
    };

    private static final String ARG_PARAM1="param1";
    private static final String ARG_PARAM2="param2";

    private String mParam1;
    private String mParam2;

    public remindersFragment(){
        //Req empty public ctor
    }

    public static remindersFragment newInstance(String param1, String param2){
        remindersFragment fragment=new remindersFragment();
        Bundle args=new Bundle();
        args.putString(ARG_PARAM1,param1);
        args.putString(ARG_PARAM2,param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView=inflater.inflate(R.layout.reminders_layout,container,false);
        FloatingActionButton remFAB = (FloatingActionButton) rootView.findViewById(R.id.reminders_fab);
        remFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(remindersFragment.this, EditorActivity.class);
//                startActivity(intent);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return rootView;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}


/*    private remindersViewModel remindersViewModel;
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
    }*/