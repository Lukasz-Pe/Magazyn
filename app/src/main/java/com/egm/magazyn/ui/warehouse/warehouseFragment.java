package com.egm.magazyn.ui.warehouse;

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
import com.egm.magazyn.data.dbproviders.reminders.remindersDBHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import static com.egm.magazyn.data.dbproviders.reminders.remindersContract.remindersEntry.COLUMN_EQUIPMENT_NAME;
import static com.egm.magazyn.data.dbproviders.reminders.remindersContract.remindersEntry.COLUMN_NEXT_INSPECTION_DATE;
import static com.egm.magazyn.data.dbproviders.reminders.remindersContract.remindersEntry.COLUMN_SERIAL_NUMBER;
import static com.egm.magazyn.data.dbproviders.reminders.remindersContract.remindersEntry.CONTENT_URI;
import static com.egm.magazyn.data.dbproviders.reminders.remindersContract.remindersEntry._ID;

public class warehouseFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private remindersDBHelper dbHelper;
    private warehouseCursorAdapter cursorAdapter;

    private static final int REMINDERS_LOADER=0;
    static final String[] REMINDERS_PROJECTION=new String[]{
            _ID,
            COLUMN_EQUIPMENT_NAME,
            COLUMN_SERIAL_NUMBER,
            COLUMN_NEXT_INSPECTION_DATE
    };

    private static final String ARG_PARAM1="param1";
    private static final String ARG_PARAM2="param2";

    private String mParam1;
    private String mParam2;

    public warehouseFragment(){
        //Req empty public c'tor
    }

    public static warehouseFragment newInstance(String param1, String param2){
        warehouseFragment fragment=new warehouseFragment();
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
        View emptyView=rootView.findViewById(R.id.empty_view);
        lv.setEmptyView(emptyView);

        dbHelper=new remindersDBHelper(getContext());
        cursorAdapter=new warehouseCursorAdapter(getContext(), null);
        lv.setAdapter(cursorAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editRem=new Intent (getActivity(), warehouseEditorActivity.class);
                Uri content= ContentUris.withAppendedId(CONTENT_URI, id);
                editRem.setData(content);
                startActivity(editRem);
            }
        });

        final FloatingActionButton addPosition = (FloatingActionButton) rootView.findViewById(R.id.reminders_add_item_fab);
        addPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), warehouseEditorActivity.class);
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