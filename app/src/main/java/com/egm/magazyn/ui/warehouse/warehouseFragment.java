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
import com.egm.magazyn.data.dbClasses.dbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.egm.magazyn.data.dbClasses.dbContract.warehouseEntry.*;

public class warehouseFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private com.egm.magazyn.data.dbClasses.dbHelper dbHelper;
    private warehouseCursorAdapter cursorAdapter;

    private static final int WAREHOUSE_LOADER =0;
    static final String[] WAREHOUSE_PROJECTION =new String[]{
            _ID,
            COL_PRODUCT_NAME,
            COL_QUANTITY,
            COL_ACCOUNTING_UNIT,
            COL_UNIT_PRICE,
            COL_QUANTITY_AFTER_LAST_DELIVERY,
            COL_LOW_QUANTITY_WARNING,
            COL_LOW_QUANTITY_WARNING_UNIT,
            COL_LOW_QUANTITY_ALARM,
            COL_LOW_QUANTITY_ALARM_UNIT,
            COL_SOURCE,
            COL_LAST_DELIVERY_PRICE,
            COL_LAST_DELIVERY_DATE
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
        View rootView=inflater.inflate(R.layout.warehouse_layout,container,false);

        ListView lv=(ListView) rootView.findViewById(R.id.warehouse_list_view);
        View emptyView=rootView.findViewById(R.id.empty_view);
        lv.setEmptyView(emptyView);

        dbHelper=new dbHelper(getContext());
        cursorAdapter=new warehouseCursorAdapter(getContext(), null);
        lv.setAdapter(cursorAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editWare=new Intent (getActivity(), warehouseEditorActivity.class);
                Uri content= ContentUris.withAppendedId(CONTENT_URI, id);
                editWare.setData(content);
                startActivity(editWare);
            }
        });

        final FloatingActionButton addPosition = (FloatingActionButton) rootView.findViewById(R.id.warehouse_add_product);
        addPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), warehouseEditorActivity.class);
                startActivity(intent);
            }
        });
        LoaderManager.getInstance(this).initLoader(WAREHOUSE_LOADER, null, this);
        return rootView;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getContext(),
                CONTENT_URI,
                WAREHOUSE_PROJECTION,
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