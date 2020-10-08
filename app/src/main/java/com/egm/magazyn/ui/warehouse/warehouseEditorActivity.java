package com.egm.magazyn.ui.warehouse;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.egm.magazyn.R;
import com.egm.magazyn.data.dbClasses.dbProvider;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;

import static com.egm.magazyn.data.dbClasses.dbContract.remindersEntry.COLUMN_EQUIPMENT_NAME;
import static com.egm.magazyn.data.dbClasses.dbContract.remindersEntry.COLUMN_NEXT_INSPECTION_DATE;
import static com.egm.magazyn.data.dbClasses.dbContract.remindersEntry.COLUMN_SERIAL_NUMBER;
import static com.egm.magazyn.data.dbClasses.dbContract.remindersEntry.CONTENT_URI;
import static com.egm.magazyn.data.dbClasses.dbContract.remindersEntry.TABLE_NAME;
import static com.egm.magazyn.data.dbClasses.dbContract.remindersEntry._ID;

//import android.content.CursorLoader;

public class warehouseEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private EditText equipmentName, serialNumber;
    private TextView nextInspectionDate;

    private static final String[] PROJECTION=new String[]{
            _ID,
            /*COLUMN_EQUIPMENT_NAME,
            COLUMN_SERIAL_NUMBER,
            COLUMN_NEXT_INSPECTION_DATE*/
    };

    private Intent intent;

    private static final int EXISTING_EQUIPMENT_LOADER=0;
    private boolean hasChanged=false;

    private FloatingActionButton menuFAB, close, save, delete, cancel;
    private DatePickerDialog picker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.warehouse_editor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        intent=getIntent();
        if(intent.getData()!=null){
            setTitle(R.string.edit_position);
            LoaderManager.getInstance(this).initLoader(EXISTING_EQUIPMENT_LOADER, null, this);
        }else{
            setTitle(R.string.add_position);
        }
        /*
        equipmentName = (EditText) findViewById(R.id.input_eq_name);
        serialNumber = (EditText) findViewById(R.id.input_eq_serial_number);
        nextInspectionDate = (TextView) findViewById(R.id.input_next_inspection_date);
        menuFAB = findViewById(R.id.reminders_edit_menu_fab);
        close= findViewById(R.id.reminders_edit_close_fab);
        save= findViewById(R.id.reminders_edit_save_fab);
        delete= findViewById(R.id.reminders_edit_delete_fab);
        cancel= findViewById(R.id.reminders_edit_cancel_fab);
        close.hide();
        delete.hide();
        save.hide();
        cancel.hide();
        menuFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMenu();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContentResolver().delete(intent.getData(),TABLE_NAME, PROJECTION);
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData(view);
                closeMenu();
                finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        nextInspectionDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                final int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(warehouseEditorActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String moy, dom;
                                monthOfYear++;
                                if(monthOfYear<10){
                                    moy="0"+String.valueOf(monthOfYear);
                                }else{
                                    moy=String.valueOf(monthOfYear);
                                }
                                if(dayOfMonth<10){
                                    dom="0"+String.valueOf(dayOfMonth);
                                }else{
                                    dom=String.valueOf(dayOfMonth);
                                }
                                nextInspectionDate.setText(year + "-" + moy + "-" + dom);
                            }
                        }, year, month, day);
                picker.show();
            }
        });*/

    }

    private void showMenu(){
        menuFAB.hide();
        close.show();
        delete.show();
        save.show();
        cancel.show();
        delete.animate().translationY(-3*getResources().getDimension(R.dimen.fab_spacing));
        save.animate().translationY(-2*getResources().getDimension(R.dimen.fab_spacing));
        cancel.animate().translationY(-getResources().getDimension(R.dimen.fab_spacing));
    }

    private void closeMenu(){
        delete.animate().translationY(0);
        save.animate().translationY(0);
        cancel.animate().translationY(0);
        close.hide();
        delete.hide();
        save.hide();
        cancel.hide();
        menuFAB.show();
    }

    private void saveData(View view){
        String equipmentNameText=equipmentName.getText().toString().trim();
        String serialNumberText=serialNumber.getText().toString().trim();
        String nextInspectionDateString=nextInspectionDate.getText().toString().trim();
        if(equipmentNameText.isEmpty()){
            Snackbar.make(view,
                    getString(R.string.empty_field)+"\n"+getString(R.string.string_equipment_name),
                    Snackbar.LENGTH_LONG).show();
//            Toast.makeText(this,
//                    getString(R.string.empty_field)+"\n"+getString(R.string.string_equipment_name),
//                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(serialNumberText.isEmpty()){
            Snackbar.make(view,
                    getString(R.string.empty_field)+"\n"+getString(R.string.string_equipment_serial_number),
                    Snackbar.LENGTH_LONG).show();
//            Toast.makeText(this,
//                    getString(R.string.empty_field)+"\n"+getString(R.string.string_equipment_serial_number),
//                    Toast.LENGTH_SHORT).show();
            return;
        }
        if(nextInspectionDateString.isEmpty()){
            Snackbar.make(view,
                    getString(R.string.empty_field)+"\n"+getString(R.string.string_next_inspection_date),
                    Snackbar.LENGTH_LONG).show();
//            Toast.makeText(this,
//                    getString(R.string.empty_field)+"\n"+getString(R.string.string_next_inspection_date),
//                    Toast.LENGTH_SHORT).show();
            return;
        }

        dbProvider rp=new dbProvider();

        ContentValues cvs=new ContentValues();
        cvs.put(COLUMN_EQUIPMENT_NAME, equipmentNameText);
        cvs.put(COLUMN_SERIAL_NUMBER, serialNumberText);
        cvs.put(COLUMN_NEXT_INSPECTION_DATE,nextInspectionDateString);

        if(intent.getData()==null){
            Uri newRowID = getContentResolver().insert(CONTENT_URI, cvs);
            if(newRowID==null){
                Snackbar.make(view,
                        getString(R.string.row_saved_false),
                        Snackbar.LENGTH_LONG).show();
//                Toast.makeText(this, getString(R.string.row_saved_false), Toast.LENGTH_SHORT).show();
            }else{
                Snackbar.make(view,
                        getString(R.string.row_saved_true),
                        Snackbar.LENGTH_LONG).show();
//                Toast.makeText(this, getString(R.string.row_saved_true), Toast.LENGTH_SHORT).show();
            }
        }else{
            int affectedRows=getContentResolver().update(intent.getData(), cvs,
                    TABLE_NAME, PROJECTION);
            Snackbar.make(view,
                        getString(R.string.rows_updated+affectedRows),
                        Snackbar.LENGTH_LONG).show();
//            Toast.makeText(this, getString(R.string.rows_updated+affectedRows), Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
//        return new CursorLoader(this,
//                intent.getData(),
//                PROJECTION,
//                null,null,null);
    return null;}

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(data.moveToFirst()){
//            equipmentName.setText(data.getString(data.getColumnIndexOrThrow(COLUMN_EQUIPMENT_NAME)));
//            serialNumber.setText(data.getString(data.getColumnIndexOrThrow(COLUMN_SERIAL_NUMBER)));
//            nextInspectionDate.setText(data.getString(data.getColumnIndexOrThrow(COLUMN_NEXT_INSPECTION_DATE)));
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
//        equipmentName.setText("");
//        serialNumber.setText("");
//        nextInspectionDate.setText(getString(R.string.string_next_inspection_date));
    }
}
