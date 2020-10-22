package com.egm.magazyn.ui.orders;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.egm.magazyn.R;
import com.egm.magazyn.data.dbClasses.dbProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import static com.egm.magazyn.data.dbClasses.dbContract.customersEntry.COL_ADRESS;
import static com.egm.magazyn.data.dbClasses.dbContract.customersEntry.COL_NAMES;
import static com.egm.magazyn.data.dbClasses.dbContract.customersEntry.COL_NOTES;
import static com.egm.magazyn.data.dbClasses.dbContract.customersEntry.COL_PHONE_NUMBER;
import static com.egm.magazyn.data.dbClasses.dbContract.customersEntry.COL_SURNAME;
import static com.egm.magazyn.data.dbClasses.dbContract.customersEntry.CONTENT_URI;
import static com.egm.magazyn.data.dbClasses.dbContract.customersEntry.TABLE_NAME;
import static com.egm.magazyn.data.dbClasses.dbContract.customersEntry._ID;

//import android.content.CursorLoader;

public class ordersEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private EditText names, surname, phoneNumber, address, notes;

    private static final String[] PROJECTION=new String[]{
            _ID,
            COL_NAMES,
            COL_SURNAME,
            COL_PHONE_NUMBER,
            COL_ADRESS,
            COL_NOTES
    };

    private Intent intent;

    private static final int EXISTING_LOADER =0;
    private boolean hasChanged=false, operationSuccessful=false;

    private FloatingActionButton save, delete, back;
    private DatePickerDialog picker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customers_editor_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        intent=getIntent();
        if(intent.getData()!=null){
            setTitle(R.string.edit_position);
            LoaderManager.getInstance(this).initLoader(EXISTING_LOADER, null, this);
        }else{
            setTitle(R.string.add_position);
        }
        names = (EditText) findViewById(R.id.editText_customers_names);
        surname = (EditText) findViewById(R.id.editText_customers_surname);
        phoneNumber = (EditText) findViewById(R.id.editText_customers_phone);
        address = (EditText) findViewById(R.id.editText_customers_address);
        notes = (EditText) findViewById(R.id.editText_customers_notes);
        save = findViewById(R.id.fab_customers_save);
        delete = findViewById(R.id.fab_customers_delete);
        back = findViewById(R.id.fab_customers_back);
        save.animate().translationY(-getResources().getDimension(R.dimen.fab_spacing));
        delete.animate().translationX(-getResources().getDimension(R.dimen.fab_spacing));
        delete.show();
        save.show();
        back.show();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContentResolver().delete(intent.getData(),TABLE_NAME, PROJECTION);
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view   ) {
                saveData(view);
                if(operationSuccessful)
                    finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void saveData(View view){
        String namesString = names.getText().toString().trim();
        String surnameString = surname.getText().toString().trim();
        String phoneNumberString = phoneNumber.getText().toString().trim();
        String addressString = address.getText().toString().trim();
        String notesString = notes.getText().toString().trim();
        if(namesString.isEmpty()){
            Snackbar.make(view,
                    getString(R.string.empty_field)+"\n"+getString(R.string.client_names),
                    Snackbar.LENGTH_LONG).show();
            return;
        }
        if(surnameString.isEmpty()){
            Snackbar.make(view,
                    getString(R.string.empty_field)+"\n"+getString(R.string.client_surname),
                    Snackbar.LENGTH_LONG).show();
            return;
        }
        if(phoneNumberString.isEmpty()){
            Snackbar.make(view,
                    getString(R.string.empty_field)+"\n"+getString(R.string.client_phone),
                    Snackbar.LENGTH_LONG).show();
            return;
        }
        if(addressString.isEmpty()){
            Snackbar.make(view,
                    getString(R.string.empty_field)+"\n"+getString(R.string.client_address),
                    Snackbar.LENGTH_LONG).show();
            return;
        }

        dbProvider rp=new dbProvider();

        ContentValues cvs=new ContentValues();
        cvs.put(COL_NAMES, namesString);
        cvs.put(COL_SURNAME, surnameString);
        cvs.put(COL_PHONE_NUMBER,phoneNumberString);
        cvs.put(COL_ADRESS, addressString);
        cvs.put(COL_NOTES, notesString);

        if(intent.getData()==null){
            Uri newRowID = getContentResolver().insert(CONTENT_URI, cvs);
            if(newRowID==null){
                Snackbar.make(view,
                        getString(R.string.row_saved_false),
                        Snackbar.LENGTH_LONG).show();
            }else{
                Snackbar.make(view,
                        getString(R.string.row_saved_true),
                        Snackbar.LENGTH_LONG).show();
                operationSuccessful=true;
            }
        }else{
            int affectedRows=getContentResolver().update(intent.getData(), cvs,
                    TABLE_NAME, PROJECTION);
            Snackbar.make(view,
                        getString(R.string.rows_updated+affectedRows),
                        Snackbar.LENGTH_LONG).show();
            operationSuccessful=true;
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                intent.getData(),
                PROJECTION,
                null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data.moveToFirst()){
            names.setText(data.getString(data.getColumnIndexOrThrow(COL_NAMES)));
            surname.setText(data.getString(data.getColumnIndexOrThrow(COL_SURNAME)));
            phoneNumber.setText(data.getString(data.getColumnIndexOrThrow(COL_PHONE_NUMBER)));
            address.setText(data.getString(data.getColumnIndexOrThrow(COL_ADRESS)));
            notes.setText(data.getString(data.getColumnIndexOrThrow(COL_NOTES)));
        }
    }
    View.OnTouchListener onTouchListener=new View.OnTouchListener(){
        @Override
        public boolean onTouch(View v, MotionEvent event){
            hasChanged=true;
            return false;
        }
    };

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        names.setText("");
        surname.setText("");
        phoneNumber.setText("");
        address.setText("");
        notes.setText("");
    }
}
