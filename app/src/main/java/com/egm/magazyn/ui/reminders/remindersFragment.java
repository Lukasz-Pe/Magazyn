package com.egm.magazyn.ui.reminders;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.egm.magazyn.R;
import com.egm.magazyn.data.dbClasses.dbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.egm.magazyn.data.dbClasses.dbContract.remindersEntry.*;

public class remindersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private com.egm.magazyn.data.dbClasses.dbHelper dbHelper;
    private remindersCursorAdapter cursorAdapter;

    private static final int REMINDERS_LOADER=0;
    static final String[] REMINDERS_PROJECTION=new String[]{
            _ID,
            COL_EQUIPMENT_NAME,
            COL_SERIAL_NUMBER,
            COL_NEXT_INSPECTION_DATE
    };

    private static final String ARG_PARAM1="param1";
    private static final String ARG_PARAM2="param2";

    private String mParam1;
    private String mParam2;

    public remindersFragment(){
        //Req empty public c'tor
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

        ListView lv=(ListView) rootView.findViewById(R.id.reminders_list_view);
        View emptyView=rootView.findViewById(R.id.reminders_empty_view);
        lv.setEmptyView(emptyView);

        dbHelper=new dbHelper(getContext());
        cursorAdapter=new remindersCursorAdapter(getContext(), null);
        lv.setAdapter(cursorAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editRem=new Intent (getActivity(), remindersEditorActivity.class);
                Uri content= ContentUris.withAppendedId(CONTENT_URI, id);
                editRem.setData(content);
                startActivity(editRem);
            }
        });

        final FloatingActionButton addAlert = (FloatingActionButton) rootView.findViewById(R.id.reminders_add_item_fab);
        addAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), remindersEditorActivity.class);
                startActivity(intent);
            }
        });
        LoaderManager.getInstance(this).initLoader(REMINDERS_LOADER, null, this);
        return rootView;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getContext(),
                CONTENT_URI,
                REMINDERS_PROJECTION,
                null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}