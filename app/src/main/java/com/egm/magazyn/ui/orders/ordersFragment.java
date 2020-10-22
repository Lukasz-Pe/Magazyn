package com.egm.magazyn.ui.orders;

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

import static com.egm.magazyn.data.dbClasses.dbContract.ordersEntry.*;

public class ordersFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private com.egm.magazyn.data.dbClasses.dbHelper dbHelper;
    private ordersCursorAdapter cursorAdapter;

    private static final int CLIENTS_LOADER =0;
    static final String[] CLIENTS_PROJECTION =new String[]{
            _ID,
            COL_CLIENT_ID,
            COL_PRODUCTS_IDS,
            COL_PRODUCTS_LOADED,
            COL_IS_DELIVERED
    };

    private static final String ARG_PARAM1="param1";
    private static final String ARG_PARAM2="param2";

    private String mParam1;
    private String mParam2;

    public ordersFragment(){
        //Req empty public c'tor
    }

    public static ordersFragment newInstance(String param1, String param2){
        ordersFragment fragment=new ordersFragment();
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
        View rootView=inflater.inflate(R.layout.main_list_layout,container,false);

        ListView lv=(ListView) rootView.findViewById(R.id.list_view);
        View emptyView=rootView.findViewById(R.id.empty_view);
        lv.setEmptyView(emptyView);

        dbHelper=new dbHelper(getContext());
        cursorAdapter=new ordersCursorAdapter(getContext(), null);
        lv.setAdapter(cursorAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent edit=new Intent (getActivity(), ordersEditorActivity.class);
                Uri content= ContentUris.withAppendedId(CONTENT_URI, id);
                edit.setData(content);
                startActivity(edit);
            }
        });

        final FloatingActionButton addItem = (FloatingActionButton) rootView.findViewById(R.id.main_list_add_item);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ordersEditorActivity.class);
                startActivity(intent);
            }
        });
        LoaderManager.getInstance(this).initLoader(CLIENTS_LOADER, null, this);
        return rootView;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getContext(),
                CONTENT_URI,
                CLIENTS_PROJECTION,
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