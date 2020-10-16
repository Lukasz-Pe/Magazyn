package com.egm.magazyn.ui.clients;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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

import static com.egm.magazyn.data.dbClasses.dbContract.clientsEntry.COL_ADRESS;
import static com.egm.magazyn.data.dbClasses.dbContract.clientsEntry.COL_NAMES;
import static com.egm.magazyn.data.dbClasses.dbContract.clientsEntry.COL_NOTES;
import static com.egm.magazyn.data.dbClasses.dbContract.clientsEntry.COL_PHONE_NUMBER;
import static com.egm.magazyn.data.dbClasses.dbContract.clientsEntry.COL_SURNAME;
import static com.egm.magazyn.data.dbClasses.dbContract.clientsEntry.CONTENT_URI;
import static com.egm.magazyn.data.dbClasses.dbContract.clientsEntry._ID;

public class clientsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private com.egm.magazyn.data.dbClasses.dbHelper dbHelper;
    private clientsCursorAdapter cursorAdapter;

    private static final int CLIENTS_LOADER =0;
    static final String[] CLIENTS_PROJECTION =new String[]{
            _ID,
            COL_NAMES,
            COL_SURNAME,
            COL_PHONE_NUMBER,
            COL_ADRESS,
            COL_NOTES
    };

    private static final String ARG_PARAM1="param1";
    private static final String ARG_PARAM2="param2";

    private String mParam1;
    private String mParam2;

    public clientsFragment(){
        //Req empty public c'tor
    }

    public static clientsFragment newInstance(String param1, String param2){
        clientsFragment fragment=new clientsFragment();
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
        View rootView=inflater.inflate(R.layout.clients_layout,container,false);

        ListView lv=(ListView) rootView.findViewById(R.id.clients_list_view);
        View emptyView=rootView.findViewById(R.id.clients_empty_view);
        lv.setEmptyView(emptyView);

        dbHelper=new dbHelper(getContext());
        cursorAdapter=new clientsCursorAdapter(getContext(), null);
        lv.setAdapter(cursorAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent editRem=new Intent (getActivity(), clientsEditorActivity.class);
                Uri content= ContentUris.withAppendedId(CONTENT_URI, id);
                editRem.setData(content);
                startActivity(editRem);
            }
        });

        final FloatingActionButton addClient = (FloatingActionButton) rootView.findViewById(R.id.clients_add);
        addClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), clientsEditorActivity.class);
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

    public void clientsCall(View view){
        LinearLayout row = (LinearLayout) view.getParent();
        String phoneNumber="tel:" + row.findViewById(R.id.textView_clients_li_phone);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(phoneNumber));
        startActivity(intent);
    }

    public void clientsNavigate(View view){
        LinearLayout row = (LinearLayout) view.getParent();
        String address="geo:0,0?q=" + row.findViewById(R.id.textView_clients_li_adress);
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(address));
        startActivity(intent);
    }

}